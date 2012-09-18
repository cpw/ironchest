/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 * cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.client;

import static org.lwjgl.opengl.GL11.GL_COMPILE_AND_EXECUTE;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModelChest;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.MappableItemStackWrapper;
import cpw.mods.ironchest.TileEntityIronChest;

public class TileEntityIronChestRenderer extends TileEntitySpecialRenderer {
	private static Map<MappableItemStackWrapper, Integer> renderList = new HashMap<MappableItemStackWrapper, Integer>();

	private Random random;

	private RenderBlocks renderBlocks;

	private static float[][] shifts = { { 0.3F, 0.45F, 0.3F }, { 0.7F, 0.45F, 0.3F }, { 0.3F, 0.45F, 0.7F }, { 0.7F, 0.45F, 0.7F }, { 0.3F, 0.1F, 0.3F },
			{ 0.7F, 0.1F, 0.3F }, { 0.3F, 0.1F, 0.7F }, { 0.7F, 0.1F, 0.7F }, { 0.5F, 0.32F, 0.5F }, };

	public TileEntityIronChestRenderer() {
		model = new ModelChest();
		random = new Random();
		renderBlocks = new RenderBlocks();
	}

	private void overrideTexture(Object obj) {
		if (obj instanceof Item) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture(((Item) obj).getTextureFile()));
		} else if (obj instanceof Block) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture(((Block) obj).getTextureFile()));
		}
	}

	public void render(TileEntityIronChest tile, double x, double y, double z, float partialTick) {
		if (tile == null) {
			return;
		}
		int facing = 3;
		IronChestType type = tile.getType();
		if (tile != null && tile.getWorldObj() != null) {
			facing = tile.getFacing();
			type = tile.getType();
			int typ = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);
			type = IronChestType.values()[typ];
		}
		bindTextureByName(type.getModelTexture());
		glPushMatrix();
		glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
		glScalef(1.0F, -1F, -1F);
		glTranslatef(0.5F, 0.5F, 0.5F);
		int k = 0;
		if (facing == 2) {
			k = 180;
		}
		if (facing == 3) {
			k = 0;
		}
		if (facing == 4) {
			k = 90;
		}
		if (facing == 5) {
			k = -90;
		}
		glRotatef(k, 0.0F, 1.0F, 0.0F);
		glTranslatef(-0.5F, -0.5F, -0.5F);
		float lidangle = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * partialTick;
		lidangle = 1.0F - lidangle;
		lidangle = 1.0F - lidangle * lidangle * lidangle;
		model.chestLid.rotateAngleX = -((lidangle * 3.141593F) / 2.0F);
		// Render the chest itself
		model.renderAll();
		glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		glPopMatrix();
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (type.isTransparent()) {
			random.setSeed(254L);
			float shiftX;
			float shiftY;
			float shiftZ;
			int shift = 0;
			float spread = 0.1F;
			float blockScale = 0.15F;
			float timeD = (float) (360.0 * (double) (System.currentTimeMillis() & 0x3FFFL) / (double) 0x3FFFL);
			if (tile.getTopItemStacks()[1] == null) {
				shift = 8;
				blockScale = 0.2F;
				spread = 0.22F;
			}
			glPushMatrix();
			glDisable(2896 /* GL_LIGHTING */);
			glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
			glTranslatef((float) x, (float) y, (float) z);
			EntityItem customitem = new EntityItem(tileEntityRenderer.worldObj);
			for (ItemStack item : tile.getTopItemStacks()) {
				if (shift > shifts.length) {
					break;
				}
				if (item == null) {
					shift++;
					continue;
				}
				shiftX = shifts[shift][0];
				shiftY = shifts[shift][1];
				shiftZ = shifts[shift][2];
				shift++;
				float localScale = blockScale;
				if (item.itemID < Block.blocksList.length && Block.blocksList[item.itemID] != null && Block.blocksList[item.itemID].blockID != 0) {
					int j = Block.blocksList[item.itemID].getRenderType();
					if (j == 1 || j == 19 || j == 12 || j == 2) {
						localScale = 2 * blockScale;
					}
				}
				glPushMatrix();
				glTranslatef(shiftX, shiftY, shiftZ);
				glScalef(localScale, localScale, localScale);
				for (int miniBlocks = 0; miniBlocks < (item.stackSize / 32) + 1; miniBlocks++) {
					glPushMatrix();
					glRotatef(timeD, 0.0F, 1.0F, 0.0F);
					if (miniBlocks > 0) {
						float minishiftX = ((random.nextFloat() * 2.0F - 1.0F) * spread) / localScale;
						float minishiftY = ((random.nextFloat() * 2.0F - 1.0F) * spread * 0.9F) / localScale;
						float minishiftZ = ((random.nextFloat() * 2.0F - 1.0F) * spread) / localScale;
						glTranslatef(minishiftX, minishiftY, minishiftZ);
					}
					MappableItemStackWrapper mis = new MappableItemStackWrapper(item);
					if (!IronChest.CACHE_RENDER || !renderList.containsKey(mis)) { // Added support for using only old system.
						IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(item, IItemRenderer.ItemRenderType.ENTITY);
						if (customRenderer != null) {
							customitem.item = item;
							bindTextureByName("/terrain.png");
							overrideTexture(item.getItem());
							customRenderer.renderItem(IItemRenderer.ItemRenderType.ENTITY, item, renderBlocks, customitem);
						} else if (item.itemID < Block.blocksList.length && Block.blocksList[item.itemID] != null && Block.blocksList[item.itemID].blockID != 0 && RenderBlocks.renderItemIn3d(Block.blocksList[item.itemID].getRenderType())) {
							bindTextureByName("/terrain.png");
							overrideTexture(Block.blocksList[item.itemID]);
							renderBlocks.renderBlockAsItem(Block.blocksList[item.itemID], item.getItemDamage(), 1.0F);
						} else {
							int render = 0;
							if (IronChest.CACHE_RENDER) {
								render = glGenLists(1);
								if (render != 0)
								{
									renderList.put(mis, render);
									glNewList(render, GL_COMPILE_AND_EXECUTE);
								}
							}
							int i = item.getIconIndex();
							if (item.itemID >= Block.blocksList.length || Block.blocksList[item.itemID] == null || Block.blocksList[item.itemID].blockID == 0) {
								bindTextureByName("/gui/items.png");
								overrideTexture(Item.itemsList[item.itemID]);
							} else {
								bindTextureByName("/terrain.png");
								overrideTexture(Block.blocksList[item.itemID]);
							}
							Tessellator tessellator = Tessellator.instance;
							float f5 = (float) ((i % 16) * 16 + 0) / 256F;
							float f8 = (float) ((i % 16) * 16 + 16) / 256F;
							float f10 = (float) ((i / 16) * 16 + 0) / 256F;
							float f12 = (float) ((i / 16) * 16 + 16) / 256F;
							float f13 = 1.0F;
							float f14 = 0.5F;
							float f15 = 0.25F;
							tessellator.startDrawingQuads();
							tessellator.setNormal(0.0F, 1.0F, 0.0F);
							tessellator.addVertexWithUV(0.0F - f14, 0.0F - f15, 0.0D, f5, f12);
							tessellator.addVertexWithUV(f13 - f14, 0.0F - f15, 0.0D, f8, f12);
							tessellator.addVertexWithUV(f13 - f14, 1.0F - f15, 0.0D, f8, f10);
							tessellator.addVertexWithUV(0.0F - f14, 1.0F - f15, 0.0D, f5, f10);
							tessellator.draw();
							glScalef(-1.0F, 1.0F, 1.0F);
							tessellator.startDrawingQuads();
							tessellator.setNormal(0.0F, 1.0F, 0.0F);
							tessellator.addVertexWithUV(0.0F - f14, 0.0F - f15, 0.0D, f5, f12);
							tessellator.addVertexWithUV(f13 - f14, 0.0F - f15, 0.0D, f8, f12);
							tessellator.addVertexWithUV(f13 - f14, 1.0F - f15, 0.0D, f8, f10);
							tessellator.addVertexWithUV(0.0F - f14, 1.0F - f15, 0.0D, f5, f10);
							tessellator.draw();
							if (IronChest.CACHE_RENDER && render != 0) {
								glEndList();
							}
						}
					} else {
						Integer integer = renderList.get(mis);
						if (integer != null) { // Added null check for auto-unboxing JUST in case.
							glCallList(integer.intValue());
						}
					}
					glPopMatrix();
				}
				glPopMatrix();
			}
			glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
			glEnable(2896 /* GL_LIGHTING */);
			glPopMatrix();
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTick) {
		render((TileEntityIronChest) tileentity, x, y, z, partialTick);
	}

	private ModelChest model;
}
