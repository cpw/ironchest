package com.progwml6.ironchest.client.render;

import com.google.common.primitives.SignedBytes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.progwml6.ironchest.client.model.IronChestsModels;
import com.progwml6.ironchest.client.model.inventory.ModelItem;
import com.progwml6.ironchest.common.block.GenericIronChestBlock;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.tileentity.CrystalChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

public class IronChestTileEntityRenderer<T extends BlockEntity & LidBlockEntity> extends BlockEntityRenderer<T> {

  private final ModelPart chestLid;
  private final ModelPart chestBottom;
  private final ModelPart chestLock;

  private static ItemEntity customItem;
  private static ItemEntityRenderer itemRenderer;

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

  public IronChestTileEntityRenderer(BlockEntityRenderDispatcher tileEntityRendererDispatcher) {
    super(tileEntityRendererDispatcher);

    this.chestBottom = new ModelPart(64, 64, 0, 19);
    this.chestBottom.addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F);
    this.chestLid = new ModelPart(64, 64, 0, 0);
    this.chestLid.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
    this.chestLid.y = 9.0F;
    this.chestLid.z = 1.0F;
    this.chestLock = new ModelPart(64, 64, 0, 0);
    this.chestLock.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
    this.chestLock.y = 8.0F;
  }

  @Override
  public void render(T tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
    GenericIronChestTileEntity tileEntity = (GenericIronChestTileEntity) tileEntityIn;

    Level world = tileEntity.getLevel();
    boolean flag = world != null;

    BlockState blockstate = flag ? tileEntity.getBlockState() : (BlockState) tileEntity.getBlockToUse().defaultBlockState().setValue(GenericIronChestBlock.FACING, Direction.SOUTH);
    Block block = blockstate.getBlock();
    IronChestsTypes chestType = IronChestsTypes.IRON;
    IronChestsTypes actualType = GenericIronChestBlock.getTypeFromBlock(block);

    if (actualType != null) {
      chestType = actualType;
    }

    if (block instanceof GenericIronChestBlock) {
      GenericIronChestBlock ironChestBlock = (GenericIronChestBlock) block;

      matrixStackIn.pushPose();
      float f = blockstate.getValue(GenericIronChestBlock.FACING).toYRot();
      matrixStackIn.translate(0.5D, 0.5D, 0.5D);
      matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-f));
      matrixStackIn.translate(-0.5D, -0.5D, -0.5D);

      DoubleBlockCombiner.NeighborCombineResult<? extends GenericIronChestTileEntity> iCallbackWrapper;
      if (flag) {
        iCallbackWrapper = ironChestBlock.getWrapper(blockstate, world, tileEntity.getBlockPos(), true);
      } else {
        iCallbackWrapper = DoubleBlockCombiner.Combiner::acceptNone;
      }

      float f1 = iCallbackWrapper.apply(GenericIronChestBlock.getLid((LidBlockEntity) tileEntity)).get(partialTicks);
      f1 = 1.0F - f1;
      f1 = 1.0F - f1 * f1 * f1;
      int i = iCallbackWrapper.apply(new BrightnessCombiner<>()).applyAsInt(combinedLightIn);

      Material material = new Material(Sheets.CHEST_SHEET, IronChestsModels.chooseChestTexture(chestType));
      VertexConsumer ivertexbuilder = material.buffer(bufferIn, RenderType::entityCutout);

      this.handleModelRender(matrixStackIn, ivertexbuilder, this.chestLid, this.chestLock, this.chestBottom, f1, i, combinedOverlayIn);

      matrixStackIn.popPose();

      if (chestType.isTransparent() && tileEntity instanceof CrystalChestTileEntity && Vec3.atCenterOf(tileEntityIn.getBlockPos()).closerThan(this.renderer.camera.getPosition(), 128d)) {
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

  private void handleModelRender(PoseStack matrixStackIn, VertexConsumer iVertexBuilder, ModelPart firstModel, ModelPart secondModel, ModelPart thirdModel, float f1, int p_228871_7_, int p_228871_8_) {
    firstModel.xRot = -(f1 * ((float) Math.PI / 2F));
    secondModel.xRot = firstModel.xRot;
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
  public static void renderItem(PoseStack matrices, MultiBufferSource buffer, ItemStack item, ModelItem modelItem, float rotation, int light, float partialTicks) {
    // if no stack, skip
    if (item.isEmpty()) return;

    customItem.setItem(item);

    // start rendering
    matrices.pushPose();
    Vector3f center = modelItem.getCenter();
    matrices.translate(center.x(), center.y(), center.z());

    matrices.mulPose(Vector3f.YP.rotation(rotation));

    // scale
    float scale = modelItem.getSizeScaled();
    matrices.scale(scale, scale, scale);

    // render the actual item
    if (itemRenderer == null) {
      itemRenderer = new ItemEntityRenderer(Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer()) {
        @Override
        public int getRenderAmount(ItemStack stack) {
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
    matrices.popPose();
  }
}
