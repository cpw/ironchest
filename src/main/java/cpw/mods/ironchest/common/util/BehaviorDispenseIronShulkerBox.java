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
package cpw.mods.ironchest.common.util;

import cpw.mods.ironchest.common.blocks.shulker.BlockIronShulkerBox;
import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityIronShulkerBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.init.Bootstrap.BehaviorDispenseOptional;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BehaviorDispenseIronShulkerBox extends BehaviorDispenseOptional
{
    @Override
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
    {
        Block block = Block.getBlockFromItem(stack.getItem());
        World world = source.getWorld();
        EnumFacing enumfacing = source.getBlockState().getValue(BlockDispenser.FACING);
        BlockPos blockpos = source.getBlockPos().offset(enumfacing);
        this.successful = world.mayPlace(block, blockpos, false, EnumFacing.DOWN, (Entity) null);

        if (this.successful)
        {
            EnumFacing enumfacing1 = world.isAirBlock(blockpos.down()) ? enumfacing : EnumFacing.UP;
            IBlockState iblockstate = block.getDefaultState().withProperty(BlockIronShulkerBox.VARIANT_PROP, IronShulkerBoxType.VALUES[stack.getMetadata()]);
            world.setBlockState(blockpos, iblockstate);
            TileEntity tileentity = world.getTileEntity(blockpos);
            ItemStack itemstack = stack.splitStack(1);

            ((TileEntityIronShulkerBox) tileentity).setFacing(enumfacing1);

            if (itemstack.hasTagCompound())
            {
                ((TileEntityIronShulkerBox) tileentity).loadFromNbt(itemstack.getTagCompound().getCompoundTag("BlockEntityTag"));
            }

            if (itemstack.hasDisplayName())
            {
                ((TileEntityIronShulkerBox) tileentity).setCustomName(itemstack.getDisplayName());
            }

            world.updateComparatorOutputLevel(blockpos, iblockstate.getBlock());
        }

        return stack;
    }
}
