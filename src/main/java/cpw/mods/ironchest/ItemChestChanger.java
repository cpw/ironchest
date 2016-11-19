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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChestChanger extends Item
{
    public final ChestChangerType type;

    public ItemChestChanger(ChestChangerType type)
    {
        this.type = type;
        this.setMaxStackSize(1);
        this.setUnlocalizedName("ironchest:" + type.name());
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    //@formatter:off
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    //@formatter:on
    {
        if (world.isRemote)
        {
            return EnumActionResult.PASS;
        }

        if (this.type.canUpgrade(IronChestType.WOOD))
        {
            if (!(world.getBlockState(pos).getBlock() instanceof BlockChest))
            {
                return EnumActionResult.PASS;
            }
        }
        else
        {
            //@formatter:off
            if (world.getBlockState(pos) != IronChest.ironChestBlock.getStateFromMeta(IronChestType.valueOf(this.type.source.getName().toUpperCase()).ordinal()))
            //@formatter:on
            {
                return EnumActionResult.PASS;
            }
        }

        TileEntity te = world.getTileEntity(pos);
        TileEntityIronChest newchest = new TileEntityIronChest();
        NonNullList<ItemStack> chestContents = NonNullList.<ItemStack> withSize(27, ItemStack.EMPTY);
        EnumFacing chestFacing = EnumFacing.DOWN;

        if (te != null)
        {
            if (te instanceof TileEntityIronChest)
            {
                chestContents = ((TileEntityIronChest) te).getItems();
                chestFacing = ((TileEntityIronChest) te).getFacing();
                newchest = this.type.target.makeEntity();
                if (newchest == null)
                {
                    return EnumActionResult.PASS;
                }
            }
            else if (te instanceof TileEntityChest)
            {
                IBlockState chestState = world.getBlockState(pos);
                chestFacing = chestState.getValue(BlockChest.FACING);
                TileEntityChest chest = (TileEntityChest) te;

                if (chest.numPlayersUsing > 0)
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
                newchest = this.type.target.makeEntity();
            }
        }

        te.updateContainingBlockInfo();

        if (te instanceof TileEntityChest)
        {
            ((TileEntityChest) te).checkForAdjacentChests();
        }

        world.removeTileEntity(pos);
        world.setBlockToAir(pos);

        IBlockState iblockstate = IronChest.ironChestBlock.getDefaultState().withProperty(BlockIronChest.VARIANT_PROP, this.type.target);

        world.setTileEntity(pos, newchest);
        world.setBlockState(pos, iblockstate, 3);

        world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

        TileEntity te2 = world.getTileEntity(pos);

        if (te2 instanceof TileEntityIronChest)
        {
            ((TileEntityIronChest) te2).setContents(chestContents);
            ((TileEntityIronChest) te2).setFacing(chestFacing);
        }

        ItemStack stack = player.getHeldItem(hand);

        stack.setCount(player.capabilities.isCreativeMode ? stack.getCount() : stack.getCount() - 1);

        return EnumActionResult.SUCCESS;
    }
}
