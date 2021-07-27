package com.progwml6.ironchest.common.ai;

import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;

public class CatsSitOnChestsHandler {

  @SubscribeEvent
  public void changeSittingTaskForOcelots(final LivingEvent.LivingUpdateEvent evt) {
    if (evt.getEntityLiving().tickCount < 5 && evt.getEntityLiving() instanceof Cat) {
      HashSet<WrappedGoal> goals = new HashSet<>();

      Cat catEntity = (Cat) evt.getEntityLiving();

      for (WrappedGoal goal : catEntity.goalSelector.availableGoals) {
        if (goal.getGoal().getClass() == CatSitOnBlockGoal.class) {
          goals.add(goal);
        }
      }

      for (WrappedGoal goal : goals) {
        catEntity.goalSelector.removeGoal(goal.getGoal());
        catEntity.goalSelector.addGoal(goal.getPriority(), new IronChestCatSitOnBlockGoal(catEntity, 0.4F));
      }
    }
  }
}
