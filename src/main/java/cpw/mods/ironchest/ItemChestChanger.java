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

import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChestChanger extends Item
{
    private ChestChangerType type;

    public ItemChestChanger(ChestChangerType type)
    {
        this.type = type;

        this.setMaxStackSize(1);
        this.setUnlocalizedName("ironchest:" + type.name());
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return EnumActionResult.PASS;
        }
        if (this.type.canUpgrade(IronChestType.WOOD))
        {
            if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockChest))
            {
                return EnumActionResult.PASS;
            }
        }
        else
        {
            if (worldIn.getBlockState(pos) != IronChest.ironChestBlock
                    .getStateFromMeta(IronChestType.valueOf(this.type.getSource().getName().toUpperCase()).ordinal()))
            {
                return EnumActionResult.PASS;
            }
        }
        TileEntity te = worldIn.getTileEntity(pos);
        TileEntityIronChest newchest = new TileEntityIronChest();
        ItemStack[] chestContents = new ItemStack[27];
        int chestFacing = 0;
        if (te != null)
        {
            if (te instanceof TileEntityIronChest)
            {
                chestContents = ((TileEntityIronChest) te).chestContents;
                chestFacing = ((TileEntityIronChest) te).getFacing();
                newchest = IronChestType.makeEntity(this.getTargetChestOrdinal(this.type.ordinal()));
                if (newchest == null)
                {
                    return EnumActionResult.PASS;
                }
            }
            else if (te instanceof TileEntityChest)
            {
                IBlockState chestState = worldIn.getBlockState(pos);
                EnumFacing orientation = chestState.getValue(BlockChest.FACING);
                if (orientation == EnumFacing.NORTH)
                {
                    chestFacing = 2;
                }
                if (orientation == EnumFacing.EAST)
                {
                    chestFacing = 5;
                }
                if (orientation == EnumFacing.SOUTH)
                {
                    chestFacing = 3;
                }
                if (orientation == EnumFacing.WEST)
                {
                    chestFacing = 4;
                }
                if (((TileEntityChest) te).numPlayersUsing > 0)
                {
                    return EnumActionResult.PASS;
                }
                if (!this.getType().canUpgrade(IronChestType.WOOD))
                {
                    return EnumActionResult.PASS;
                }
                chestContents = new ItemStack[((TileEntityChest) te).getSizeInventory()];
                for (int i = 0; i < chestContents.length; i++)
                {
                    chestContents[i] = ((TileEntityChest) te).getStackInSlot(i);
                }
                newchest = IronChestType.makeEntity(this.getTargetChestOrdinal(this.type.ordinal()));
            }
        }

        te.updateContainingBlockInfo();
        if (te instanceof TileEntityChest)
        {
            ((TileEntityChest) te).checkForAdjacentChests();
        }

        worldIn.removeTileEntity(pos);
        worldIn.setBlockToAir(pos);

        IBlockState iblockstate = IronChest.ironChestBlock.getStateFromMeta(newchest.getType().ordinal());

        worldIn.setTileEntity(pos, newchest);
        worldIn.setBlockState(pos, iblockstate, 3);

        worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

        TileEntity te2 = worldIn.getTileEntity(pos);
        if (te2 instanceof TileEntityIronChest)
        {
            ((TileEntityIronChest) te2).setContents(chestContents);
            ((TileEntityIronChest) te2).setFacing((byte) chestFacing);
        }

        stack.stackSize = playerIn.capabilities.isCreativeMode ? stack.stackSize : stack.stackSize - 1;
        return EnumActionResult.SUCCESS;
    }

    public int getTargetChestOrdinal(int sourceOrdinal)
    {
        return this.type.getTarget();
    }

    public ChestChangerType getType()
    {
        return this.type;
    }
}
