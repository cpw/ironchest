package cpw.mods.ironchest;

import net.minecraft.entity.passive.EntityOcelot;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OcelotsSitOnChestsHandler
{

    @SubscribeEvent
    public void changeSittingTaskForOcelots(LivingEvent.LivingUpdateEvent evt)
    {
        if (evt.getEntityLiving().ticksExisted < 5 && evt.getEntityLiving() instanceof EntityOcelot)
        {
            // EntityOcelot ocelot = (EntityOcelot) evt.entityLiving;
            // Set<EntityAITasks.EntityAITaskEntry> tasks = ocelot.tasks.taskEntries;

            // for (EntityAITasks.EntityAITaskEntry task : tasks)
            // {
            // if (task.priority == 6 && (task.action instanceof EntityAIOcelotSit) && !(task.action instanceof IronChestAIOcelotSit))
            // {
            // task.action = new IronChestAIOcelotSit(ocelot, 0.4F);
            // }
            // }
        }
    }
}
