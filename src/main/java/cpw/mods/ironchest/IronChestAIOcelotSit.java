package cpw.mods.ironchest;

import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.world.World;

public class IronChestAIOcelotSit extends EntityAIOcelotSit {
    public IronChestAIOcelotSit(EntityOcelot par1EntityOcelot, float par2)
    {
        super(par1EntityOcelot, par2);
    }

/*    @Override
    protected boolean func_151486_a(World world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z) == IronChest.ironChestBlock)
        {
            return true;
        }
        return super.func_151486_a(world, x, y, z);
    }
*/}
