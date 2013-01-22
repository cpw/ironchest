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

import org.lwjgl.opengl.GL11;

import cpw.mods.ironchest.ContainerIronChestBase;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;

public class GUIChest extends GuiContainer {
    public enum GUI {
        IRON(184, 202, "/cpw/mods/ironchest/sprites/ironcontainer.png", IronChestType.IRON),
        GOLD(184, 256, "/cpw/mods/ironchest/sprites/goldcontainer.png", IronChestType.GOLD),
        DIAMOND(238, 256, "/cpw/mods/ironchest/sprites/diamondcontainer.png", IronChestType.DIAMOND),
        COPPER(184, 184, "/cpw/mods/ironchest/sprites/coppercontainer.png", IronChestType.COPPER),
        SILVER(184, 238, "/cpw/mods/ironchest/sprites/silvercontainer.png", IronChestType.SILVER),
        CRYSTAL(238, 256, "/cpw/mods/ironchest/sprites/diamondcontainer.png", IronChestType.CRYSTAL),
        OBSIDIAN(238, 256, "/cpw/mods/ironchest/sprites/diamondcontainer.png", IronChestType.OBSIDIAN);

        private int xSize;
        private int ySize;
        private String guiTexture;
        private IronChestType mainType;

        private GUI(int xSize, int ySize, String guiTexture, IronChestType mainType)
        {
            this.xSize = xSize;
            this.ySize = ySize;
            this.guiTexture = guiTexture;
            this.mainType = mainType;
        }

        protected Container makeContainer(IInventory player, IInventory chest)
        {
            return new ContainerIronChestBase(player, chest, mainType, xSize, ySize);
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
        int tex = mc.renderEngine.getTexture(type.guiTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(tex);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
