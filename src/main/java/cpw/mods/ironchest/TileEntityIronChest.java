/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors:
 * cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;

public class TileEntityIronChest extends TileEntityLockableLoot implements ITickable//, IInventory
{
    /** The current angle of the lid (between 0 and 1) */
    public float lidAngle;

    /** The angle of the lid last tick */
    public float prevLidAngle;

    private NonNullList<ItemStack> chestContents;

    private int ticksSinceSync = -1;

    private int numPlayersUsing;

    //private ItemStack[] topStacks;

    private EnumFacing facing;

    private boolean inventoryTouched;

    private boolean hadStuff;

    private String customName;

    private IronChestType chestType;

    public TileEntityIronChest()
    {
        this(IronChestType.IRON);
    }

    protected TileEntityIronChest(IronChestType type)
    {
        super();
        this.chestType = type;
        this.chestContents = NonNullList.<ItemStack> func_191197_a(type.size, ItemStack.field_190927_a);
        //this.topStacks = new ItemStack[8];
        this.facing = EnumFacing.NORTH;
    }

    public void setContents(NonNullList<ItemStack> contents)
    {
        this.chestContents = NonNullList.<ItemStack> func_191197_a(this.getType().size, ItemStack.field_190927_a);

        for (int i = 0; i < contents.size(); i++)
        {
            if (i < this.chestContents.size())
            {
                this.chestContents.set(i, contents.get(i));
            }
        }

        this.inventoryTouched = true;
    }

    @Override
    public int getSizeInventory()
    {
        return this.chestContents.size();
    }

    public EnumFacing getFacing()
    {
        return this.facing;
    }

    public void setFacing(EnumFacing facing)
    {
        this.facing = facing;
    }

    public IronChestType getType()
    {
        IronChestType type = IronChestType.IRON;

        if (this.hasWorldObj())
        {
            IBlockState state = this.worldObj.getBlockState(this.pos);

            if (state.getBlock() == IronChest.ironChestBlock)
            {
                type = state.getValue(BlockIronChest.VARIANT_PROP);
            }
        }

        return type;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        this.fillWithLoot((EntityPlayer) null);

        this.inventoryTouched = true;

        return this.func_190576_q().get(index);
    }

    @Override
    public void markDirty()
    {
        super.markDirty();

        //this.sortTopStacks();
    }

    /*protected void sortTopStacks()
    {
        if (!this.getType().isTransparent() || (this.worldObj != null && this.worldObj.isRemote))
        {
            return;
        }
    
        ItemStack[] tempCopy = new ItemStack[this.getSizeInventory()];
    
        boolean hasStuff = false;
    
        int compressedIdx = 0;
    
        mainLoop:
        for (int i = 0; i < this.getSizeInventory(); i++)
        {
            if (this.chestContents[i] != null)
            {
                for (int j = 0; j < compressedIdx; j++)
                {
                    if (tempCopy[j].isItemEqual(this.chestContents[i]))
                    {
                        tempCopy[j].func_190920_e(tempCopy[j].func_190916_E() + this.chestContents[i].func_190916_E());
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
    
        Arrays.sort(tempCopy, new Comparator<ItemStack>()
        {
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
                    return o2.func_190916_E() - o1.func_190916_E();
                }
            }
        });
    
        int p = 0;
    
        for (ItemStack element : tempCopy)
        {
            if (element != ItemStack.field_190927_a && element.func_190916_E() > 0)
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
    }*/

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : this.getType().name();
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
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        this.chestContents = NonNullList.<ItemStack> func_191197_a(this.getSizeInventory(), ItemStack.field_190927_a);

