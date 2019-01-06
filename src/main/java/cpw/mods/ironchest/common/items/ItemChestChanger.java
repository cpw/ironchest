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
        EntityPlayer entityplayer = context.getPlayer();
        BlockPos blockpos = context.getPos();
        World world = context.getWorld();
        ItemStack itemstack = context.getItem();

        if (world.isRemote)
        {
            return EnumActionResult.PASS;
        }

        if (this.type.canUpgrade(IronChestType.WOOD))
        {
            if (!(world.getBlockState(blockpos).getBlock() instanceof net.minecraft.block.BlockChest))
            {
                return EnumActionResult.PASS;
            }
        }
        else
        {
            //@formatter:off
            if (world.getBlockState(blockpos).getBlock().getDefaultState() != IronChestType.get(this.type.source))
            //@formatter:on
            {
                return EnumActionResult.PASS;
            }
        }

        TileEntity te = world.getTileEntity(blockpos);
        TileEntityIronChest newchest = new TileEntityIronChest();

        ITextComponent customname = null;

        NonNullList<ItemStack> chestContents = NonNullList.<ItemStack> withSize(27, ItemStack.EMPTY);
        EnumFacing chestFacing = EnumFacing.NORTH;

        if (te != null)
        {
            if (te instanceof TileEntityIronChest)
            {
                TileEntityIronChest chest = (TileEntityIronChest) te;
                IBlockState chestState = world.getBlockState(blockpos);

                chestContents = chest.getItems();
                chestFacing = chestState.get(BlockChest.FACING);
                customname = chest.getCustomName();
                newchest = this.type.target.makeEntity();

                if (newchest == null)
                {
                    return EnumActionResult.PASS;
                }
            }
            else if (te instanceof TileEntityChest)
            {
                IBlockState chestState = world.getBlockState(blockpos);
                chestFacing = chestState.get(net.minecraft.block.BlockChest.FACING);
                TileEntityChest chest = (TileEntityChest) te;

                if (TileEntityChest.getPlayersUsing(world, blockpos) > 0)
                {
                    return EnumActionResult.PASS;
                }
                if (!this.type.canUpgrade(IronChestType.WOOD))
                {
                    return EnumActionResult.PASS;
                }

                chestContents = NonNullList.<ItemStack> withSize(chest.getSizeInventory(), ItemStack.EMPTY);

                for (int i = 0; i < chestContents.size(); i++)
                {
                    chestContents.set(i, chest.getStackInSlot(i));
                }

                customname = chest.getCustomName();

                newchest = this.type.target.makeEntity();
            }
        }

        te.updateContainingBlockInfo();

        //if (te instanceof TileEntityChest)
        //{
        //    ((TileEntityChest) te).checkForAdjacentChests();
        //}

        world.removeTileEntity(blockpos);
        world.removeBlock(blockpos);

        IBlockState iblockstate = IronChestType.get(this.type.target).with(BlockIronChest.FACING, chestFacing);

        System.out.println(iblockstate);

        world.setTileEntity(blockpos, newchest);
        world.setBlockState(blockpos, iblockstate, 3);

        world.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);

        TileEntity te2 = world.getTileEntity(blockpos);

        if (te2 instanceof TileEntityIronChest)
        {
            if (customname != null)
            {
                ((TileEntityIronChest) te2).setCustomName(customname);
            }

            ((TileEntityIronChest) te2).setItems(chestContents);
        }

        if (!entityplayer.abilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }
}
