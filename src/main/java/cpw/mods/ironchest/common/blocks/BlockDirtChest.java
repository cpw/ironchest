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

import cpw.mods.ironchest.common.tileentity.TileEntityDirtChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockDirtChest extends BlockChest
{
    public BlockDirtChest(Builder properties)
    {
        super(properties, IronChestType.DIRTCHEST9000);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void getDrops(IBlockState state, net.minecraft.util.NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune)
    {
        ItemStack stack = getItem(world, pos, state);

        stack.setTagInfo("dirtchest", new NBTTagByte((byte) 1));

        drops.add(stack);
    }

    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world)
    {
        return new TileEntityDirtChest();
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return new TileEntityDirtChest();
    }
}
