package cpw.mods.ironchest;

import java.util.Random;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_IronChest;
import net.minecraft.src.forge.ITextureProvider;

public class BlockIronChest extends BlockContainer implements ITextureProvider {

	private Random random;
	public BlockIronChest(int id) {
		super(id, Material.iron);
		setBlockName("IronChest");
		setHardness(3.0F);
		random=new Random();
	}

	@Override
	public TileEntity getBlockEntity() {
		return null;
	}

	@Override
	public String getTextureFile() {
		return "cpw/mods/ironchest/sprites/block_textures.png";
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

	public void onBlockRemoval(World world, int i, int j, int k)
	{
	    TileEntityIronChest tileentitychest = (TileEntityIronChest)world.getBlockTileEntity(i, j, k);
	    if (tileentitychest != null)
	    {
	    	dropContent(0, tileentitychest, world);
	    }
	    super.onBlockRemoval(world, i, j, k);
	}

	public void dropContent(int newSize, TileEntityIronChest chest, World world) {
        for (int l = newSize; l < chest.getSizeInventory(); l++)
        {
            ItemStack itemstack = chest.getStackInSlot(l);
            if (itemstack == null)
            {
                continue;
            }
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            while (itemstack.stackSize > 0)
            {
                int i1 = random.nextInt(21) + 10;
                if (i1 > itemstack.stackSize)
                {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float)chest.xCoord + f, (float)chest.yCoord + (newSize>0 ? 1 : 0) + f1, (float)chest.zCoord + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float)random.nextGaussian() * f3;
                entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)random.nextGaussian() * f3;
                if (itemstack.hasTagCompound())
                {
                	mod_IronChest.proxy.applyExtraDataToDrops(entityitem, (NBTTagCompound)itemstack.getTagCompound().cloneTag());
                }
                world.spawnEntityInWorld(entityitem);
            }
        }
	}
}
