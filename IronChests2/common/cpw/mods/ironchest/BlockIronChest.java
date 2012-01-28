package cpw.mods.ironchest;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_IronChest;
import net.minecraft.src.forge.ITextureProvider;

public class BlockIronChest extends BlockContainer implements ITextureProvider {

	public BlockIronChest(int id) {
		super(id, Material.iron);
		setBlockName("IronChest");
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
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	@Override
	public int getRenderType() {
		return 22;
	}
	@Override
	public TileEntity getBlockEntity(int metadata) {
		return IronChestType.makeEntity(metadata);
	}

	public int getBlockTexture(IBlockAccess worldAccess, int i, int j, int k, int l) {
		TileEntity te = worldAccess.getBlockTileEntity(i, j, k);
		if (te != null && te instanceof TileEntityIronChest) {
			TileEntityIronChest icte=(TileEntityIronChest) te;
			if (l==0 || l==1) {									// Top and Bottom
				return icte.getType().getTextureRow()*16+1;
			} else if (l==icte.getFacing()) {	// Front
				return icte.getType().getTextureRow()*16+2;
			} else { 											// Back and Sides
				return icte.getType().getTextureRow()*16;
			}
		}
		return 0;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		IronChestType typ=IronChestType.values()[j];
		switch (i) {
		case 0:
		case 1:
			return typ.getTextureRow()*16+1;
		case 3:
			return typ.getTextureRow()*16+2;
		default:
			return typ.getTextureRow()*16;
		}
	}
	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer player) {
        TileEntity te = world.getBlockTileEntity(i, j, k);
        
        if(te == null || !(te instanceof TileEntityIronChest))
        {
            return true;
        }

        if(world.isBlockSolidOnSide(i, j + 1, k, 0))
        {
            return true;
        }
        
        if (world.isRemote) {
        	return true;
        }
        
        mod_IronChest.openGUI(player, (TileEntityIronChest)te);
        return true;
	}
	
	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
        world.markBlockNeedsUpdate(i, j, k);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
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
		System.out.printf("Facing %d %d\n", facing,chestFacing);
		TileEntity te = world.getBlockTileEntity(i, j, k);
		if (te != null && te instanceof TileEntityIronChest) {
			((TileEntityIronChest) te).setFacing(chestFacing);
			world.markBlockNeedsUpdate(i, j, k);
		}
	}
	@Override
	protected int damageDropped(int i) {
		return i;
	}
}
