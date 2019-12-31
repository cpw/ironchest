package com.progwml6.ironchest.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class IronChestItemStackRenderer<T extends TileEntity> extends ItemStackTileEntityRenderer {

  private final Supplier<T> te;

  public IronChestItemStackRenderer(Supplier<T> te) {
    this.te = te;
  }

  @Override
  public void func_228364_a_(ItemStack itemStackIn, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int p_228364_4_, int p_228364_5_) {
    TileEntityRendererDispatcher.instance.func_228852_a_(this.te.get(), matrixStack, iRenderTypeBuffer, p_228364_4_, p_228364_5_);
  }
}
