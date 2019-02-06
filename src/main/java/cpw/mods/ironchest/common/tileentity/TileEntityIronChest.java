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
package cpw.mods.ironchest.common.tileentity;

import cpw.mods.ironchest.common.blocks.BlockChest;
import cpw.mods.ironchest.common.blocks.IronChestType;
import cpw.mods.ironchest.common.core.IronChestBlocks;
import cpw.mods.ironchest.common.gui.ContainerIronChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityIronChest extends TileEntityLockableLoot implements IChestLid, ITickable
{
    private NonNullList<ItemStack> chestContents;

    /** The current angle of the lid (between 0 and 1) */
    protected float lidAngle;

    /** The angle of the lid last tick */
    protected float prevLidAngle;

    /** The number of players currently using this chest */
    protected int numPlayersUsing;

    /**
     * A counter that is incremented once each tick. Used to determine when to recompute {@link #numPlayersUsing}; this
     * is done every 200 ticks (but staggered between different chests). However, the new value isn't actually sent to
     * clients when it is changed.
     */
    private int ticksSinceSync;

    private IronChestType chestType;

    private Block blockToUse;

    public TileEntityIronChest()
    {
        this(IronChestEntityType.IRON_CHEST, IronChestType.IRON, IronChestBlocks.ironChestBlock);
    }

    protected TileEntityIronChest(TileEntityType<?> typeIn, IronChestType chestTypeIn, Block blockToUseIn)
    {
        super(typeIn);
        this.chestType = chestTypeIn;
        this.chestContents = NonNullList.<ItemStack>withSize(chestTypeIn.size, ItemStack.EMPTY);
        this.blockToUse = blockToUseIn;
    }

    @Override
    public void setItems(NonNullList<ItemStack> contents)
    {
        this.chestContents = NonNullList.<ItemStack>withSize(this.getIronChestType().size, ItemStack.EMPTY);

        for (int i = 0; i < contents.size(); i++)
        {
            if (i < this.chestContents.size())
            {
                this.getItems().set(i, contents.get(i));
            }
        }
    }

    @Override
    public int getSizeInventory()
    {
        return this.getItems().size();
    }

    public IronChestType getIronChestType()
    {
        IronChestType type = IronChestType.IRON;

        if (this.hasWorld())
        {
            IronChestType typeNew = BlockChest.getTypeFromBlock(this.getBlockState().getBlock());

            if (typeNew != null)
            {
                type = typeNew;
            }
        }

        return type;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        this.fillWithLoot((EntityPlayer) null);

        return this.getItems().get(index);
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
    }

    @Override
    public void tick()
    {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;
        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0)
        {
            this.numPlayersUsing = 0;

            for (EntityPlayer entityplayer : this.world
                    .getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(i - 5.0F, j - 5.0F, k - 5.0F, i + 1 + 5.0F, j + 1 + 5.0F, k + 1 + 5.0F)))
            {
                if (entityplayer.openContainer instanceof ContainerIronChest)
                {
                    ++this.numPlayersUsing;
                }
            }
        }

        this.prevLidAngle = this.lidAngle;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
        {
            this.playSound(SoundEvents.BLOCK_CHEST_OPEN);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
        {
            float f2 = this.lidAngle;
            if (this.numPlayersUsing > 0)
            {
                this.lidAngle += 0.1F;
            }
            else
            {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            if (this.lidAngle < 0.5F && f2 >= 0.5F)
            {
                this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }
    }

    private void playSound(SoundEvent soundIn)
    {
        double d0 = this.pos.getX() + 0.5D;
        double d1 = this.pos.getY() + 0.5D;
        double d2 = this.pos.getZ() + 0.5D;

        this.world.playSound((EntityPlayer) null, d0, d1, d2, soundIn, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public ITextComponent getName()
    {
        ITextComponent itextcomponent = this.getCustomName();
        return itextcomponent != null ? itextcomponent : new TextComponentTranslation(this.getIronChestType().name(), new Object[0]);
    }

    @Override
    public void read(NBTTagCompound compound)
    {
        super.read(compound);

        this.chestContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound))
        {
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }

        if (compound.contains("CustomName", 8))
        {
            this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
        }
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound)
    {
        super.write(compound);
        if (!this.checkLootAndWrite(compound))
        {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
        }

        ITextComponent itextcomponent = this.getCustomName();
        if (itextcomponent != null)
        {
            compound.putString("CustomName", ITextComponent.Serializer.toJson(itextcomponent));
        }

        return compound;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
     * clientside.
     */
    @Override
    public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
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
            if (this.numPlayersUsing < 0)
            {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (!player.isSpectator())
        {
            --this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    protected void onOpenOrClose()
    {
        Block block = this.getBlockState().getBlock();

        if (block instanceof BlockChest)
        {
            this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, block);
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return this.getIronChestType().acceptsStack(stack);
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

        return new ContainerIronChest(playerInventory, this, this.chestType, playerIn, this.chestType.xSize, this.chestType.ySize);
    }

    @Override
    public String getGuiID()
    {
        return "IronChest:" + this.getIronChestType().name() + "_chest";
    }

    @Override
    public NonNullList<ItemStack> getItems()
    {
        return this.chestContents;
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getLidAngle(float partialTicks)
    {
        return this.prevLidAngle + (this.lidAngle - this.prevLidAngle) * partialTicks;
    }

    public static int getPlayersUsing(IBlockReader reader, BlockPos posIn)
    {
        IBlockState iblockstate = reader.getBlockState(posIn);
        if (iblockstate.hasTileEntity())
        {
            TileEntity tileentity = reader.getTileEntity(posIn);
            if (tileentity instanceof TileEntityIronChest)
            {
                return ((TileEntityIronChest) tileentity).numPlayersUsing;
            }
        }

        return 0;
    }

    public Block getBlockToUse()
    {
        return this.blockToUse;
    }
}
