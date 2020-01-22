package com.progwml6.ironchest.common.ai;

import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;

public class CatsSitOnChestsHandler {

  @SubscribeEvent
  public void changeSittingTaskForOcelots(final LivingEvent.LivingUpdateEvent evt) {
    if (evt.getEntityLiving().ticksExisted < 5 && evt.getEntityLiving() instanceof CatEntity) {
      HashSet<PrioritizedGoal> goals = new HashSet<>();

      CatEntity catEntity = (CatEntity) evt.getEntityLiving();

      for (PrioritizedGoal goal : catEntity.goalSelector.goals) {
        if (goal.getGoal().getClass() == CatSitOnBlockGoal.class) {
          goals.add(goal);
        }
      }

      for (PrioritizedGoal goal : goals) {
        catEntity.goalSelector.removeGoal(goal.getGoal());
        catEntity.goalSelector.addGoal(goal.getPriority(), new IronChestCatSitOnBlockGoal(catEntity, 0.4F));
      }
    }
  }
}
