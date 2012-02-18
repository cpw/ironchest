package cpw.mods.ironchest.client;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModelChest;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.forge.ForgeHooksClient;
import net.minecraft.src.forge.ICustomItemRenderer;
import net.minecraft.src.forge.MinecraftForgeClient;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;

public class TileEntityIronChestRenderer extends TileEntitySpecialRenderer {
	private Random random;
	private RenderBlocks renderBlocks;

	private static float[][] shifts = { { 0.3F, 0.45F, 0.3F }, { 0.7F, 0.45F, 0.3F }, { 0.3F, 0.45F, 0.7F }, { 0.7F, 0.45F, 0.7F },
			{ 0.3F, 0.1F, 0.3F }, { 0.7F, 0.1F, 0.3F }, { 0.3F, 0.1F, 0.7F }, { 0.7F, 0.1F, 0.7F }, { 0.5F, 0.32F, 0.5F }, };

	public TileEntityIronChestRenderer() {
		model = new ModelChest();
		random = new Random();
		renderBlocks = new RenderBlocks();
	}

	public void render(TileEntityIronChest tile, double x, double y, double z, float partialTick) {
		if (tile==null) {
			return;
		}
		int facing = 3;
		IronChestType type=tile.getType();
		if (tile != null && tile.worldObj != null) {
			facing = tile.getFacing();
			type=tile.getType();
			int typ=tile.worldObj.getBlockMetadata(tile.xCoord,tile.yCoord,tile.zCoord);
			type=IronChestType.values()[typ];
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
		model.func_35402_a();
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
				ICustomItemRenderer customRenderer = MinecraftForgeClient.getCustomItemRenderer(item.itemID);
				float localScale = blockScale;
				if (item.itemID < Block.blocksList.length) {
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

					if (customRenderer != null) {
						bindTextureByName("/terrain.png");
						ForgeHooksClient.overrideTexture(item.getItem());
						ForgeHooksClient.renderCustomItem(customRenderer, renderBlocks, item.itemID, item.getItemDamage(), 1.0F);
					} else if (item.itemID < Block.blocksList.length && RenderBlocks.renderItemIn3d(Block.blocksList[item.itemID].getRenderType())) {
						bindTextureByName("/terrain.png");
						ForgeHooksClient.overrideTexture(Block.blocksList[item.itemID]);
						renderBlocks.renderBlockAsItem(Block.blocksList[item.itemID], item.getItemDamage(), 1.0F);
					} else {
						int i = item.getIconIndex();
						if (item.itemID >= Block.blocksList.length) {
							bindTextureByName("/gui/items.png");
							ForgeHooksClient.overrideTexture(Item.itemsList[item.itemID]);
						} else {
							bindTextureByName("/terrain.png");
							ForgeHooksClient.overrideTexture(Block.blocksList[item.itemID]);
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
