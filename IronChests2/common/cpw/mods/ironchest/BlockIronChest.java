package cpw.mods.ironchest;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.ITextureProvider;

public class BlockIronChest extends BlockContainer implements ITextureProvider {

	public BlockIronChest(int id) {
		super(id, Material.iron);
		setHardness(3.0F);
	}

	@Override
	public TileEntity getBlockEntity() {
		return null;
	}

	@Override
	public String getTextureFile() {
		return "ic2/sprites/ironchest_block_tex.png";
	}

	@Override
	public TileEntity getBlockEntity(int metadata) {
		return TileEntityIronChest.makeEntity(metadata);
	}

	public int getBlockTexture(IBlockAccess worldAccess, int i, int j, int k, int l) {
		TileEntity te = worldAccess.getBlockTileEntity(i, j, k);
		if (te != null && te instanceof TileEntityIronChest) {
			TileEntityIronChest icte=(TileEntityIronChest) te;
			if (l==0 || l==1) {									// Top and Bottom
				return icte.getType().getTextureRow()*16;
			} else if (l==getTextureFace(icte.getFacing())) {	// Front
				return icte.getType().getTextureRow()*16+1;
			} else { 											// Back and Sides
				return icte.getType().getTextureRow()*16+2;
			}
		}
		return 0;
	}

	public byte getTextureFace(byte facing) {
		switch (facing) {
		case 0:
			return 3;
		case 1:
			return 4;
		case 2:
			return 2;
		case 3:
			return 5;
		default:
			return 0;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		super.onBlockPlacedBy(world, i, j, k, entityliving);
		byte chestFacing = 0;
		int facing = MathHelper.floor_double((double) ((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		if (facing == 0) {
			chestFacing = 2;
		}
		if (facing == 1) {
			chestFacing = 5;
		}
		if (facing == 2) {
			chestFacing = 3;
		}
		if (facing == 3) {
			chestFacing = 4;
		}
		TileEntity te = world.getBlockTileEntity(i, j, k);
		if (te != null && te instanceof TileEntityIronChest) {
			((TileEntityIronChest) te).setFacing(chestFacing);
		}
	}
}
