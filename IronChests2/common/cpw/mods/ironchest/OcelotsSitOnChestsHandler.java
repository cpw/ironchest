package cpw.mods.ironchest;

import java.util.List;

import net.minecraft.src.EntityAITaskEntry;
import net.minecraft.src.EntityOcelot;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpecialSpawnEvent;

public class OcelotsSitOnChestsHandler {
	@ForgeSubscribe
	public void changeSittingTaskForOcelots(LivingEvent.LivingUpdateEvent evt) {
		if (evt.entityLiving instanceof EntityOcelot && evt.entityLiving.ticksExisted < 5)
		{
			EntityOcelot ocelot = (EntityOcelot) evt.entityLiving;
			@SuppressWarnings("unchecked")
			List<EntityAITaskEntry> tasks = ocelot.tasks.field_75782_a;

			for (int i=0; i<tasks.size(); i++)
			{
				EntityAITaskEntry task = tasks.get(i);
				if (task.priority == 6 && !(task.action instanceof IronChestAIOcelotSit))
				{
					task.action = new IronChestAIOcelotSit(ocelot, 0.4F);
				}
			}
		}
	}
}
