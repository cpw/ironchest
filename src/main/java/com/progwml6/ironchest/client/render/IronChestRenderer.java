package com.progwml6.ironchest.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.progwml6.ironchest.IronChestsClientEvents;
import com.progwml6.ironchest.client.model.IronChestsModels;
import com.progwml6.ironchest.client.model.inventory.ModelItem;
import com.progwml6.ironchest.common.block.regular.AbstractIronChestBlock;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import com.progwml6.ironchest.common.block.entity.ICrystalChest;
import com.progwml6.ironchest.common.block.trapped.entity.AbstractTrappedIronChestBlockEntity;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class IronChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {

  private final ModelPart lid;
  private final ModelPart bottom;
  private final ModelPart lock;

  private final BlockEntityRenderDispatcher renderer;

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

  public IronChestRenderer(BlockEntityRendererProvider.Context context) {
    ModelPart modelPart = context.bakeLayer(IronChestsClientEvents.IRON_CHEST);

    this.renderer = context.getBlockEntityRenderDispatcher();
    this.bottom = modelPart.getChild("iron_bottom");
    this.lid = modelPart.getChild("iron_lid");
    this.lock = modelPart.getChild("iron_lock");
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition meshDefinition = new MeshDefinition();
    PartDefinition partDefinition = meshDefinition.getRoot();

    partDefinition.addOrReplaceChild("iron_bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
    partDefinition.addOrReplaceChild("iron_lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
    partDefinition.addOrReplaceChild("iron_lock", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 8.0F, 0.0F));

    return LayerDefinition.create(meshDefinition, 64, 64);
  }

  @Override
  public void render(T tileEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
    AbstractIronChestBlockEntity tileEntity = (AbstractIronChestBlockEntity) tileEntityIn;

    Level level = tileEntity.getLevel();
    boolean useTileEntityBlockState = level != null;

    BlockState blockState = useTileEntityBlockState ? tileEntity.getBlockState() : (BlockState) tileEntity.getBlockToUse().defaultBlockState().setValue(AbstractIronChestBlock.FACING, Direction.SOUTH);
    Block block = blockState.getBlock();
    IronChestsTypes chestType = IronChestsTypes.IRON;
    IronChestsTypes actualType = AbstractIronChestBlock.getTypeFromBlock(block);

    if (actualType != null) {
      chestType = actualType;
    }

    if (block instanceof AbstractIronChestBlock abstractChestBlock) {
      poseStack.pushPose();

      float f = blockState.getValue(AbstractIronChestBlock.FACING).toYRot();

      poseStack.translate(0.5D, 0.5D, 0.5D);
      poseStack.mulPose(Vector3f.YP.rotationDegrees(-f));
      poseStack.translate(-0.5D, -0.5D, -0.5D);

      DoubleBlockCombiner.NeighborCombineResult<? extends AbstractIronChestBlockEntity> neighborCombineResult;

      if (useTileEntityBlockState) {
        neighborCombineResult = abstractChestBlock.combine(blockState, level, tileEntityIn.getBlockPos(), true);
      } else {
        neighborCombineResult = DoubleBlockCombiner.Combiner::acceptNone;
      }

      float openness = neighborCombineResult.<Float2FloatFunction>apply(AbstractIronChestBlock.opennessCombiner(tileEntity)).get(partialTicks);
      openness = 1.0F - openness;
      openness = 1.0F - openness * openness * openness;

      int brightness = neighborCombineResult.<Int2IntFunction>apply(new BrightnessCombiner<>()).applyAsInt(combinedLightIn);

      boolean trapped = tileEntityIn instanceof AbstractTrappedIronChestBlockEntity;

      Material material = new Material(Sheets.CHEST_SHEET, IronChestsModels.chooseChestTexture(chestType, trapped));

      VertexConsumer vertexConsumer = material.buffer(bufferSource, RenderType::entityCutout);

      this.render(poseStack, vertexConsumer, this.lid, this.lock, this.bottom, openness, brightness, combinedOverlayIn);

      poseStack.popPose();

      if (chestType.isTransparent() && tileEntity instanceof ICrystalChest crystalChest && Vec3.atCenterOf(tileEntityIn.getBlockPos()).closerThan(this.renderer.camera.getPosition(), 128d)) {
        float rotation = (float) (360D * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL) - partialTicks;

        for (int j = 0; j < MODEL_ITEMS.size() - 1; j++) {
          renderItem(poseStack, bufferSource, crystalChest.getTopItems().get(j), MODEL_ITEMS.get(j), rotation, combinedLightIn);
        }
      }
    }
  }

  private void render(PoseStack poseStack, VertexConsumer vertexConsumer, ModelPart lid, ModelPart lock, ModelPart bottom, float openness, int brightness, int combinedOverlayIn) {
    lid.xRot = -(openness * ((float) Math.PI / 2F));
    lock.xRot = lid.xRot;

    lid.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
    lock.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
    bottom.render(poseStack, vertexConsumer, brightness, combinedOverlayIn);
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
  public static void renderItem(PoseStack matrices, MultiBufferSource buffer, ItemStack item, ModelItem modelItem, float rotation, int light) {
    // if no stack, skip
    if (item.isEmpty()) return;

    // start rendering
    matrices.pushPose();
    Vector3f center = modelItem.getCenter();
    matrices.translate(center.x(), center.y(), center.z());

    matrices.mulPose(Vector3f.YP.rotation(rotation));

    // scale
    float scale = modelItem.getSizeScaled();
    matrices.scale(scale, scale, scale);

    // render the actual item
    Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, matrices, buffer, 0);

    matrices.popPose();
  }
}
