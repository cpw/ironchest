package cpw.mods.ironchest;

import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class IronChestAIOcelotSit extends EntityAIOcelotSit
{
    public IronChestAIOcelotSit(EntityOcelot par1EntityOcelot, float par2)
    {
        super(par1EntityOcelot, par2);
    }

    @Override
    protected boolean shouldMoveTo(World world, BlockPos pos)
    {
        if (world.getBlockState(pos).getBlock() == IronChest.ironChestBlock)
        {
            return true;
        }
        return super.shouldMoveTo(world, pos);
    }
}
