package cpw.mods.ironchest.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.ironchest.TileEntityIronChest;

import net.minecraft.src.ModelChest;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

public class TileEntityIronChestRenderer extends TileEntitySpecialRenderer {

	public TileEntityIronChestRenderer() {
		model = new ModelChest();
	}

	public void render(TileEntityIronChest tile, double d, double d1, double d2, float f) {
		int facing = 3;
		if (tile != null && tile.worldObj != null) {
			facing = tile.getFacing();
		}
		bindTextureByName(tile.getType().getModelTexture());

		GL11.glPushMatrix();
		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float) d, (float) d1 + 1.0F, (float) d2 + 1.0F);
		GL11.glScalef(1.0F, -1F, -1F);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
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
		GL11.glRotatef(k, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		float f1 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * f;
		f1 = 1.0F - f1;
		f1 = 1.0F - f1 * f1 * f1;
		model.chestLid.rotateAngleX = -((f1 * 3.141593F) / 2.0F);
		model.func_35402_a();
		GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
		render((TileEntityIronChest) tileentity, d, d1, d2, f);
	}

	private ModelChest model;
}
