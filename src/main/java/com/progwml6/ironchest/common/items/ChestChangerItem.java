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
package com.progwml6.ironchest.common.items;

import com.progwml6.ironchest.common.blocks.ChestBlock;
import com.progwml6.ironchest.common.blocks.ChestType;
import com.progwml6.ironchest.common.tileentity.IronChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class ChestChangerItem extends TooltipItem
{
    public final ChestChangerType type;

    public ChestChangerItem(Properties properties, ChestChangerType chestChangerType)
    {
        super(properties);
        this.type = chestChangerType;
        this.setRegistryName(chestChangerType.itemName);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
    {
        PlayerEntity entityPlayer = context.getPlayer();
        BlockPos blockPos = context.getPos();
        World world = context.getWorld();
        ItemStack itemStack = context.getItem();

        if (world.isRemote)
        {
            return ActionResultType.PASS;
        }

        if (this.type.canUpgrade(ChestType.WOOD))
        {
            if (!(world.getBlockState(blockPos).getBlock() instanceof net.minecraft.block.ChestBlock))
            {
                return ActionResultType.PASS;
            }
        }
        else
        {
            if (world.getBlockState(blockPos).getBlock().getDefaultState() != ChestType.get(this.type.source))
            {
                return ActionResultType.PASS;
            }
        }

        TileEntity tileEntity = world.getTileEntity(blockPos);
        IronChestTileEntity newChest = new IronChestTileEntity();

        ITextComponent customName = null;

        NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
        Direction chestFacing = Direction.NORTH;

        if (tileEntity != null)
        {
            if (tileEntity instanceof IronChestTileEntity)
            {
                IronChestTileEntity chest = (IronChestTileEntity) tileEntity;
                BlockState chestState = world.getBlockState(blockPos);

                chestContents = chest.getItems();
                chestFacing = chestState.get(ChestBlock.FACING);
                customName = chest.getCustomName();
                newChest = this.type.target.makeEntity();

                if (newChest == null)
                {
                    return ActionResultType.PASS;
                }
            }
            else if (tileEntity instanceof ChestTileEntity)
            {
                BlockState chestState = world.getBlockState(blockPos);
                chestFacing = chestState.get(net.minecraft.block.ChestBlock.FACING);
                ChestTileEntity chest = (ChestTileEntity) tileEntity;

                if (ChestTileEntity.getPlayersUsing(world, blockPos) > 0)
                {
                    return ActionResultType.PASS;
                }

                if (!this.type.canUpgrade(ChestType.WOOD))
                {
                    return ActionResultType.PASS;
                }

                chestContents = NonNullList.<ItemStack>withSize(chest.getSizeInventory(), ItemStack.EMPTY);

                for (int slot = 0; slot < chestContents.size(); slot++)
                {
                    chestContents.set(slot, chest.getStackInSlot(slot));
                }

                customName = chest.getCustomName();

                newChest = this.type.target.makeEntity();
            }
        }

        tileEntity.updateContainingBlockInfo();

        world.removeTileEntity(blockPos);
        world.removeBlock(blockPos, false);

        BlockState iBlockState = ChestType.get(this.type.target).with(ChestBlock.FACING, chestFacing);

        world.setTileEntity(blockPos, newChest);
        world.setBlockState(blockPos, iBlockState, 3);

        world.notifyBlockUpdate(blockPos, iBlockState, iBlockState, 3);

        TileEntity tileEntity2 = world.getTileEntity(blockPos);

        if (tileEntity2 instanceof IronChestTileEntity)
        {
            if (customName != null)
            {
                ((IronChestTileEntity) tileEntity2).setCustomName(customName);
            }

            ((IronChestTileEntity) tileEntity2).setItems(chestContents);
        }

        if (!entityPlayer.abilities.isCreativeMode)
        {
            itemStack.shrink(1);
        }

        return ActionResultType.SUCCESS;
    }
}
