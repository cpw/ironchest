package com.progwml6.ironchest.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.progwml6.ironchest.common.block.GenericIronChestBlock;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class IronChestTileEntityRenderer<T extends TileEntity & IChestLid> extends TileEntityRenderer<T> {

  private final ModelRenderer chestLid;
  private final ModelRenderer chestBottom;
  private final ModelRenderer chestLock;

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
      }
      else {
        iCallbackWrapper = TileEntityMerger.ICallback::func_225537_b_;
      }

      float f1 = iCallbackWrapper.apply(GenericIronChestBlock.getLid((IChestLid) tileEntity)).get(partialTicks);
      f1 = 1.0F - f1;
      f1 = 1.0F - f1 * f1 * f1;
      int i = iCallbackWrapper.apply(new DualBrightnessCallback<>()).applyAsInt(combinedLightIn);

      Material material = IronChestsModels.chooseChestModel(tileEntity, chestType);
      IVertexBuilder ivertexbuilder = material.getBuffer(bufferIn, RenderType::getEntityCutout);

      this.handleModelRender(matrixStackIn, ivertexbuilder, this.chestLid, this.chestLock, this.chestBottom, f1, i, combinedOverlayIn);

      matrixStackIn.pop();
    }
  }

  private void handleModelRender(MatrixStack matrixStackIn, IVertexBuilder iVertexBuilder, ModelRenderer firstModel, ModelRenderer secondModel, ModelRenderer thirdModel, float f1, int p_228871_7_, int p_228871_8_) {
    firstModel.rotateAngleX = -(f1 * ((float) Math.PI / 2F));
    secondModel.rotateAngleX = firstModel.rotateAngleX;
    firstModel.render(matrixStackIn, iVertexBuilder, p_228871_7_, p_228871_8_);
    secondModel.render(matrixStackIn, iVertexBuilder, p_228871_7_, p_228871_8_);
    thirdModel.render(matrixStackIn, iVertexBuilder, p_228871_7_, p_228871_8_);
  }
}
