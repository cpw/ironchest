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
package cpw.mods.ironchest.common.items;

import cpw.mods.ironchest.common.blocks.BlockChest;
import cpw.mods.ironchest.common.blocks.BlockIronChest;
import cpw.mods.ironchest.common.blocks.IronChestType;
import cpw.mods.ironchest.common.tileentity.TileEntityIronChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class ItemChestChanger extends ItemTooltip
{
    public final ChestChangerType type;

    public ItemChestChanger(Builder properties, ChestChangerType chestChangerType)
    {
        super(properties);
        this.type = chestChangerType;
        this.setRegistryName(chestChangerType.itemName);
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, ItemUseContext context)
    {
        EntityPlayer entityPlayer = context.getPlayer();
        BlockPos blockPos = context.getPos();
        World world = context.getWorld();
        ItemStack itemStack = context.getItem();

        if (world.isRemote)
        {
            return EnumActionResult.PASS;
        }

        if (this.type.canUpgrade(IronChestType.WOOD))
        {
            if (!(world.getBlockState(blockPos).getBlock() instanceof net.minecraft.block.BlockChest))
            {
                return EnumActionResult.PASS;
            }
        }
        else
        {
            if (world.getBlockState(blockPos).getBlock().getDefaultState() != IronChestType.get(this.type.source))
            {
                return EnumActionResult.PASS;
            }
        }

        TileEntity tileEntity = world.getTileEntity(blockPos);
        TileEntityIronChest newChest = new TileEntityIronChest();

        ITextComponent customName = null;

        NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
        EnumFacing chestFacing = EnumFacing.NORTH;

        if (tileEntity != null)
        {
            if (tileEntity instanceof TileEntityIronChest)
            {
                TileEntityIronChest chest = (TileEntityIronChest) tileEntity;
                IBlockState chestState = world.getBlockState(blockPos);

                chestContents = chest.getItems();
                chestFacing = chestState.get(BlockChest.FACING);
                customName = chest.getCustomName();
                newChest = this.type.target.makeEntity();

                if (newChest == null)
                {
                    return EnumActionResult.PASS;
                }
            }
            else if (tileEntity instanceof TileEntityChest)
            {
                IBlockState chestState = world.getBlockState(blockPos);
                chestFacing = chestState.get(net.minecraft.block.BlockChest.FACING);
                TileEntityChest chest = (TileEntityChest) tileEntity;

                if (TileEntityChest.getPlayersUsing(world, blockPos) > 0)
                {
                    return EnumActionResult.PASS;
                }

                if (!this.type.canUpgrade(IronChestType.WOOD))
                {
                    return EnumActionResult.PASS;
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
        world.removeBlock(blockPos);

        IBlockState iBlockState = IronChestType.get(this.type.target).with(BlockIronChest.FACING, chestFacing);

        System.out.println(iBlockState);

        world.setTileEntity(blockPos, newChest);
        world.setBlockState(blockPos, iBlockState, 3);

        world.notifyBlockUpdate(blockPos, iBlockState, iBlockState, 3);

        TileEntity tileEntity2 = world.getTileEntity(blockPos);

        if (tileEntity2 instanceof TileEntityIronChest)
        {
            if (customName != null)
            {
                ((TileEntityIronChest) tileEntity2).setCustomName(customName);
            }

            ((TileEntityIronChest) tileEntity2).setItems(chestContents);
        }

        if (!entityPlayer.abilities.isCreativeMode)
        {
            itemStack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }
}
