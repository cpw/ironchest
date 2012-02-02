package cpw.mods.ironchest;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_IronChest;

public class TileEntityIronChest extends TileEntity implements IInventory {
	private int ticksSinceSync;
	public float prevLidAngle;
	public float lidAngle;
	private int numUsingPlayers;
	private IronChestType type;
	public ItemStack[] chestContents;
	private byte facing;

	public TileEntityIronChest() {
		this(IronChestType.IRON);
	}
	
	protected TileEntityIronChest(IronChestType type) {
		super();
		this.type=type;
		this.chestContents=new ItemStack[getSizeInventory()];
	}
	
	public ItemStack[] getContents() {
		return chestContents;
	}
	
	@Override
	public int getSizeInventory() {
		return type.size;
	}

	public byte getFacing() {
		return this.facing;
	}
	@Override
	public String getInvName() {
		return type.name();
	}

	public IronChestType getType() {
		return type;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
        return chestContents[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
        if (chestContents[i] != null)
        {
            if (chestContents[i].stackSize <= j)
            {
                ItemStack itemstack = chestContents[i];
                chestContents[i] = null;
                onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = chestContents[i].splitStack(j);
            if (chestContents[i].stackSize == 0)
            {
                chestContents[i] = null;
            }
            onInventoryChanged();
            return itemstack1;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
        chestContents[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();
	}

    @Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        chestContents = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < chestContents.length)
            {
                chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        facing=nbttagcompound.getByte("facing");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < chestContents.length; i++)
        {
            if (chestContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
        nbttagcompound.setByte("facing", facing);
    }

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj==null) {
			return true;
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		}
		return entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		// Resynchronize clients with the server state
		if ((++ticksSinceSync % 20) * 4 == 0) {
			worldObj.playNoteAt(xCoord, yCoord, zCoord, 3, ( ( numUsingPlayers<<3 ) & 0xF8 ) | (facing & 0x7));
		}
		prevLidAngle = lidAngle;
		float f = 0.1F;
		if (numUsingPlayers > 0 && lidAngle == 0.0F) {
			double d = (double) xCoord + 0.5D;
			double d1 = (double) zCoord + 0.5D;
			worldObj.playSoundEffect(d, (double) yCoord + 0.5D, d1, "random.chestopen", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}
		if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F) {
			float f1 = lidAngle;
			if (numUsingPlayers > 0) {
				lidAngle += f;
			} else {
				lidAngle -= f;
			}
			if (lidAngle > 1.0F) {
				lidAngle = 1.0F;
			}
			float f2 = 0.5F;
			if (lidAngle < f2 && f1 >= f2) {
				double d2 = (double) xCoord + 0.5D;
				double d3 = (double) zCoord + 0.5D;
				worldObj.playSoundEffect(d2, (double) yCoord + 0.5D, d3, "random.chestclosed", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
			if (lidAngle < 0.0F) {
				lidAngle = 0.0F;
			}
		}
	}

	@Override
    public void onTileEntityPowered(int i, int j)
    {
        if (i == 1)
        {
            numUsingPlayers = j;
        } else if (i == 2) {
        	facing = (byte)j;
        } else if (i == 3) {
        	facing = (byte)(j & 0x7);
        	numUsingPlayers = (j & 0xF8 )>> 3;
        }
    }

	@Override
	public void openChest() {
		if (worldObj==null) return;
        numUsingPlayers++;
        worldObj.playNoteAt(xCoord, yCoord, zCoord, 1, numUsingPlayers);
	}

	@Override
	public void closeChest() {
		if (worldObj==null) return;
        numUsingPlayers--;
        worldObj.playNoteAt(xCoord, yCoord, zCoord, 1, numUsingPlayers);
	}

	public void setFacing(byte chestFacing) {
		this.facing=chestFacing;
	}

	public TileEntityIronChest applyUpgradeItem(ItemChestChanger itemChestChanger) {
		if (numUsingPlayers>0) {
			return null;
		}
		if (!itemChestChanger.getType().canUpgrade(this.getType())) {
			return null;
		}
		TileEntityIronChest newEntity=IronChestType.makeEntity(itemChestChanger.getTargetChestOrdinal(getType().ordinal()));
		int newSize=newEntity.chestContents.length;
		System.arraycopy(chestContents, 0, newEntity.chestContents, 0, Math.min(newSize,chestContents.length));
		BlockIronChest block=mod_IronChest.ironChestBlock;
		block.dropContent(newSize,this,this.worldObj);
		newEntity.setFacing(facing);
		return newEntity;
	}

}
