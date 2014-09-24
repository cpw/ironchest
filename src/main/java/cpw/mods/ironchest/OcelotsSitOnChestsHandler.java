package cpw.mods.ironchest;

import java.util.List;

import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/*public class OcelotsSitOnChestsHandler {
    @SubscribeEvent
    public void changeSittingTaskForOcelots(LivingEvent.LivingUpdateEvent evt)
    {
        if (evt.entityLiving.ticksExisted < 5 && evt.entityLiving instanceof EntityOcelot)
        {
            EntityOcelot ocelot = (EntityOcelot) evt.entityLiving;
            @SuppressWarnings("unchecked")
            List<EntityAITasks.EntityAITaskEntry> tasks = ocelot.tasks.taskEntries;

            for (EntityAITasks.EntityAITaskEntry task : tasks) {
                if (task.priority == 6 && (task.action instanceof EntityAIOcelotSit) && !(task.action instanceof IronChestAIOcelotSit)) {
                    task.action = new IronChestAIOcelotSit(ocelot, 0.4F);
                }
            }
        }
    }
}*/
