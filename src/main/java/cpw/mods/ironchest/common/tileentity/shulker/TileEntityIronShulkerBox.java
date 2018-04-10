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
package cpw.mods.ironchest.common.tileentity.shulker;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.blocks.shulker.BlockIronShulkerBox;
import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.gui.shulker.ContainerIronShulkerBox;
import cpw.mods.ironchest.common.network.MessageCrystalShulkerSync;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
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
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityIronShulkerBox extends TileEntityLockableLoot implements ITickable, ISidedInventory
{
    private final int[] SLOTS;

    /** Shulker Box Contents */
    private NonNullList<ItemStack> items;

    /** Crystal Shulker Boxes top stacks */
    private NonNullList<ItemStack> topStacks;

    /** Direction Shulker ox is facing */
    private EnumFacing facing;

    /** If the inventory got touched */
    private boolean inventoryTouched;

    /** Server sync counter (once per 20 ticks) */
    private int ticksSinceSync;

    /** If the inventory had items */
    private boolean hadStuff;

    private boolean hasBeenCleared;
    private int openCount;
    private AnimationStatus animationStatus;
    private float progress;
    private float progressOld;
    private EnumDyeColor color;
    private boolean destroyedByCreativePlayer;
    private boolean hasBeenUpgraded;

    /** The Variant of the Shulker Box (Not Color) */
    private IronShulkerBoxType shulkerBoxType;

    public TileEntityIronShulkerBox()
    {
        this(null);
    }

    public TileEntityIronShulkerBox(@Nullable EnumDyeColor colorIn)
    {
        this(colorIn, IronShulkerBoxType.IRON);
    }

    public TileEntityIronShulkerBox(@Nullable EnumDyeColor colorIn, IronShulkerBoxType typeIn)
    {
        super();

        this.shulkerBoxType = typeIn;

        this.SLOTS = new int[typeIn.size];

        this.items = NonNullList.<ItemStack> withSize(typeIn.size, ItemStack.EMPTY);
        this.topStacks = NonNullList.<ItemStack> withSize(8, ItemStack.EMPTY);

        this.animationStatus = AnimationStatus.CLOSED;
        this.color = colorIn;

        this.facing = EnumFacing.UP;

        this.hasBeenUpgraded = false;
    }

    public void setContents(NonNullList<ItemStack> contents)
    {
        this.items = NonNullList.<ItemStack> withSize(this.getType().size, ItemStack.EMPTY);

        for (int i = 0; i < contents.size(); i++)
        {
            if (i < this.items.size())
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

    public IronShulkerBoxType getType()
    {
        IronShulkerBoxType type = IronShulkerBoxType.IRON;

        if (this.hasWorld())
        {
            IBlockState state = this.world.getBlockState(this.pos);

            if (state.getBlock() instanceof BlockIronShulkerBox)
            {
                type = state.getValue(BlockIronShulkerBox.VARIANT_PROP);
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

        sendTopStacksPacket();
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : this.getType().name();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        this.loadFromNbt(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        return this.saveToNbt(compound);
    }

    public void loadFromNbt(NBTTagCompound compound)
    {
        this.items = NonNullList.<ItemStack> withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound) && compound.hasKey("Items", 9))
        {
            ItemStackHelper.loadAllItems(compound, this.items);
        }

        if (compound.hasKey("CustomName", 8))
        {
            this.customName = compound.getString("CustomName");
        }

        this.facing = EnumFacing.VALUES[compound.getByte("facing")];

        this.sortTopStacks();
    }

    public NBTTagCompound saveToNbt(NBTTagCompound compound)
    {
        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.items, false);
        }

        compound.setInteger("ShulkerBoxSize", this.getSizeInventory());

        compound.setByte("facing", (byte) this.facing.ordinal());

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.customName);
        }

        if (!compound.hasKey("Lock") && this.isLocked())
        {
            this.getLockCode().toNBT(compound);
        }

        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
    public void update()
    {
        this.updateAnimation();

        if (this.animationStatus == AnimationStatus.OPENING || this.animationStatus == AnimationStatus.CLOSING)
        {
            this.moveCollidedEntities();
        }

        if (this.world != null && !this.world.isRemote && this.ticksSinceSync < 0)
        {
            this.world.addBlockEvent(this.pos, this.getBlockType(), 3, ((this.openCount << 3) & 0xF8) | (this.facing.ordinal() & 0x7));
        }

        if (!this.world.isRemote && this.inventoryTouched)
        {
            this.inventoryTouched = false;

            this.sortTopStacks();
        }

        this.ticksSinceSync++;
    }

    protected void updateAnimation()
    {
        this.progressOld = this.progress;

        switch (this.animationStatus)
        {
        case CLOSED:
            this.progress = 0.0F;
            break;
        case OPENING:
            this.progress += 0.1F;

            if (this.progress >= 1.0F)
            {
                this.moveCollidedEntities();
                this.animationStatus = AnimationStatus.OPENED;
                this.progress = 1.0F;
            }

            break;
        case CLOSING:
            this.progress -= 0.1F;

            if (this.progress <= 0.0F)
            {
                this.animationStatus = AnimationStatus.CLOSED;
                this.progress = 0.0F;
            }

            break;
        case OPENED:
            this.progress = 1.0F;
        }
    }

    public AnimationStatus getAnimationStatus()
    {
        return this.animationStatus;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return this.getBoundingBox(this.getFacing());
    }

    public AxisAlignedBB getBoundingBox(EnumFacing facing)
    {
        //@formatter:off
        return Block.FULL_BLOCK_AABB.expand(0.5F * this.getProgress(1.0F) * facing.getFrontOffsetX(), 0.5F * this.getProgress(1.0F) * facing.getFrontOffsetY(), 0.5F * this.getProgress(1.0F) * facing.getFrontOffsetZ());
        //@formatter:on
    }

    private AxisAlignedBB getTopBoundingBox(EnumFacing facing)
    {
        EnumFacing enumfacing = facing.getOpposite();

        return this.getBoundingBox(facing).contract(enumfacing.getFrontOffsetX(), enumfacing.getFrontOffsetY(), enumfacing.getFrontOffsetZ());
    }

    private void moveCollidedEntities()
    {
        IBlockState iblockstate = this.world.getBlockState(this.getPos());

        if (iblockstate.getBlock() instanceof BlockIronShulkerBox)
        {
            EnumFacing enumfacing = this.getFacing();
            AxisAlignedBB axisalignedbb = this.getTopBoundingBox(enumfacing).offset(this.pos);
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity((Entity) null, axisalignedbb);

            if (!list.isEmpty())
            {
                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity = list.get(i);

                    if (entity.getPushReaction() != EnumPushReaction.IGNORE)
                    {
                        double d0 = 0.0D;
                        double d1 = 0.0D;
                        double d2 = 0.0D;
                        AxisAlignedBB axisalignedbb1 = entity.getEntityBoundingBox();

                        switch (enumfacing.getAxis())
                        {
                        case X:

                            if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                            {
                                d0 = axisalignedbb.maxX - axisalignedbb1.minX;
                            }
                            else
                            {
                                d0 = axisalignedbb1.maxX - axisalignedbb.minX;
                            }

                            d0 = d0 + 0.01D;
                            break;
                        case Y:

                            if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                            {
                                d1 = axisalignedbb.maxY - axisalignedbb1.minY;
                            }
                            else
                            {
                                d1 = axisalignedbb1.maxY - axisalignedbb.minY;
                            }

                            d1 = d1 + 0.01D;
                            break;
                        case Z:

                            if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                            {
                                d2 = axisalignedbb.maxZ - axisalignedbb1.minZ;
                            }
                            else
                            {
                                d2 = axisalignedbb1.maxZ - axisalignedbb.minZ;
                            }

                            d2 = d2 + 0.01D;
                        }

                        //@formatter:off
                        entity.move(MoverType.SHULKER_BOX, d0 * enumfacing.getFrontOffsetX(), d1 * enumfacing.getFrontOffsetY(), d2 * enumfacing.getFrontOffsetZ());
                        //@formatter:on
                    }
                }
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.openCount = type;

            if (type == 0)
            {
                this.animationStatus = AnimationStatus.CLOSING;
            }

            if (type == 1)
            {
                this.animationStatus = AnimationStatus.OPENING;
            }

            return true;
        }
        else if (id == 2)
        {
            this.facing = EnumFacing.VALUES[type];

            return true;
        }
        else if (id == 3)
        {
            this.facing = EnumFacing.VALUES[type & 0x7];

            this.openCount = (type & 0xF8) >> 3;

            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            if (this.openCount < 0)
            {
                this.openCount = 0;
            }

            ++this.openCount;

            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.openCount);

            if (this.openCount == 1)
            {
                //@formatter:off
                this.world.playSound((EntityPlayer) null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                //@formatter:on
            }
        }
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            --this.openCount;

            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.openCount);

            if (this.openCount <= 0)
            {
                //@formatter:off
                this.world.playSound((EntityPlayer) null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                //@formatter:on
            }
        }
    }

    public void setFacing(EnumFacing facing)
    {
        this.facing = facing;
    }

    @Override
    @Nullable
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
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        this.fillWithLoot(playerIn);

        return new ContainerIronShulkerBox(playerInventory, this, this.shulkerBoxType, this.shulkerBoxType.xSize, this.shulkerBoxType.ySize);
    }

    @Override
    public String getGuiID()
    {
        return "IronChest:" + this.getType().name() + "_shulker_box";
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
        return this.items;
    }

    public NonNullList<ItemStack> getTopItems()
    {
        return this.topStacks;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.items)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return SLOTS;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        //@formatter:off
        return !(Block.getBlockFromItem(itemStackIn.getItem()) instanceof BlockIronShulkerBox) && !(Block.getBlockFromItem(itemStackIn.getItem()) instanceof BlockShulkerBox);
        //@formatter:on
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return true;
    }

    @Override
    public void clear()
    {
        this.hasBeenCleared = true;
        super.clear();
    }

    public boolean isCleared()
    {
        return this.hasBeenCleared;
    }

    public void setHasBeenUpgraded()
    {
        this.hasBeenUpgraded = true;
    }

    public boolean beenUpgraded()
    {
        return this.hasBeenUpgraded;
    }

    public float getProgress(float partialTicks)
    {
        return this.progressOld + (this.progress - this.progressOld) * partialTicks;
    }

    @SideOnly(Side.CLIENT)
    public EnumDyeColor getColor()
    {
        if (this.color == null)
        {
            this.color = BlockIronShulkerBox.getColorFromBlock(this.getBlockType());
        }

        return this.color;
    }

    public boolean isDestroyedByCreativePlayer()
    {
        return this.destroyedByCreativePlayer;
    }

    public void setDestroyedByCreativePlayer(boolean destoryedByCreativeUser)
    {
        this.destroyedByCreativePlayer = destoryedByCreativeUser;
    }

    public boolean shouldDrop()
    {
        return !this.isDestroyedByCreativePlayer() || !this.isEmpty() || this.hasCustomName() || this.lootTable != null;
    }

    protected void sendTopStacksPacket()
    {
        NonNullList<ItemStack> stacks = this.buildItemStackDataList();
        //@formatter:off
        IronChest.packetHandler.sendToAllAround(new MessageCrystalShulkerSync(this, stacks), new TargetPoint(world.provider.getDimension(), getPos().getX(), getPos().getY(), getPos().getZ(), 128));
        //@formatter:on
    }

    public void receiveMessageFromServer(NonNullList<ItemStack> topStacks)
    {
        this.topStacks = topStacks;
    }

    public static enum AnimationStatus
    {
        CLOSED, OPENING, OPENED, CLOSING;
    }

    public static void registerFixesShulkerBox(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityIronShulkerBox.class, new String[] { "Items" }));
    }
}
