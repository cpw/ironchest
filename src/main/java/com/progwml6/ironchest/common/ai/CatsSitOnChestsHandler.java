package com.progwml6.ironchest.common.ai;

import com.progwml6.ironchest.IronChests;
import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;

@Mod.EventBusSubscriber(modid = IronChests.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CatsSitOnChestsHandler {

  @SubscribeEvent
  static void changeSittingTaskForOcelots(final LivingEvent.LivingUpdateEvent evt) {
    if (evt.getEntityLiving().tickCount < 5 && evt.getEntityLiving() instanceof Cat cat) {
      HashSet<WrappedGoal> goals = new HashSet<>();

      for (WrappedGoal goal : cat.goalSelector.availableGoals) {
        if (goal.getGoal().getClass() == CatSitOnBlockGoal.class) {
          goals.add(goal);
        }
      }

      for (WrappedGoal goal : goals) {
        cat.goalSelector.removeGoal(goal.getGoal());
        cat.goalSelector.addGoal(goal.getPriority(), new IronChestCatSitOnBlockGoal(cat, 0.4F));
      }
    }
  }
}
