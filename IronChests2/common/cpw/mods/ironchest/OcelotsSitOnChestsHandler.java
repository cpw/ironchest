package cpw.mods.ironchest;

import java.util.List;

import net.minecraft.src.EntityAIOcelotSit;
import net.minecraft.src.EntityAITaskEntry;
import net.minecraft.src.EntityOcelot;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;

public class OcelotsSitOnChestsHandler {
	@ForgeSubscribe
	public void changeSittingTaskForOcelots(LivingEvent.LivingUpdateEvent evt) {
		if (evt.entityLiving.ticksExisted < 5 && evt.entityLiving instanceof EntityOcelot)
		{
			EntityOcelot ocelot = (EntityOcelot) evt.entityLiving;
			@SuppressWarnings("unchecked")
			List<EntityAITaskEntry> tasks = ocelot.tasks.taskEntries;

			for (int i=0; i<tasks.size(); i++)
			{
				EntityAITaskEntry task = tasks.get(i);
				if (task.priority == 6 && (task.action instanceof EntityAIOcelotSit) && !(task.action instanceof IronChestAIOcelotSit))
				{
					task.action = new IronChestAIOcelotSit(ocelot, 0.4F);
				}
			}
		}
	}
}
