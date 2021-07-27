package com.progwml6.ironchest.client.render;

import com.google.common.primitives.SignedBytes;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.progwml6.ironchest.client.model.IronChestsModels;
import com.progwml6.ironchest.client.model.inventory.ModelItem;
import com.progwml6.ironchest.common.block.GenericIronChestBlock;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.tileentity.CrystalChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class IronChestTileEntityRenderer<T extends TileEntity & IChestLid> extends TileEntityRenderer<T> {

  private final ModelRenderer chestLid;
  private final ModelRenderer chestBottom;
  private final ModelRenderer chestLock;

  private static ItemEntity customItem;
  private static ItemRenderer itemRenderer;

  private static final List<ModelItem> MODEL_ITEMS = Arrays.asList(
    new ModelItem(new Vector3f(0.3F, 0.45F, 0.3F), 3.0F),
    new ModelItem(new Vector3f(0.7F, 0.45F, 0.3F), 3.0F),
    new ModelItem(new Vector3f(0.3F, 0.45F, 0.7F), 3.0F),
    new ModelItem(new Vector3f(0.7F, 0.45F, 0.7F), 3.0F),
    new ModelItem(new Vector3f(0.3F, 0.1F, 0.3F), 3.0F),
    new ModelItem(new Vector3f(0.7F, 0.1F, 0.3F), 3.0F),
    new ModelItem(new Vector3f(0.3F, 0.1F, 0.7F), 3.0F),
    new ModelItem(new Vector3f(0.7F, 0.1F, 0.7F), 3.0F),
    new ModelItem(new Vector3f(0.5F, 0.32F, 0.5F), 3.0F)
  );

  public IronChestTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
    super(tileEntityRendererDispatcher);

    this.chestBottom = new ModelRenderer(64, 64, 0, 19);
    this.chestBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
    this.chestLid = new ModelRenderer(64, 64, 0, 0);
    this.chestLid.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
    this.chestLid.rotationPointY = 9.0F;
    this.chestLid.rotationPointZ = 1.0F;
    this.chestLock = new ModelRenderer(64, 64, 0, 0);
    this.chestLock.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
    this.chestLock.rotationPointY = 8.0F;
  }

  @Override
  public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
    GenericIronChestTileEntity tileEntity = (GenericIronChestTileEntity) tileEntityIn;

    World world = tileEntity.getWorld();
    boolean flag = world != null;

    BlockState blockstate = flag ? tileEntity.getBlockState() : (BlockState) tileEntity.getBlockToUse().getDefaultState().with(GenericIronChestBlock.FACING, Direction.SOUTH);
    Block block = blockstate.getBlock();
    IronChestsTypes chestType = IronChestsTypes.IRON;
    IronChestsTypes actualType = GenericIronChestBlock.getTypeFromBlock(block);

    if (actualType != null) {
      chestType = actualType;
    }

    if (block instanceof GenericIronChestBlock) {
      GenericIronChestBlock ironChestBlock = (GenericIronChestBlock) block;

      matrixStackIn.push();
      float f = blockstate.get(GenericIronChestBlock.FACING).getHorizontalAngle();
      matrixStackIn.translate(0.5D, 0.5D, 0.5D);
      matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
      matrixStackIn.translate(-0.5D, -0.5D, -0.5D);

      TileEntityMerger.ICallbackWrapper<? extends GenericIronChestTileEntity> iCallbackWrapper;
      if (flag) {
        iCallbackWrapper = ironChestBlock.getWrapper(blockstate, world, tileEntity.getPos(), true);
      } else {
        iCallbackWrapper = TileEntityMerger.ICallback::func_225537_b_;
      }

      float f1 = iCallbackWrapper.apply(GenericIronChestBlock.getLid((IChestLid) tileEntity)).get(partialTicks);
      f1 = 1.0F - f1;
      f1 = 1.0F - f1 * f1 * f1;
      int i = iCallbackWrapper.apply(new DualBrightnessCallback<>()).applyAsInt(combinedLightIn);

      RenderMaterial material = new RenderMaterial(Atlases.CHEST_ATLAS, IronChestsModels.chooseChestTexture(chestType));
      IVertexBuilder ivertexbuilder = material.getBuffer(bufferIn, RenderType::getEntityCutout);

      this.handleModelRender(matrixStackIn, ivertexbuilder, this.chestLid, this.chestLock, this.chestBottom, f1, i, combinedOverlayIn);

      matrixStackIn.pop();

      if (chestType.isTransparent() && tileEntity instanceof CrystalChestTileEntity && Vector3d.copyCentered(tileEntityIn.getPos()).isWithinDistanceOf(this.renderDispatcher.renderInfo.getProjectedView(), 128d)) {
        float rotation = (float) (360D * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) - partialTicks;
        CrystalChestTileEntity crystalChestTileEntity = (CrystalChestTileEntity) tileEntity;

        if (customItem == null) {
          assert world != null;
          customItem = new ItemEntity(EntityType.ITEM, world);
        }

        for (int j = 0; j < MODEL_ITEMS.size() - 1; j++) {
          renderItem(matrixStackIn, bufferIn, crystalChestTileEntity.getTopItems().get(j), MODEL_ITEMS.get(j), rotation, combinedLightIn, partialTicks);
        }
      }
    }
  }

  private void handleModelRender(MatrixStack matrixStackIn, IVertexBuilder iVertexBuilder, ModelRenderer firstModel, ModelRenderer secondModel, ModelRenderer thirdModel, float f1, int p_228871_7_, int p_228871_8_) {
    firstModel.rotateAngleX = -(f1 * ((float) Math.PI / 2F));
    secondModel.rotateAngleX = firstModel.rotateAngleX;
    firstModel.render(matrixStackIn, iVertexBuilder, p_228871_7_, p_228871_8_);
    secondModel.render(matrixStackIn, iVertexBuilder, p_228871_7_, p_228871_8_);
    thirdModel.render(matrixStackIn, iVertexBuilder, p_228871_7_, p_228871_8_);
  }

  /**
   * Renders a single item in a TESR
   *
   * @param matrices  Matrix stack instance
   * @param buffer    Buffer instance
   * @param item      Item to render
   * @param modelItem Model items for render information
   * @param light     Model light
   */
  public static void renderItem(MatrixStack matrices, IRenderTypeBuffer buffer, ItemStack item, ModelItem modelItem, float rotation, int light, float partialTicks) {
    // if no stack, skip
    if (item.isEmpty()) return;

    customItem.setItem(item);

    // start rendering
    matrices.push();
    Vector3f center = modelItem.getCenter();
    matrices.translate(center.getX(), center.getY(), center.getZ());

    matrices.rotate(Vector3f.YP.rotation(rotation));

    // scale
    float scale = modelItem.getSizeScaled();
    matrices.scale(scale, scale, scale);

    // render the actual item
    if (itemRenderer == null) {
      itemRenderer = new ItemRenderer(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getItemRenderer()) {
        @Override
        public int getModelCount(ItemStack stack) {
          return SignedBytes.saturatedCast(Math.min(stack.getCount() / 32, 15) + 1);
        }

        @Override
        public boolean shouldBob() {
          return false;
        }

        @Override
        public boolean shouldSpreadItems() {
          return true;
        }
      };
    }

    itemRenderer.render(customItem, 0F, partialTicks, matrices, buffer, light);
    matrices.pop();
  }
}
