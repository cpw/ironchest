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

import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.IInventory;

import org.lwjgl.opengl.GL11;

import cpw.mods.ironchest.mod_IronChest;
import cpw.mods.ironchest.ContainerIronChestBase;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;

public class GUIChest extends GuiContainer {
	public enum GUI {
		IRON(184,202,184,202,"/cpw/mods/ironchest/sprites/ironcontainer.png","/cpw/mods/ironchest/sprites/ironcontainer.png",IronChestType.IRON),
		GOLD(184,256,238,220,"/cpw/mods/ironchest/sprites/goldcontainer.png","/cpw/mods/ironchest/sprites/goldcontainer-short.png",IronChestType.GOLD),
		DIAMOND(238,256,256,238,"/cpw/mods/ironchest/sprites/diamondcontainer.png","/cpw/mods/ironchest/sprites/diamondcontainer-short.png",IronChestType.DIAMOND),
		COPPER(184,184,184,184,"/cpw/mods/ironchest/sprites/coppercontainer.png","/cpw/mods/ironchest/sprites/coppercontainer.png",IronChestType.COPPER),
		SILVER(184,238,238,202,"/cpw/mods/ironchest/sprites/silvercontainer.png","/cpw/mods/ironchest/sprites/silvercontainer-short.png",IronChestType.SILVER),
		CRYSTAL(238,256,256,238,"/cpw/mods/ironchest/sprites/diamondcontainer.png","/cpw/mods/ironchest/sprites/diamondcontainer-short.png",IronChestType.CRYSTAL);

		private int normalXSize;
		private int normalYSize;
		private String normalGuiTexture;
		private int shortXSize;
		private int shortYSize;
		private String shortGuiTexture;
		private IronChestType mainType;

		private GUI(int normalXSize, int normalYSize, int shortXSize, int shortYSize, String normalGuiTexture, String shortGuiTexture, IronChestType mainType) {
			this.normalXSize =normalXSize;
			this.normalYSize =normalYSize;
			this.normalGuiTexture =normalGuiTexture;
			this.shortXSize =shortXSize;
			this.shortYSize =shortYSize;
			this.shortGuiTexture =shortGuiTexture;
			this.mainType=mainType;
		}
		
		protected Container makeContainer(IInventory player, IInventory chest) {
			return new ContainerIronChestBase(player,chest, mainType, getXSize(), getYSize());
		}
		
		public static GUIChest buildGUI(IronChestType type, IInventory playerInventory, TileEntityIronChest chestInventory) {
			return new GUIChest(values()[chestInventory.getType().ordinal()],playerInventory,chestInventory);
		}

		public int getXSize() {
			return mod_IronChest.SHORT_CHESTS ? shortXSize : normalXSize;
		}

		public int getYSize() {
			return mod_IronChest.SHORT_CHESTS ? shortYSize : normalYSize;
		}

		public String getGuiTexture() {
			return mod_IronChest.SHORT_CHESTS ? shortGuiTexture : normalGuiTexture;
		}
	}

	public int getRowLength() {
		return type.mainType.getRowLength();
	}
	private GUI type;

	private GUIChest(GUI type, IInventory player, IInventory chest) {
		super(type.makeContainer(player,chest));
		this.type=type;
		this.xSize=type.getXSize();
		this.ySize=type.getYSize();
		this.allowUserInput=false;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int tex = mc.renderEngine.getTexture(type.getGuiTexture());
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(tex);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
