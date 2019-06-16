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
package com.progwml6.ironchest.common.inventory;

import com.progwml6.ironchest.IronChest;
import com.progwml6.ironchest.client.inventory.ChestScreen;
import com.progwml6.ironchest.common.util.ContainerNames;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

public class ChestContainerType
{
    @ObjectHolder(ContainerNames.IRON_CHEST)
    public static ContainerType<ChestContainer> IRON_CHEST;

    @ObjectHolder(ContainerNames.GOLD_CHEST)
    public static ContainerType<ChestContainer> GOLD_CHEST;

    @ObjectHolder(ContainerNames.DIAMOND_CHEST)
    public static ContainerType<ChestContainer> DIAMOND_CHEST;

    @ObjectHolder(ContainerNames.CRYSTAL_CHEST)
    public static ContainerType<ChestContainer> CRYSTAL_CHEST;

    @ObjectHolder(ContainerNames.COPPER_CHEST)
    public static ContainerType<ChestContainer> COPPER_CHEST;

    @ObjectHolder(ContainerNames.SILVER_CHEST)
    public static ContainerType<ChestContainer> SILVER_CHEST;

    @ObjectHolder(ContainerNames.OBSIDIAN_CHEST)
    public static ContainerType<ChestContainer> OBSIDIAN_CHEST;

    @ObjectHolder(ContainerNames.DIRT_CHEST)
    public static ContainerType<ChestContainer> DIRT_CHEST;
    
    @Mod.EventBusSubscriber(modid = IronChest.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration
    {
        @SubscribeEvent
        public static void onContainerTypeRegistry(final RegistryEvent.Register<ContainerType<?>> e)
        {
            e.getRegistry().registerAll(
                    new ContainerType<>(ChestContainer::createIronContainer).setRegistryName(ContainerNames.IRON_CHEST),
                    new ContainerType<>(ChestContainer::createGoldContainer).setRegistryName(ContainerNames.GOLD_CHEST),
                    new ContainerType<>(ChestContainer::createDiamondContainer).setRegistryName(ContainerNames.DIAMOND_CHEST),
                    new ContainerType<>(ChestContainer::createCrystalContainer).setRegistryName(ContainerNames.CRYSTAL_CHEST),
                    new ContainerType<>(ChestContainer::createCopperContainer).setRegistryName(ContainerNames.COPPER_CHEST),
                    new ContainerType<>(ChestContainer::createSilverContainer).setRegistryName(ContainerNames.SILVER_CHEST),
                    new ContainerType<>(ChestContainer::createObsidianContainer).setRegistryName(ContainerNames.OBSIDIAN_CHEST),
                    new ContainerType<>(ChestContainer::createDirtContainer).setRegistryName(ContainerNames.DIRT_CHEST)
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreenFactories()
    {
        ScreenManager.registerFactory(IRON_CHEST, ChestScreen::new);
        ScreenManager.registerFactory(GOLD_CHEST, ChestScreen::new);
        ScreenManager.registerFactory(DIAMOND_CHEST, ChestScreen::new);
        ScreenManager.registerFactory(CRYSTAL_CHEST, ChestScreen::new);
        ScreenManager.registerFactory(COPPER_CHEST, ChestScreen::new);
        ScreenManager.registerFactory(SILVER_CHEST, ChestScreen::new);
        ScreenManager.registerFactory(OBSIDIAN_CHEST, ChestScreen::new);
        ScreenManager.registerFactory(DIRT_CHEST, ChestScreen::new);
    }
}
