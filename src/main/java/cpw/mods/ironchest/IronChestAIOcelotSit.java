package cpw.mods.ironchest;

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

            if (block == IronChest.ironChestBlock)
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
