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

  private final IronChestsTypes chestType;

  private final int textureXSize;

  private final int textureYSize;

  public IronChestScreen(IronChestContainer container, PlayerInventory playerInventory, ITextComponent title) {
    super(container, playerInventory, title);

    this.chestType = container.getChestType();
    this.xSize = container.getChestType().xSize;
    this.ySize = container.getChestType().ySize;
    this.textureXSize = container.getChestType().textureXSize;
    this.textureYSize = container.getChestType().textureYSize;

    this.passEvents = false;
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
    this.font.func_243248_b(matrixStack, this.title, 8.0F, 6.0F, 4210752);

    this.font.func_243248_b(matrixStack, this.playerInventory.getDisplayName(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

    this.minecraft.getTextureManager().bindTexture(this.chestType.guiTexture);

    int x = (this.width - this.xSize) / 2;
    int y = (this.height - this.ySize) / 2;

    blit(matrixStack, x, y, 0, 0, this.xSize, this.ySize, this.textureXSize, this.textureYSize);
  }
}

