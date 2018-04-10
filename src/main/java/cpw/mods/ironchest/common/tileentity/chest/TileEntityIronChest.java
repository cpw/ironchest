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
package cpw.mods.ironchest.common.tileentity.chest;

import java.util.Collections;
import java.util.Comparator;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.blocks.chest.BlockIronChest;
import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.core.IronChestBlocks;
import cpw.mods.ironchest.common.gui.chest.ContainerIronChest;
import cpw.mods.ironchest.common.network.MessageCrystalChestSync;
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
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class TileEntityIronChest extends TileEntityLockableLoot implements ITickable
{
    /** Chest Contents */
    public NonNullList<ItemStack> chestContents;

    /** Crystal Chest top stacks */
    private NonNullList<ItemStack> topStacks;

    /** The current angle of the lid (between 0 and 1) */
    public float lidAngle;

    /** The angle of the lid last tick */
    public float prevLidAngle;

    /** The number of players currently using this chest */
    public int numPlayersUsing;

    /** Server sync counter (once per 20 ticks) */
    private int ticksSinceSync;

    /** Direction chest is facing */
    private EnumFacing facing;

    /** If the inventory got touched */
    private boolean inventoryTouched;

    /** If the inventory had items */
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
        this.chestContents = NonNullList.<ItemStack> withSize(type.size, ItemStack.EMPTY);
        this.topStacks = NonNullList.<ItemStack> withSize(8, ItemStack.EMPTY);
        this.facing = EnumFacing.NORTH;
    }

    public void setContents(NonNullList<ItemStack> contents)
    {
        this.chestContents = NonNullList.<ItemStack> withSize(this.getType().size, ItemStack.EMPTY);

        for (int i = 0; i < contents.size(); i++)
        {
            if (i < this.chestContents.size())
            {
                this.getItems().set(i, contents.get(i));
            }
        }

        this.inventoryTouched = true;
    }

    @Override
    public int getSizeInventory()
    {
        return this.getItems().size();
    }

    public EnumFacing getFacing()
    {
        return this.facing;
    }

    public IronChestType getType()
    {
        IronChestType type = IronChestType.IRON;

        if (this.hasWorld())
        {
            IBlockState state = this.world.getBlockState(this.pos);

            if (state.getBlock() == IronChestBlocks.ironChestBlock)
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

        return this.getItems().get(index);
    }

    @Override
    public void markDirty()
    {
        super.markDirty();

        this.sortTopStacks();
    }

    protected void sortTopStacks()
    {
        if (!this.getType().isTransparent() || (this.world != null && this.world.isRemote))
        {
            return;
        }

        NonNullList<ItemStack> tempCopy = NonNullList.<ItemStack> withSize(this.getSizeInventory(), ItemStack.EMPTY);

        boolean hasStuff = false;

        int compressedIdx = 0;

        mainLoop:
        for (int i = 0; i < this.getSizeInventory(); i++)
        {
            ItemStack itemStack = this.getItems().get(i);

            if (!itemStack.isEmpty())
            {
                for (int j = 0; j < compressedIdx; j++)
                {
                    ItemStack tempCopyStack = tempCopy.get(j);

                    if (ItemStack.areItemsEqualIgnoreDurability(tempCopyStack, itemStack))
                    {
                        if (itemStack.getCount() != tempCopyStack.getCount())
                        {
                            tempCopyStack.grow(itemStack.getCount());
                        }

                        continue mainLoop;
                    }
                }

                tempCopy.set(compressedIdx, itemStack.copy());

                compressedIdx++;

                hasStuff = true;
            }
        }

        if (!hasStuff && this.hadStuff)
        {
            this.hadStuff = false;

            for (int i = 0; i < this.getTopItems().size(); i++)
            {
                this.getTopItems().set(i, ItemStack.EMPTY);
            }

            if (this.world != null)
            {
                IBlockState iblockstate = this.world.getBlockState(this.pos);

                this.world.notifyBlockUpdate(this.pos, iblockstate, iblockstate, 3);
            }

            return;
        }

        this.hadStuff = true;

        Collections.sort(tempCopy, new Comparator<ItemStack>()
        {
            @Override
            public int compare(ItemStack stack1, ItemStack stack2)
            {
                if (stack1.isEmpty())
                {
                    return 1;
                }
                else if (stack2.isEmpty())
                {
                    return -1;
                }
                else
                {
                    return stack2.getCount() - stack1.getCount();
                }
            }
        });

        int p = 0;

        for (ItemStack element : tempCopy)
        {
            if (!element.isEmpty() && element.getCount() > 0)
            {
                if (p == this.getTopItems().size())
                {
                    break;
                }

                this.getTopItems().set(p, element);

                p++;
            }
        }

        for (int i = p; i < this.getTopItems().size(); i++)
        {
            this.getTopItems().set(i, ItemStack.EMPTY);
        }

        if (this.world != null)
        {
            IBlockState iblockstate = this.world.getBlockState(this.pos);

            this.world.notifyBlockUpdate(this.pos, iblockstate, iblockstate, 3);
        }

        sendTopStacksPacket();
    }

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

    @Override
    public void setCustomName(String name)
    {
        this.customName = name;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        this.chestContents = NonNullList.<ItemStack> withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (compound.hasKey("CustomName", Constants.NBT.TAG_STRING))
        {
            this.customName = compound.getString("CustomName");
        }

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }

        this.facing = EnumFacing.VALUES[compound.getByte("facing")];

        this.sortTopStacks();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
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
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world == null)
        {
            return true;
        }

        if (this.world.getTileEntity(this.pos) != this)
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
        if (this.world != null && !this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + this.pos.getX() + this.pos.getY() + this.pos.getZ()) % 200 == 0)
        //@formatter:on
        {
            this.numPlayersUsing = 0;

            float f = 5.0F;

            //@formatter:off
            for (EntityPlayer player : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos.getX() - f, this.pos.getY() - f, this.pos.getZ() - f, this.pos.getX() + 1 + f, this.pos.getY() + 1 + f, this.pos.getZ() + 1 + f)))
            //@formatter:on
            {
                if (player.openContainer instanceof ContainerIronChest)
                {
                    ++this.numPlayersUsing;
                }
            }
        }

        if (this.world != null && !this.world.isRemote && this.ticksSinceSync < 0)
        {
            this.world.addBlockEvent(this.pos, IronChestBlocks.ironChestBlock, 3, ((this.numPlayersUsing << 3) & 0xF8) | (this.facing.ordinal() & 0x7));
        }

        if (!this.world.isRemote && this.inventoryTouched)
        {
            this.inventoryTouched = false;

            this.sortTopStacks();
        }

        this.ticksSinceSync++;

        this.prevLidAngle = this.lidAngle;

        float angle = 0.1F;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
        {
            double x = this.pos.getX() + 0.5D;
            double y = this.pos.getY() + 0.5D;
            double z = this.pos.getZ() + 0.5D;

            this.world.playSound(null, x, y, z, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
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

                this.world.playSound(null, x, y, z, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
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
            if (this.world == null)
            {
                return;
            }

            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;

            this.world.addBlockEvent(this.pos, IronChestBlocks.ironChestBlock, 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, IronChestBlocks.ironChestBlock, false);
            this.world.notifyNeighborsOfStateChange(this.pos.down(), IronChestBlocks.ironChestBlock, false);
        }
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.world == null)
            {
                return;
            }

            --this.numPlayersUsing;

            this.world.addBlockEvent(this.pos, IronChestBlocks.ironChestBlock, 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, IronChestBlocks.ironChestBlock, false);
            this.world.notifyNeighborsOfStateChange(this.pos.down(), IronChestBlocks.ironChestBlock, false);
        }
    }

    public void setFacing(EnumFacing facing)
    {
        this.facing = facing;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound compound = new NBTTagCompound();

        compound.setByte("facing", (byte) this.facing.ordinal());

        return new SPacketUpdateTileEntity(this.pos, 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        if (pkt.getTileEntityType() == 0)
        {
            NBTTagCompound compound = pkt.getNbtCompound();

            this.facing = EnumFacing.VALUES[compound.getByte("facing")];
        }
    }

    public NonNullList<ItemStack> buildItemStackDataList()
    {
        if (this.getType().isTransparent())
        {
            NonNullList<ItemStack> sortList = NonNullList.<ItemStack> withSize(this.getTopItems().size(), ItemStack.EMPTY);

            int pos = 0;

            for (ItemStack is : this.topStacks)
            {
                if (!is.isEmpty())
                {
                    sortList.set(pos, is);
                }
                else
                {
                    sortList.set(pos, ItemStack.EMPTY);
                }

                pos++;
            }

            return sortList;
        }

        return NonNullList.<ItemStack> withSize(this.getTopItems().size(), ItemStack.EMPTY);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return this.getType().acceptsStack(stack);
    }

    public void rotateAround()
    {
        this.setFacing(this.facing.rotateY());

        this.world.addBlockEvent(this.pos, IronChestBlocks.ironChestBlock, 2, this.facing.ordinal());
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
        this.fillWithLoot(playerIn);

        return new ContainerIronChest(playerInventory, this, this.chestType, this.chestType.xSize, this.chestType.ySize);
    }

    @Override
    public String getGuiID()
    {
        return "IronChest:" + this.getType().name() + "_chest";
    }

    @Override
    public boolean canRenderBreaking()
    {
        return true;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound compound = super.getUpdateTag();
        compound.setByte("facing", (byte) this.facing.ordinal());
        return compound;
    }

    @Override
    public NonNullList<ItemStack> getItems()
    {
        return this.chestContents;
    }

    public NonNullList<ItemStack> getTopItems()
    {
        return this.topStacks;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.chestContents)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    protected void sendTopStacksPacket()
    {
        NonNullList<ItemStack> stacks = this.buildItemStackDataList();
        //@formatter:off
        IronChest.packetHandler.sendToAllAround(new MessageCrystalChestSync(this, stacks), new TargetPoint(world.provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128));
        //@formatter:on
    }

    public void receiveMessageFromServer(NonNullList<ItemStack> topStacks)
    {
        this.topStacks = topStacks;
    }

    public static void registerFixesChest(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityIronChest.class, new String[] { "Items" }));
    }
}
