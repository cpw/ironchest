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
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;

public class TileEntityIronChest extends TileEntityLockable implements ITickable, IInventory
{
    private int ticksSinceSync = -1;
    public float prevLidAngle;
    public float lidAngle;
    private int numUsingPlayers;
    private IronChestType type;
    public ItemStack[] chestContents;
    private ItemStack[] topStacks;
    private byte facing;
    private boolean inventoryTouched;
    private boolean hadStuff;
    private String customName;

    public TileEntityIronChest()
    {
        this(IronChestType.IRON);
    }

    protected TileEntityIronChest(IronChestType type)
    {
        super();
        this.type = type;
        this.chestContents = new ItemStack[this.getSizeInventory()];
        this.topStacks = new ItemStack[8];
    }

    public ItemStack[] getContents()
    {
        return this.chestContents;
    }

    public void setContents(ItemStack[] contents)
    {
        this.chestContents = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < contents.length; i++)
        {
            if (i < this.chestContents.length)
            {
                this.chestContents[i] = contents[i];
            }
        }
        this.inventoryTouched = true;
    }

    @Override
    public int getSizeInventory()
    {
        return this.type.size;
    }

    public int getFacing()
    {
        return this.facing;
    }

    public IronChestType getType()
    {
        return this.type;
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        this.inventoryTouched = true;
        return this.chestContents[i];
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        this.sortTopStacks();
    }

    protected void sortTopStacks()
    {
        if (!this.type.isTransparent() || (this.worldObj != null && this.worldObj.isRemote))
        {
            return;
        }
        ItemStack[] tempCopy = new ItemStack[this.getSizeInventory()];
        boolean hasStuff = false;
        int compressedIdx = 0;
        mainLoop: for (int i = 0; i < this.getSizeInventory(); i++)
        {
            if (this.chestContents[i] != null)
            {
                for (int j = 0; j < compressedIdx; j++)
                {
                    if (tempCopy[j].isItemEqual(this.chestContents[i]))
                    {
                        tempCopy[j].stackSize += this.chestContents[i].stackSize;
                        continue mainLoop;
                    }
                }
                tempCopy[compressedIdx++] = this.chestContents[i].copy();
                hasStuff = true;
            }
        }
        if (!hasStuff && this.hadStuff)
        {
            this.hadStuff = false;
            for (int i = 0; i < this.topStacks.length; i++)
            {
                this.topStacks[i] = null;
            }
            if (this.worldObj != null)
            {
                IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
                this.worldObj.notifyBlockUpdate(this.pos, iblockstate, iblockstate, 3);
            }
            return;
        }
        this.hadStuff = true;
        Arrays.sort(tempCopy, new Comparator<ItemStack>() {
            @Override
            public int compare(ItemStack o1, ItemStack o2)
            {
                if (o1 == null)
                {
                    return 1;
                }
                else if (o2 == null)
                {
                    return -1;
                }
                else
                {
                    return o2.stackSize - o1.stackSize;
                }
            }
        });
        int p = 0;
        for (ItemStack element : tempCopy)
        {
            if (element != null && element.stackSize > 0)
            {
                this.topStacks[p++] = element;
                if (p == this.topStacks.length)
                {
                    break;
                }
            }
        }
        for (int i = p; i < this.topStacks.length; i++)
        {
            this.topStacks[i] = null;
        }
        if (this.worldObj != null)
        {
            IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
            this.worldObj.notifyBlockUpdate(this.pos, iblockstate, iblockstate, 3);
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.chestContents[i] != null)
        {
            if (this.chestContents[i].stackSize <= j)
            {
                ItemStack itemstack = this.chestContents[i];
                this.chestContents[i] = null;
                this.markDirty();
                return itemstack;
            }
            ItemStack itemstack1 = this.chestContents[i].splitStack(j);
            if (this.chestContents[i].stackSize == 0)
            {
                this.chestContents[i] = null;
            }
            this.markDirty();
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.chestContents[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : this.type.name();
    }

    @Override
    public boolean hasCustomName()
    {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomName(String name)
    {
        this.customName = name;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        this.chestContents = new ItemStack[this.getSizeInventory()];

        if (nbttagcompound.hasKey("CustomName", Constants.NBT.TAG_STRING))
        {
            this.customName = nbttagcompound.getString("CustomName");
        }

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if (j >= 0 && j < this.chestContents.length)
            {
                this.chestContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        this.facing = nbttagcompound.getByte("facing");
        this.sortTopStacks();
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.chestContents.length; i++)
        {
            if (this.chestContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
        nbttagcompound.setByte("facing", this.facing);

        if (this.hasCustomName())
        {
            nbttagcompound.setString("CustomName", this.customName);
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (this.worldObj == null)
        {
            return true;
        }
        if (this.worldObj.getTileEntity(this.pos) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64D;
    }

    @Override
    public void update()
    {
        // Resynchronize clients with the server state
        if (this.worldObj != null && !this.worldObj.isRemote && this.numUsingPlayers != 0
                && (this.ticksSinceSync + this.pos.getX() + this.pos.getY() + this.pos.getZ()) % 200 == 0)
        {
            this.numUsingPlayers = 0;
            float var1 = 5.0F;
            //@formatter:off
            List<EntityPlayer> var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos.getX() - var1, this.pos.getY() - var1, this.pos.getZ() - var1, this.pos.getX() + 1 + var1, this.pos.getY() + 1 + var1, this.pos.getZ() + 1 + var1));
            //@formatter:on

            for (EntityPlayer var4 : var2)
            {
                if (var4.openContainer instanceof ContainerIronChest)
                {
                    ++this.numUsingPlayers;
                }
            }
        }

        if (this.worldObj != null && !this.worldObj.isRemote && this.ticksSinceSync < 0)
        {
            this.worldObj.addBlockEvent(this.pos, IronChest.ironChestBlock, 3, ((this.numUsingPlayers << 3) & 0xF8) | (this.facing & 0x7));
        }
        if (!this.worldObj.isRemote && this.inventoryTouched)
        {
            this.inventoryTouched = false;
            this.sortTopStacks();
        }

        this.ticksSinceSync++;
        this.prevLidAngle = this.lidAngle;
        float f = 0.1F;
        if (this.numUsingPlayers > 0 && this.lidAngle == 0.0F)
        {
            double d = this.pos.getX() + 0.5D;
            double d1 = this.pos.getZ() + 0.5D;
            //@formatter:off
            this.worldObj.playSound((EntityPlayer) null, d, this.pos.getY() + 0.5D, d1, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            //@formatter:on
        }
        if (this.numUsingPlayers == 0 && this.lidAngle > 0.0F || this.numUsingPlayers > 0 && this.lidAngle < 1.0F)
        {
            float f1 = this.lidAngle;
            if (this.numUsingPlayers > 0)
            {
                this.lidAngle += f;
            }
            else
            {
                this.lidAngle -= f;
            }
            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }
            float f2 = 0.5F;
            if (this.lidAngle < f2 && f1 >= f2)
            {
                double d2 = this.pos.getX() + 0.5D;
                double d3 = this.pos.getZ() + 0.5D;
                //@formatter:off
                this.worldObj.playSound((EntityPlayer) null, d2, this.pos.getY() + 0.5D, d3, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
                //@formatter:on
            }
            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int i, int j)
    {
        if (i == 1)
        {
            this.numUsingPlayers = j;
        }
        else if (i == 2)
        {
            this.facing = (byte) j;
        }
        else if (i == 3)
        {
            this.facing = (byte) (j & 0x7);
            this.numUsingPlayers = (j & 0xF8) >> 3;
        }
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        if (this.worldObj == null)
        {
            return;
        }
        this.numUsingPlayers++;
        this.worldObj.addBlockEvent(this.pos, IronChest.ironChestBlock, 1, this.numUsingPlayers);
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (this.worldObj == null)
        {
            return;
        }
        this.numUsingPlayers--;
        this.worldObj.addBlockEvent(this.pos, IronChest.ironChestBlock, 1, this.numUsingPlayers);
    }

    public void setFacing(byte facing2)
    {
        this.facing = facing2;
    }

    public ItemStack[] getTopItemStacks()
    {
        return this.topStacks;
    }

    public TileEntityIronChest updateFromMetadata(int l)
    {
        if (this.worldObj != null && this.worldObj.isRemote)
        {
            if (l != this.type.ordinal())
            {
                this.worldObj.setTileEntity(this.pos, IronChestType.makeEntity(l));
                return (TileEntityIronChest) this.worldObj.getTileEntity(this.pos);
            }
        }
        return this;
    }

    @Override
    public Packet<?> getDescriptionPacket()
    {

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("type", this.getType().ordinal());
        nbt.setByte("facing", this.facing);
        ItemStack[] stacks = this.buildItemStackDataList();
        if (stacks != null)
        {
            NBTTagList nbttaglist = new NBTTagList();
            for (int i = 0; i < stacks.length; i++)
            {
                if (stacks[i] != null)
                {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    nbttagcompound1.setByte("Slot", (byte) i);
                    stacks[i].writeToNBT(nbttagcompound1);
                    nbttaglist.appendTag(nbttagcompound1);
                }
            }
            nbt.setTag("stacks", nbttaglist);
        }

        return new SPacketUpdateTileEntity(this.pos, 0, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (pkt.getTileEntityType() == 0)
        {
            NBTTagCompound nbt = pkt.getNbtCompound();
            this.type = IronChestType.values()[nbt.getInteger("type")];
            this.facing = nbt.getByte("facing");

            NBTTagList tagList = nbt.getTagList("stacks", Constants.NBT.TAG_COMPOUND);
            ItemStack[] stacks = new ItemStack[this.topStacks.length];

            for (int i = 0; i < stacks.length; i++)
            {
                NBTTagCompound nbt1 = tagList.getCompoundTagAt(i);
                int j = nbt1.getByte("Slot") & 0xff;
                if (j >= 0 && j < stacks.length)
                {
                    stacks[j] = ItemStack.loadItemStackFromNBT(nbt1);
                }
            }

            if (this.type.isTransparent() && stacks != null)
            {
                int pos = 0;
                for (int i = 0; i < this.topStacks.length; i++)
                {
                    if (stacks[pos] != null)
                    {
                        this.topStacks[i] = stacks[pos];
                    }
                    else
                    {
                        this.topStacks[i] = null;
                    }
                    pos++;
                }
            }
        }
    }

    public ItemStack[] buildItemStackDataList()
    {
        if (this.type.isTransparent())
        {
            ItemStack[] sortList = new ItemStack[this.topStacks.length];
            int pos = 0;
            for (ItemStack is : this.topStacks)
            {
                if (is != null)
                {
                    sortList[pos++] = is;
                }
                else
                {
                    sortList[pos++] = null;
                }
            }
            return sortList;
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
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

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return this.type.acceptsStack(itemstack);
    }

    public void rotateAround()
    {
        this.facing++;
        if (this.facing > EnumFacing.EAST.ordinal())
        {
            this.facing = (byte) EnumFacing.NORTH.ordinal();
        }
        this.setFacing(this.facing);
        this.worldObj.addBlockEvent(this.pos, IronChest.ironChestBlock, 2, this.facing);
    }

    public void wasPlaced(EntityLivingBase entityliving, ItemStack itemStack)
    {
    }

    public void removeAdornments()
    {
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < this.chestContents.length; ++i)
        {
            this.chestContents[i] = null;
        }
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer player)
    {
        return null;
    }

    @Override
    public String getGuiID()
    {
        return "IronChest:" + this.type.name();
    }

    @Override
    public boolean canRenderBreaking()
    {
        return true;
    }
}
