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
package cpw.mods.ironchest.common.items.shulker;

import java.util.Locale;

import cpw.mods.ironchest.common.blocks.shulker.BlockIronShulkerBox;
import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.core.IronChestCreativeTabs;
import cpw.mods.ironchest.common.items.ShulkerBoxChangerType;
import cpw.mods.ironchest.common.lib.BlockLists;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityIronShulkerBox;
import cpw.mods.ironchest.common.util.ItemTooltip;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemShulkerBoxChanger extends ItemTooltip
{
    public final ShulkerBoxChangerType type;

    public ItemShulkerBoxChanger(ShulkerBoxChangerType type)
    {
        this.type = type;

        this.setMaxStackSize(1);
        this.setUnlocalizedName("ironchest.shulker_box." + type.name().toLowerCase(Locale.US));
        this.setCreativeTab(IronChestCreativeTabs.tabIronChests);
    }

    public EnumDyeColor getColorFromTileEntity(TileEntity te, World worldIn)
    {
        if (te != null)
        {
            if (te instanceof TileEntityIronShulkerBox)
            {
                TileEntityIronShulkerBox ironShulkerBox = (TileEntityIronShulkerBox) te;

                Block ironShulkerBoxBlock = worldIn.getBlockState(ironShulkerBox.getPos()).getBlock();

                for (int i = 0; i < BlockLists.SHULKER_BLOCKS.size(); i++)
                {
                    if (BlockLists.SHULKER_BLOCKS.get(i) == ironShulkerBoxBlock)
                    {
                        return BlockLists.VANILLA_SHULKER_COLORS.get(i);
                    }
                }
            }
            else if (te instanceof TileEntityShulkerBox)
            {
                TileEntityShulkerBox shulkerBox = (TileEntityShulkerBox) te;

                Block shulkerBoxBlock = worldIn.getBlockState(shulkerBox.getPos()).getBlock();

                for (int i = 0; i < BlockLists.VANILLA_SHULKER_BLOCKS.size(); i++)
                {
                    if (BlockLists.VANILLA_SHULKER_BLOCKS.get(i) == shulkerBoxBlock)
                    {
                        return BlockLists.VANILLA_SHULKER_COLORS.get(i);
                    }
                }
            }
        }

        return EnumDyeColor.PURPLE;
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

        if (this.type.canUpgrade(IronShulkerBoxType.VANILLA))
        {
            if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockShulkerBox))
            {
                return EnumActionResult.PASS;
            }
        }
        else
        {
            if ((worldIn.getBlockState(pos).getBlock() instanceof BlockIronShulkerBox))
            {
                //@formatter:off
                if (worldIn.getBlockState(pos) != ((BlockIronShulkerBox) worldIn.getBlockState(pos).getBlock()).getStateFromMeta(IronShulkerBoxType.valueOf(this.type.source.getName().toUpperCase()).ordinal()))
                //@formatter:on
                {
                    return EnumActionResult.PASS;
                }
            }
            else
            {
                return EnumActionResult.PASS;
            }
        }

        TileEntity te = worldIn.getTileEntity(pos);

        TileEntityIronShulkerBox newShulkerBox = new TileEntityIronShulkerBox();

        NonNullList<ItemStack> shulkerBoxContents = NonNullList.<ItemStack> withSize(27, ItemStack.EMPTY);
        EnumFacing shulkerBoxFacing = EnumFacing.UP;
        EnumDyeColor shulkerBoxColor = EnumDyeColor.PURPLE;

        if (te != null)
        {
            if (te instanceof TileEntityIronShulkerBox)
            {
                shulkerBoxContents = ((TileEntityIronShulkerBox) te).getItems();
                shulkerBoxFacing = ((TileEntityIronShulkerBox) te).getFacing();
                shulkerBoxColor = getColorFromTileEntity(te, worldIn);
                ((TileEntityIronShulkerBox) te).setHasBeenUpgraded();

                newShulkerBox = this.type.target.makeEntity(shulkerBoxColor);

                if (newShulkerBox == null)
                {
                    return EnumActionResult.PASS;
                }
            }
            else if (te instanceof TileEntityShulkerBox)
            {
                IBlockState shulkerBoxState = worldIn.getBlockState(pos);
                shulkerBoxFacing = shulkerBoxState.getValue(BlockShulkerBox.FACING);
                TileEntityShulkerBox shulkerBox = (TileEntityShulkerBox) te;

                if (!this.type.canUpgrade(IronShulkerBoxType.VANILLA))
                {
                    return EnumActionResult.PASS;
                }

                shulkerBoxContents = NonNullList.<ItemStack> withSize(shulkerBox.getSizeInventory(), ItemStack.EMPTY);

                for (int i = 0; i < shulkerBoxContents.size(); i++)
                {
                    shulkerBoxContents.set(i, shulkerBox.getStackInSlot(i));
                }

                shulkerBoxColor = getColorFromTileEntity(te, worldIn);

                shulkerBox.clear();
                shulkerBox.setDestroyedByCreativePlayer(true);

                newShulkerBox = this.type.target.makeEntity(shulkerBoxColor);
            }
        }

        te.updateContainingBlockInfo();

        worldIn.setBlockToAir(pos);

        IBlockState iblockstate = null;

        if (BlockLists.SHULKER_BLOCKS.get(shulkerBoxColor.getMetadata()) != null)
        {
            Block block = BlockLists.SHULKER_BLOCKS.get(shulkerBoxColor.getMetadata());

            iblockstate = block.getDefaultState().withProperty(BlockIronShulkerBox.VARIANT_PROP, this.type.target);
        }
        else
        {
            return EnumActionResult.PASS;
        }

        worldIn.setTileEntity(pos, newShulkerBox);
        worldIn.setBlockState(pos, iblockstate, 3);

        worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

        TileEntity te2 = worldIn.getTileEntity(pos);

        if (te2 instanceof TileEntityIronShulkerBox)
        {
            ((TileEntityIronShulkerBox) te2).setContents(shulkerBoxContents);
            ((TileEntityIronShulkerBox) te2).setFacing(shulkerBoxFacing);
        }

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }
}
