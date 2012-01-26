package cpw.mods.ironchest;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;

public class TileEntityIronChest extends TileEntity implements IInventory {
	public enum Type {
		IRON(54,"IronChest","ironchest.png",0),
		GOLD(81,"GoldChest","goldchest.png",1),
		DIAMOND(108,"DiamondChest","diamondchest.png",2);
		private int size;
		private String friendlyName;
		private String modelTexture;
		private int textureRow;
		
		Type(int size, String friendlyName, String modelTexture, int textureRow) {
			this.size=size;
			this.friendlyName=friendlyName;
			this.modelTexture=modelTexture;
			this.textureRow=textureRow;
		}
		
		public String getModelTexture() {
			return modelTexture;
		}
		
		public int getTextureRow() {
			return textureRow;
		}
	}
	private int ticksSinceSync;
	public float prevLidAngle;
	public float lidAngle;
	private int numUsingPlayers;
	private Type type;
	public ItemStack[] chestContents;
	private byte facing;

	public TileEntityIronChest(Type type, byte facing) {
		this.type=type;
		this.facing=facing;
		this.chestContents=new ItemStack[getSizeInventory()];
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
		return type.friendlyName;
	}

	public Type getType() {
		return type;
	}
	@Override
	public ItemStack getStackInSlot(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub

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
    }

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
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
			worldObj.playNoteAt(xCoord, yCoord, zCoord, 1, numUsingPlayers);
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
        }
    }

	@Override
	public void openChest() {
        numUsingPlayers++;
        worldObj.playNoteAt(xCoord, yCoord, zCoord, 1, numUsingPlayers);
	}

	@Override
	public void closeChest() {
        numUsingPlayers--;
        worldObj.playNoteAt(xCoord, yCoord, zCoord, 1, numUsingPlayers);
	}

	protected static TileEntity makeEntity(int metadata) {
		//Compatibility			
		int chesttype=metadata;
		int facing=0;

		if (metadata>2) {
			chesttype=metadata<<2;
			facing=metadata&3;
		}
		return new TileEntityIronChest(Type.values()[chesttype],(byte)facing);
	}
	public void setFacing(byte chestFacing) {
		this.facing=chestFacing;
	}

}
