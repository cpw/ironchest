package cpw.mods.ironchest;

import java.util.List;

import cpw.mods.fml.common.ReflectionHelper;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityAIOcelotSit;
import net.minecraft.src.EntityAITasks;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityOcelot;
import net.minecraft.src.World;
import net.minecraft.src.forge.IEntityLivingHandler;
import net.minecraft.src.forge.adaptors.EntityLivingHandlerAdaptor;

public class OcelotsSitOnChestsHandler extends EntityLivingHandlerAdaptor {
  private static EntityAIOcelotSit aiTask = new EntityAIOcelotSit(null, 0);
  @Override
  public boolean onEntityLivingUpdate(EntityLiving entity) {
    if (entity.ticksExisted<2 && entity instanceof EntityOcelot) {
      EntityOcelot ocelot = (EntityOcelot) entity;
      EntityAITasks ocelotTasks = ReflectionHelper.getPrivateValue(EntityLiving.class, ocelot, "tasks");
      List taskList = ReflectionHelper.getPrivateValue(EntityAITasks.class, ocelotTasks, "tasksToDo");
      taskList.remove(5);
      ocelotTasks.addTask(6, new IronChestAIOcelotSit(ocelot, 0.4F, aiTask));
    }
    return false;
  }
}
