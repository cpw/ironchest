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
package cpw.mods.ironchest;

import java.util.Arrays;
import java.util.Comparator;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;

public class TileEntityIronChest extends TileEntity implements IInventory {
  private int ticksSinceSync;
  public float prevLidAngle;
  public float lidAngle;
  private int numUsingPlayers;
  private IronChestType type;
  public ItemStack[] chestContents;
  private ItemStack[] topStacks;
  private byte facing;
  private boolean inventoryTouched;
  private boolean hadStuff;

  public TileEntityIronChest() {
    this(IronChestType.IRON);
  }

  protected TileEntityIronChest(IronChestType type) {
    super();
    this.type = type;
    this.chestContents = new ItemStack[getSizeInventory()];
    this.topStacks = new ItemStack[8];
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
    inventoryTouched = true;
    return chestContents[i];
  }

  @Override
  public void onInventoryChanged() {
    super.onInventoryChanged();
    sortTopStacks();
  }

  protected void sortTopStacks() {
    if (!type.isTransparent() || (worldObj != null && worldObj.isRemote)) {
      return;
    }
    ItemStack[] tempCopy = new ItemStack[getSizeInventory()];
    boolean hasStuff = false;
    int compressedIdx = 0;
    mainLoop: for (int i = 0; i < getSizeInventory(); i++) {
      if (chestContents[i] != null) {
        for (int j = 0; j < compressedIdx; j++) {
          if (tempCopy[j].isItemEqual(chestContents[i])) {
            tempCopy[j].stackSize += chestContents[i].stackSize;
            continue mainLoop;
          }
        }
        tempCopy[compressedIdx++] = chestContents[i].copy();
        hasStuff = true;
      }
    }
    if (!hasStuff && hadStuff) {
      hadStuff = false;
      for (int i = 0; i < topStacks.length; i++) {
        topStacks[i] = null;
      }
      if (worldObj!=null) {
        worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
      }
      return;
    }
    hadStuff = true;
    Arrays.sort(tempCopy, new Comparator<ItemStack>() {
      @Override
      public int compare(ItemStack o1, ItemStack o2) {
        if (o1 == null) {
          return 1;
        } else if (o2 == null) {
          return -1;
        } else {
          return o2.stackSize - o1.stackSize;
        }
      }
    });
    int p = 0;
    for (int i = 0; i < tempCopy.length; i++) {
      if (tempCopy[i] != null && tempCopy[i].stackSize > 0) {
        topStacks[p++] = tempCopy[i];
        if (p == topStacks.length) {
          break;
        }
      }
    }
    for (int i = p; i < topStacks.length; i++) {
      topStacks[i] = null;
    }
    if (worldObj!=null) {
      worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
    }
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
      NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
      int j = nbttagcompound1.getByte("Slot") & 0xff;
      if (j >= 0 && j < chestContents.length)
      {
        chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
      }
    }
    facing = nbttagcompound.getByte("facing");
    sortTopStacks();
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
        nbttagcompound1.setByte("Slot", (byte) i);
        chestContents[i].writeToNBT(nbttagcompound1);
        nbttaglist.appendTag(nbttagcompound1);
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
    if (worldObj == null) {
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
      worldObj.addBlockEvent(xCoord, yCoord, zCoord, IronChest.ironChestBlock.blockID, 3, ((numUsingPlayers << 3) & 0xF8) | (facing & 0x7));
      if (inventoryTouched) {
        inventoryTouched = false;
        sortTopStacks();
      }
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
  public void receiveClientEvent(int i, int j)
  {
    if (i == 1)
    {
      numUsingPlayers = j;
    } else if (i == 2) {
      facing = (byte) j;
    } else if (i == 3) {
      facing = (byte) (j & 0x7);
      numUsingPlayers = (j & 0xF8) >> 3;
    }
  }

  @Override
  public void openChest() {
    if (worldObj == null)
      return;
    numUsingPlayers++;
    worldObj.addBlockEvent(xCoord, yCoord, zCoord, IronChest.ironChestBlock.blockID, 1, numUsingPlayers);
  }

  @Override
  public void closeChest() {
    if (worldObj == null)
      return;
    numUsingPlayers--;
    worldObj.addBlockEvent(xCoord, yCoord, zCoord, IronChest.ironChestBlock.blockID, 1, numUsingPlayers);
  }

  public void setFacing(byte chestFacing) {
    this.facing = chestFacing;
  }

  public TileEntityIronChest applyUpgradeItem(ItemChestChanger itemChestChanger) {
    if (numUsingPlayers > 0) {
      return null;
    }
    if (!itemChestChanger.getType().canUpgrade(this.getType())) {
      return null;
    }
    TileEntityIronChest newEntity = IronChestType.makeEntity(itemChestChanger.getTargetChestOrdinal(getType().ordinal()));
    int newSize = newEntity.chestContents.length;
    System.arraycopy(chestContents, 0, newEntity.chestContents, 0, Math.min(newSize, chestContents.length));
    BlockIronChest block = IronChest.ironChestBlock;
    block.dropContent(newSize, this, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    newEntity.setFacing(facing);
    newEntity.sortTopStacks();
    return newEntity;
  }

  public ItemStack[] getTopItemStacks() {
    return topStacks;
  }

  public TileEntityIronChest updateFromMetadata(int l) {
    if (worldObj != null && worldObj.isRemote) {
      if (l != type.ordinal()) {
        worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, IronChestType.makeEntity(l));
        return (TileEntityIronChest) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
      }
    }
    return this;
  }

  @Override
  public Packet getDescriptionPacket() {
    return PacketHandler.getPacket(this);
  }

  public void handlePacketData(int typeData, int[] intData) {
    TileEntityIronChest chest = this;
    if (this.type.ordinal() != typeData) {
      chest = updateFromMetadata(typeData);
    }
    if (IronChestType.values()[typeData].isTransparent() && intData != null) {
      int pos = 0;
      if (intData.length < chest.topStacks.length * 3) {
        return;
      }
      for (int i = 0; i < chest.topStacks.length; i++) {
        if (intData[pos + 2] != 0) {
          ItemStack is = new ItemStack(intData[pos], intData[pos + 2], intData[pos + 1]);
          chest.topStacks[i] = is;
        } else {
          chest.topStacks[i] = null;
        }
        pos += 3;
      }
    }
  }

  public int[] buildIntDataList() {
    if (type.isTransparent()) {
      int[] sortList = new int[topStacks.length * 3];
      int pos = 0;
      for (ItemStack is : topStacks) {
        if (is != null) {
          sortList[pos++] = is.itemID;
          sortList[pos++] = is.getItemDamage();
          sortList[pos++] = is.stackSize;
        } else {
          sortList[pos++] = 0;
          sortList[pos++] = 0;
          sortList[pos++] = 0;
        }
      }
      return sortList;
    }
    return null;
  }

  public ItemStack getStackInSlotOnClosing(int par1)
  {
    if (this.chestContents[par1] != null)
    {
      ItemStack var2 = this.chestContents[par1];
      this.chestContents[par1] = null;
      return var2;
    }
    else
    {
      return null;
    }
  }
  public void setMaxStackSize(int size) {

  }
}
