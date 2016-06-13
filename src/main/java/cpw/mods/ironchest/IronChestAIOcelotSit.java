package cpw.mods.ironchest;

import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IronChestAIOcelotSit extends EntityAIOcelotSit
{
    public IronChestAIOcelotSit(EntityOcelot ocelotIn, float speedIn)
    {
        super(ocelotIn, speedIn);
    }

    @Override
    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getBlock() == IronChest.ironChestBlock)
        {
            return true;
        }

        return super.shouldMoveTo(worldIn, pos);
    }
}
