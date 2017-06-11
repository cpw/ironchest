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

import cpw.mods.ironchest.common.ICContent;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityIronChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        if (!worldIn.isAirBlock(pos.up()))
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (block == ICContent.ironChestBlock)
            {
                TileEntity tileentity = worldIn.getTileEntity(pos);

                if (tileentity instanceof TileEntityIronChest && ((TileEntityIronChest) tileentity).numPlayersUsing < 1)
                {
                    return true;
                }
            }

            return super.shouldMoveTo(worldIn, pos);
        }
    }
}
