/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors:
 * cpw - initial API and implementation
 ******************************************************************************/
package com.progwml6.ironchest.client.gui;

import com.progwml6.ironchest.common.blocks.IronChestType;
import com.progwml6.ironchest.common.gui.ContainerIronChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GUIChest extends GuiContainer
{
    public enum ResourceList
    {
        IRON(new ResourceLocation("ironchest", "textures/gui/iron_container.png")),
        COPPER(new ResourceLocation("ironchest", "textures/gui/copper_container.png")),
        SILVER(new ResourceLocation("ironchest", "textures/gui/silver_container.png")),
        GOLD(new ResourceLocation("ironchest", "textures/gui/gold_container.png")),
        DIAMOND(new ResourceLocation("ironchest", "textures/gui/diamond_container.png")),
        DIRT(new ResourceLocation("ironchest", "textures/gui/dirt_container.png"));

        public final ResourceLocation location;

        ResourceList(ResourceLocation loc)
        {
            this.location = loc;
        }
    }

    public enum GUI
    {
        IRON(184, 202, ResourceList.IRON, IronChestType.IRON, new ResourceLocation("ironchest:iron")),
        GOLD(184, 256, ResourceList.GOLD, IronChestType.GOLD, new ResourceLocation("ironchest:gold")),
        DIAMOND(238, 256, ResourceList.DIAMOND, IronChestType.DIAMOND, new ResourceLocation("ironchest:diamond")),
        COPPER(184, 184, ResourceList.COPPER, IronChestType.COPPER, new ResourceLocation("ironchest:copper")),
        SILVER(184, 238, ResourceList.SILVER, IronChestType.SILVER, new ResourceLocation("ironchest:silver")),
        CRYSTAL(238, 256, ResourceList.DIAMOND, IronChestType.CRYSTAL, new ResourceLocation("ironchest:diamond")),
        OBSIDIAN(238, 256, ResourceList.DIAMOND, IronChestType.OBSIDIAN, new ResourceLocation("ironchest:obsidian")),
        DIRTCHEST9000(184, 184, ResourceList.DIRT, IronChestType.DIRTCHEST9000, new ResourceLocation("ironchest:dirt"));

        private int xSize;

        private int ySize;

        private ResourceList guiResourceList;

        private IronChestType mainType;

        private ResourceLocation guiId;

        GUI(int xSize, int ySize, ResourceList guiResourceList, IronChestType mainType, ResourceLocation guiId)
        {
            this.xSize = xSize;
            this.ySize = ySize;
            this.guiResourceList = guiResourceList;
            this.mainType = mainType;
            this.guiId = guiId;
        }

        protected Container makeContainer(IInventory playerInventory, IInventory chestInventory, EntityPlayer player)
        {
            return new ContainerIronChest(playerInventory, chestInventory, this.mainType, player, this.xSize, this.ySize);
        }

        public ResourceLocation getGuiId()
        {
            return this.guiId;
        }
    }

    private GUI type;

    private final IInventory upperChestInventory;

    private final IInventory lowerChestInventory;

    public GUIChest(GUI type, IInventory playerInventory, IInventory chestInventory)
    {
        super(type.makeContainer(playerInventory, chestInventory, Minecraft.getInstance().player));
        this.type = type;
        this.xSize = type.xSize;
        this.ySize = type.ySize;
        this.upperChestInventory = playerInventory;
        this.lowerChestInventory = chestInventory;
        this.allowUserInput = false;
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    /*
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(this.lowerChestInventory.getDisplayName().getString(), 8.0F, 6.0F, 4210752);
        this.fontRenderer.drawString(this.upperChestInventory.getDisplayName().getString(), 8.0F, this.ySize - 96 + 2, 4210752);
    }*/

    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(this.type.guiResourceList.location);

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
