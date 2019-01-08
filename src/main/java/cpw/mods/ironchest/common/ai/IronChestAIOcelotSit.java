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
package cpw.mods.ironchest.common.ai;

import cpw.mods.ironchest.common.blocks.BlockChest;
import cpw.mods.ironchest.common.tileentity.TileEntityIronChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;

public class IronChestAIOcelotSit extends EntityAIOcelotSit
{
    public IronChestAIOcelotSit(EntityOcelot ocelotIn, float speedIn)
    {
        super(ocelotIn, speedIn);
    }

    /**
     * Return true to set given position as destination
     */
    @Override
    protected boolean shouldMoveTo(IWorldReaderBase worldIn, BlockPos pos)
    {
        if (!worldIn.isAirBlock(pos.up()))
        {
            return false;
        }
        else
        {
            IBlockState iBlockState = worldIn.getBlockState(pos);
            Block block = iBlockState.getBlock();

            if (block instanceof BlockChest)
            {
                return TileEntityIronChest.getPlayersUsing(worldIn, pos) < 1;
            }

            return super.shouldMoveTo(worldIn, pos);
        }
    }
}
