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
package com.progwml6.ironchest.common.ai;

import com.progwml6.ironchest.common.blocks.ChestBlock;
import com.progwml6.ironchest.common.tileentity.IronChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class IronChestCatSitOnBlockGoal extends CatSitOnBlockGoal
{
    public IronChestCatSitOnBlockGoal(CatEntity catEntity, float speedIn)
    {
        super(catEntity, speedIn);
    }

    /**
     * Return true to set given position as destination
     */
    @Override
    protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos)
    {
        if (!worldIn.isAirBlock(pos.up()))
        {
            return false;
        }
        else
        {
            BlockState iBlockState = worldIn.getBlockState(pos);
            Block block = iBlockState.getBlock();

            if (block instanceof ChestBlock)
            {
                return IronChestTileEntity.getPlayersUsing(worldIn, pos) < 1;
            }

            return super.shouldMoveTo(worldIn, pos);
        }
    }
}
