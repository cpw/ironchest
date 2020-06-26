package com.progwml6.ironchest.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.inventory.IronChestContainer;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IronChestScreen extends ContainerScreen<IronChestContainer> implements IHasContainer<IronChestContainer> {

  private IronChestsTypes chestType;

  private int textureXSize;

  private int textureYSize;

  public IronChestScreen(IronChestContainer container, PlayerInventory playerInventory, ITextComponent title) {
    super(container, playerInventory, title);

    this.chestType = container.getChestType();
    this.xSize = container.getChestType().xSize;
    this.ySize = container.getChestType().ySize;
    this.textureXSize = container.getChestType().textureXSize;
    this.textureYSize = container.getChestType().textureYSize;

    this.field_230711_n_  = false;
  }

  @Override
  public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.func_230446_a_(matrixStack);
    super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
    this.func_230459_a_(matrixStack, mouseX, mouseY);
  }

  @Override
  protected void func_230451_b_(MatrixStack matrixStack, int mouseX, int mouseY) {
    this.field_230712_o_.func_238422_b_(matrixStack, this.field_230704_d_, 8.0F, 6.0F, 4210752);
    this.field_230712_o_.func_238422_b_(matrixStack, this.playerInventory.getDisplayName(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
  }

  @Override
  protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    this.field_230706_i_.getTextureManager().bindTexture(this.chestType.guiTexture);

    int x = (this.field_230708_k_ - this.xSize) / 2;
    int y = (this.field_230709_l_ - this.ySize) / 2;

    func_238463_a_(matrixStack, x, y, 0, 0, this.xSize, this.ySize, this.textureXSize, this.textureYSize);
  }
}

