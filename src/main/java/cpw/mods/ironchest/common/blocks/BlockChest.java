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
package cpw.mods.ironchest.common.blocks;

import cpw.mods.ironchest.common.tileentity.TileEntityIronChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.INameable;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public abstract class BlockChest extends Block implements ITileEntityProvider
{
    public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;

    protected static final VoxelShape IRON_CHEST_SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    private final IronChestType type;

    public BlockChest(Builder builderIn, IronChestType typeIn)
    {
        super(builderIn);

        this.type = typeIn;

        this.setDefaultState((IBlockState) ((IBlockState) this.stateContainer.getBaseState()).with(FACING, EnumFacing.NORTH));

        this.setRegistryName(new ResourceLocation(type.itemName));
    }

    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return IRON_CHEST_SHAPE;
    }

    @Override
    public boolean isSolid(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context)
    {
        EnumFacing enumfacing = context.getPlacementHorizontalFacing().getOpposite();

        return this.getDefaultState().with(FACING, enumfacing);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity != null && tileentity instanceof TileEntityIronChest)
        {
            TileEntityIronChest teic = (TileEntityIronChest) tileentity;

            teic.wasPlaced(placer, stack);

            if (stack.hasDisplayName())
            {
                teic.setCustomName(stack.getDisplayName());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock())
        {
            TileEntityIronChest tileentity = (TileEntityIronChest) worldIn.getTileEntity(pos);
            if (tileentity != null)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);

                tileentity.removeAdornments();
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    public ILockableContainer getContainer(IBlockState state, World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (!(tileentity instanceof TileEntityIronChest))
        {
            return null;
        }
        else
        {
            return (ILockableContainer) tileentity;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (te instanceof INameable && ((INameable) te).hasCustomName())
        {
            player.addStat(StatList.BLOCK_MINED.get(this));
            player.addExhaustion(0.005F);
            if (worldIn.isRemote)
            {
                return;
            }

            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            Item item = this.getItemDropped(state, worldIn, pos, i).asItem();
            if (item == Items.AIR)
            {
                return;
            }

            ItemStack itemstack = new ItemStack(item, this.quantityDropped(state, worldIn.rand));
            itemstack.setDisplayName(((INameable) te).getCustomName());
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, (TileEntity) null, stack);
        }
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstoneFromInventory(this.getContainer(blockState, worldIn, pos));
    }

    @Override
    public IBlockState rotate(IBlockState state, Rotation rot)
    {
        return (IBlockState) state.with(FACING, rot.rotate((EnumFacing) state.get(FACING)));
    }

    @Override
    public IBlockState mirror(IBlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.toRotation((EnumFacing) state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder)
    {
        builder.add(FACING);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean allowsMovement(IBlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
    {
        return false;
    }

    public static IronChestType getTypeFromItem(Item itemIn)
    {
        return getTypeFromBlock(Block.getBlockFromItem(itemIn));
    }

    public static IronChestType getTypeFromBlock(Block blockIn)
    {
        return blockIn instanceof BlockChest ? ((BlockChest) blockIn).getType() : null;
    }

    public IronChestType getType()
    {
        return this.type;
    }
}
