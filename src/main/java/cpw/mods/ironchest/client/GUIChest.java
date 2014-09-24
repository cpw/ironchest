/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.ironchest.ContainerIronChest;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;

public class GUIChest extends GuiContainer {
    public enum ResourceList {
        IRON(new ResourceLocation("ironchest", "textures/gui/ironcontainer.png")),
        COPPER(new ResourceLocation("ironchest", "textures/gui/coppercontainer.png")),
        SILVER(new ResourceLocation("ironchest", "textures/gui/silvercontainer.png")),
        GOLD(new ResourceLocation("ironchest", "textures/gui/goldcontainer.png")),
        DIAMOND(new ResourceLocation("ironchest", "textures/gui/diamondcontainer.png")),
        DIRT(new ResourceLocation("ironchest", "textures/gui/dirtcontainer.png"));
        public final ResourceLocation location;
        private ResourceList(ResourceLocation loc) {
            this.location = loc;
        }
    }
    public enum GUI {
        IRON(184, 202, ResourceList.IRON, IronChestType.IRON),
        GOLD(184, 256, ResourceList.GOLD, IronChestType.GOLD),
        DIAMOND(238, 256, ResourceList.DIAMOND, IronChestType.DIAMOND),
        COPPER(184, 184, ResourceList.COPPER, IronChestType.COPPER),
        SILVER(184, 238, ResourceList.SILVER, IronChestType.SILVER),
        CRYSTAL(238, 256, ResourceList.DIAMOND, IronChestType.CRYSTAL),
        OBSIDIAN(238, 256, ResourceList.DIAMOND, IronChestType.OBSIDIAN),
        DIRTCHEST9000(184, 184, ResourceList.DIRT, IronChestType.DIRTCHEST9000);

        private int xSize;
        private int ySize;
        private ResourceList guiResourceList;
        private IronChestType mainType;

        private GUI(int xSize, int ySize, ResourceList guiResourceList, IronChestType mainType)
        {
            this.xSize = xSize;
            this.ySize = ySize;
            this.guiResourceList = guiResourceList;
            this.mainType = mainType;

        }

        protected Container makeContainer(IInventory player, IInventory chest)
        {
            return new ContainerIronChest(player, chest, mainType, xSize, ySize);
        }

        public static GUIChest buildGUI(IronChestType type, IInventory playerInventory, TileEntityIronChest chestInventory)
        {
            return new GUIChest(values()[chestInventory.getType().ordinal()], playerInventory, chestInventory);
        }
    }

    public int getRowLength()
    {
        return type.mainType.getRowLength();
    }

    private GUI type;

    private GUIChest(GUI type, IInventory player, IInventory chest)
    {
        super(type.makeContainer(player, chest));
        this.type = type;
        this.xSize = type.xSize;
        this.ySize = type.ySize;
        this.allowUserInput = false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // new "bind tex"
        this.mc.getTextureManager().bindTexture(type.guiResourceList.location);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
