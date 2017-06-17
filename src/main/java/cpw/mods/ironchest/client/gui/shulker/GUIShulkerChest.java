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
package cpw.mods.ironchest.client.gui.shulker;

import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.gui.shulker.ContainerIronShulkerBox;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityIronShulkerBox;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GUIShulkerChest extends GuiContainer
{
    public enum ResourceList
    {
        //@formatter:off
        IRON(new ResourceLocation("ironchest", "textures/gui/iron_container.png")),
        COPPER(new ResourceLocation("ironchest", "textures/gui/copper_container.png")),
        SILVER(new ResourceLocation("ironchest", "textures/gui/silver_container.png")),
        GOLD(new ResourceLocation("ironchest", "textures/gui/gold_container.png")),
        DIAMOND(new ResourceLocation("ironchest", "textures/gui/diamond_container.png"));
        //@formatter:on

        public final ResourceLocation location;

        ResourceList(ResourceLocation loc)
        {
            this.location = loc;
        }
    }

    public enum GUI
    {
        //@formatter:off
        IRON(184, 202, ResourceList.IRON, IronShulkerBoxType.IRON),
        GOLD(184, 256, ResourceList.GOLD, IronShulkerBoxType.GOLD),
        DIAMOND(238, 256, ResourceList.DIAMOND, IronShulkerBoxType.DIAMOND),
        COPPER(184, 184, ResourceList.COPPER, IronShulkerBoxType.COPPER),
        SILVER(184, 238, ResourceList.SILVER, IronShulkerBoxType.SILVER),
        CRYSTAL(238, 256, ResourceList.DIAMOND, IronShulkerBoxType.CRYSTAL),
        OBSIDIAN(238, 256, ResourceList.DIAMOND,IronShulkerBoxType.OBSIDIAN);
        //@formatter:on

        private int xSize;

        private int ySize;

        private ResourceList guiResourceList;

        private IronShulkerBoxType mainType;

        GUI(int xSize, int ySize, ResourceList guiResourceList, IronShulkerBoxType mainType)
        {
            this.xSize = xSize;
            this.ySize = ySize;
            this.guiResourceList = guiResourceList;
            this.mainType = mainType;
        }

        protected Container makeContainer(IInventory player, IInventory shulker)
        {
            return new ContainerIronShulkerBox(player, shulker, this.mainType, this.xSize, this.ySize);
        }

        public static GUIShulkerChest buildGUI(IronShulkerBoxType type, IInventory playerInventory, TileEntityIronShulkerBox shulkerInventory)
        {
            return new GUIShulkerChest(values()[shulkerInventory.getType().ordinal()], playerInventory, shulkerInventory);
        }
    }

    private GUI type;

    private GUIShulkerChest(GUI type, IInventory player, IInventory shulker)
    {
        super(type.makeContainer(player, shulker));

        this.type = type;
        this.xSize = type.xSize;
        this.ySize = type.ySize;
        this.allowUserInput = false;
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(this.type.guiResourceList.location);

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
