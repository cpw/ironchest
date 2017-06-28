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
package cpw.mods.ironchest.common.items.chest;

import java.util.Locale;

import cpw.mods.ironchest.common.blocks.chest.BlockIronChest;
import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.core.IronChestBlocks;
import cpw.mods.ironchest.common.core.IronChestCreativeTabs;
import cpw.mods.ironchest.common.items.ChestChangerType;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityIronChest;
import cpw.mods.ironchest.common.util.ItemTooltip;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChestChanger extends ItemTooltip
{
    public final ChestChangerType type;

    public ItemChestChanger(ChestChangerType type)
    {
        this.type = type;
        this.setMaxStackSize(1);
        this.setUnlocalizedName("ironchest.chest." + type.name().toLowerCase(Locale.US));
        this.setCreativeTab(IronChestCreativeTabs.tabIronChests);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    @Override
    //@formatter:off
    public EnumActionResult onItemUseFirst(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    //@formatter:on
    {
        ItemStack itemstack = playerIn.getHeldItem(hand);

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
            //@formatter:off
            if (worldIn.getBlockState(pos) != IronChestBlocks.ironChestBlock.getStateFromMeta(IronChestType.valueOf(this.type.source.getName().toUpperCase()).ordinal()))
            //@formatter:on
            {
                return EnumActionResult.PASS;
            }
        }

        TileEntity te = worldIn.getTileEntity(pos);
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
                IBlockState chestState = worldIn.getBlockState(pos);
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

        worldIn.removeTileEntity(pos);
        worldIn.setBlockToAir(pos);

        IBlockState iblockstate = IronChestBlocks.ironChestBlock.getDefaultState().withProperty(BlockIronChest.VARIANT_PROP, this.type.target);

        worldIn.setTileEntity(pos, newchest);
        worldIn.setBlockState(pos, iblockstate, 3);

        worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

        TileEntity te2 = worldIn.getTileEntity(pos);

        if (te2 instanceof TileEntityIronChest)
        {
            ((TileEntityIronChest) te2).setContents(chestContents);
            ((TileEntityIronChest) te2).setFacing(chestFacing);
        }

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }
}
