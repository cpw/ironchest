/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors:
 * cpw - initial API and implementation
 ******************************************************************************/
package com.progwml6.ironchest.common.ai;

import net.minecraft.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;

public class CatsSitOnChestsHandler
{
    @SubscribeEvent
    public void changeSittingTaskForOcelots(final LivingEvent.LivingUpdateEvent evt)
    {
        /*
        if (evt.getEntityLiving().ticksExisted < 5 && evt.getEntityLiving() instanceof CatEntity)
        {
            HashSet<PrioritizedGoal> goals = new HashSet<>();

            CatEntity catEntity = (CatEntity) evt.getEntityLiving();

            for (PrioritizedGoal goal : catEntity.goalSelector.goals)
            {
                if (goal.func_220772_j().getClass() == CatSitOnBlockGoal.class)
                {
                    goals.add(goal);
                }
            }

            for (PrioritizedGoal goal : goals)
            {
                catEntity.goalSelector.removeGoal(goal.func_220772_j());
                catEntity.goalSelector.addGoal(goal.getPriority(), new IronChestCatSitOnBlockGoal1(catEntity, 0.4F));
            }
        }*/
    }
}