        if (compound.hasKey("CustomName", Constants.NBT.TAG_STRING))
        {
            this.customName = compound.getString("CustomName");
        }

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.func_191283_b(compound, this.chestContents);
        }

        this.facing = EnumFacing.VALUES[compound.getByte("facing")];

        //this.sortTopStacks();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.func_191282_a(compound, this.chestContents);
        }

        compound.setByte("facing", (byte) this.facing.ordinal());

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        if (this.worldObj == null)
        {
            return true;
        }

        if (this.worldObj.getTileEntity(this.pos) != this)
        {
            return false;
        }

        return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64D;
    }

    @Override
    public void update()
    {
        // Resynchronizes clients with the server state
        //@formatter:off
        if (this.worldObj != null && !this.worldObj.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + this.pos.getX() + this.pos.getY() + this.pos.getZ()) % 200 == 0)
        //@formatter:on
        {
            this.numPlayersUsing = 0;

            float f = 5.0F;

            //@formatter:off
            for (EntityPlayer player : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class,
                    new AxisAlignedBB(this.pos.getX() - f, this.pos.getY() - f, this.pos.getZ() - f, this.pos.getX() + 1 + f, this.pos.getY() + 1 + f, this.pos.getZ() + 1 + f)))
            //@formatter:on
            {
                if (player.openContainer instanceof ContainerIronChest)
                {
                    ++this.numPlayersUsing;
                }
            }
        }

        if (this.worldObj != null && !this.worldObj.isRemote && this.ticksSinceSync < 0)
        {
            this.worldObj.addBlockEvent(this.pos, IronChest.ironChestBlock, 3, ((this.numPlayersUsing << 3) & 0xF8) | (this.facing.ordinal() & 0x7));
        }

        if (!this.worldObj.isRemote && this.inventoryTouched)
        {
            this.inventoryTouched = false;

            //this.sortTopStacks();
        }

        this.ticksSinceSync++;

        this.prevLidAngle = this.lidAngle;

        float angle = 0.1F;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
        {
            double x = this.pos.getX() + 0.5D;
            double y = this.pos.getY() + 0.5D;
            double z = this.pos.getZ() + 0.5D;

            //@formatter:off
            this.worldObj.playSound(null, x, y, z, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            //@formatter:on
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float currentAngle = this.lidAngle;

            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += angle;
            }
            else
            {
                this.lidAngle -= angle;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float maxAngle = 0.5F;

            if (this.lidAngle < maxAngle && currentAngle >= maxAngle)
            {
                double x = this.pos.getX() + 0.5D;
                double y = this.pos.getY() + 0.5D;
                double z = this.pos.getZ() + 0.5D;

                //@formatter:off
                this.worldObj.playSound(null, x, y, z, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
                //@formatter:on
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
        }
        else if (id == 2)
        {
            this.facing = EnumFacing.VALUES[type];
        }
        else if (id == 3)
        {
            this.facing = EnumFacing.VALUES[type & 0x7];
            this.numPlayersUsing = (type & 0xF8) >> 3;
        }

        return true;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.worldObj == null)
            {
                return;
            }

            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;

            this.worldObj.addBlockEvent(this.pos, IronChest.ironChestBlock, 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, IronChest.ironChestBlock, false);
            this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), IronChest.ironChestBlock, false);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.worldObj == null)
            {
                return;
            }

            --this.numPlayersUsing;

            this.worldObj.addBlockEvent(this.pos, IronChest.ironChestBlock, 1, this.numPlayersUsing);
            this.worldObj.notifyNeighborsOfStateChange(this.pos, IronChest.ironChestBlock, false);
            this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), IronChest.ironChestBlock, false);
        }
    }

    //public ItemStack[] getTopItemStacks()
    //{
    //    return this.topStacks;
    //}

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setByte("facing", (byte) this.facing.ordinal());

        /*ItemStack[] stacks = this.buildItemStackDataList();
        
        if (stacks != null)
        {
            NBTTagList itemList = new NBTTagList();
        
            for (int slot = 0; slot < stacks.length; slot++)
            {
                if (stacks[slot] != null)
                {
                    NBTTagCompound item = new NBTTagCompound();
        
                    item.setByte("Slot", (byte) slot);
        
                    stacks[slot].writeToNBT(item);
        
                    itemList.appendTag(item);
                }
            }
        
            compound.setTag("stacks", itemList);
        }*/

        return new SPacketUpdateTileEntity(this.pos, 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (pkt.getTileEntityType() == 0)
        {
            NBTTagCompound compound = pkt.getNbtCompound();

            this.facing = EnumFacing.VALUES[compound.getByte("facing")];

            /*NBTTagList itemList = compound.getTagList("stacks", Constants.NBT.TAG_COMPOUND);
            
            ItemStack[] stacks = new ItemStack[this.topStacks.length];
            
            for (int item = 0; item < stacks.length; item++)
            {
                NBTTagCompound itemStack = itemList.getCompoundTagAt(item);
            
                int slot = itemStack.getByte("Slot") & 255;
            
                if (slot >= 0 && slot < stacks.length)
                {
                    stacks[slot] = ItemStack.loadItemStackFromNBT(itemStack);
                }
            }
            
            if (this.getType().isTransparent() && stacks != null)
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
            }*/
        }
    }

    /*public ItemStack[] buildItemStackDataList()
    {
        if (this.getType().isTransparent())
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
    }*/

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return this.getType().acceptsStack(stack);
    }

    public void rotateAround()
    {
        this.setFacing(this.facing.rotateY());

        this.worldObj.addBlockEvent(this.pos, IronChest.ironChestBlock, 2, this.facing.ordinal());
    }

    public void wasPlaced(EntityLivingBase entityliving, ItemStack stack)
    {
    }

    public void removeAdornments()
    {
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        this.fillWithLoot((EntityPlayer) null);

        return new ContainerIronChest(playerInventory, this, this.chestType, this.chestType.xSize, this.chestType.ySize);
    }

    @Override
    public String getGuiID()
    {
        return "IronChest:" + this.getType().name();
    }

    @Override
    public boolean canRenderBreaking()
    {
        return true;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    protected NonNullList<ItemStack> func_190576_q()
    {
        return this.chestContents;
    }

    @Override
    public boolean func_191420_l()
    {
        for (ItemStack itemstack : this.chestContents)
        {
            if (!itemstack.func_190926_b())
            {
                return false;
            }
        }

        return true;
    }
}
