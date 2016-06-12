package cpw.mods.ironchest;

import java.util.HashSet;

import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OcelotsSitOnChestsHandler
{
    @SubscribeEvent
    public void changeSittingTaskForOcelots(LivingUpdateEvent evt)
    {
        if (evt.getEntityLiving().ticksExisted < 5 && evt.getEntityLiving() instanceof EntityOcelot)
        {
            HashSet<EntityAITaskEntry> hashset = new HashSet<EntityAITaskEntry>();

            EntityOcelot ocelot = (EntityOcelot) evt.getEntityLiving();

            for (EntityAITaskEntry task : ocelot.tasks.taskEntries)
            {
                if (task.action.getClass() == EntityAIOcelotSit.class)
                {
                    hashset.add(task);
                }
            }

            for (EntityAITaskEntry task : hashset)
            {
                ocelot.tasks.removeTask(task.action);
                ocelot.tasks.addTask(task.priority, new IronChestAIOcelotSit(ocelot, 0.4F));
            }
        }
    }
}
