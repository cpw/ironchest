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
package com.progwml6.ironchest.common.blocks;

import com.progwml6.ironchest.common.tileentity.DirtChestTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class DirtChestBlock extends ChestBlock
{
    public DirtChestBlock(Block.Properties properties)
    {
        super(properties, ChestType.DIRTCHEST9000);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new DirtChestTileEntity();
    }
}
