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
package com.progwml6.ironchest.common.core;

import com.progwml6.ironchest.IronChest;
import com.progwml6.ironchest.common.items.ChestChangerItem;
import com.progwml6.ironchest.common.items.ChestChangerType;
import com.progwml6.ironchest.common.util.ItemNames;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class IronChestItems
{
    public static Properties itemProperties;

    @ObjectHolder(ItemNames.IRON_GOLD_UPGRADE)
    public static Item ironToGoldUpgrade;

    @ObjectHolder(ItemNames.GOLD_DIAMOND_UPGRADE)
    public static Item goldToDiamondUpgrade;

    @ObjectHolder(ItemNames.COPPER_SILVER_UPGRADE)
    public static Item copperToSilverUpgrade;

    @ObjectHolder(ItemNames.SILVER_GOLD_UPGRADE)
    public static Item silverToGoldUpgrade;

    @ObjectHolder(ItemNames.COPPER_IRON_UPGRADE)
    public static Item copperToIronUpgrade;

    @ObjectHolder(ItemNames.DIAMOND_CRYSTAL_UPGRADE)
    public static Item diamondToCrystalUpgrade;

    @ObjectHolder(ItemNames.WOOD_IRON_UPGRADE)
    public static Item woodToIronUpgrade;

    @ObjectHolder(ItemNames.WOOD_COPPER_UPGRADE)
    public static Item woodToCopperUpgrade;

    @ObjectHolder(ItemNames.DIAMOND_OBSIDIAN_UPGRADE)
    public static Item diamondToObsidianUpgrade;

    public IronChestItems()
    {

    }

    @Mod.EventBusSubscriber(modid = IronChest.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration
    {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event)
        {
            IForgeRegistry<Item> itemRegistry = event.getRegistry();

            itemProperties = (new Properties()).group(IronChestItemGroups.IRON_CHESTS).maxStackSize(1);

            itemRegistry.register(new ChestChangerItem(itemProperties, ChestChangerType.IRON_GOLD));
            itemRegistry.register(new ChestChangerItem(itemProperties, ChestChangerType.GOLD_DIAMOND));
            itemRegistry.register(new ChestChangerItem(itemProperties, ChestChangerType.COPPER_SILVER));
            itemRegistry.register(new ChestChangerItem(itemProperties, ChestChangerType.SILVER_GOLD));
            itemRegistry.register(new ChestChangerItem(itemProperties, ChestChangerType.COPPER_IRON));
            itemRegistry.register(new ChestChangerItem(itemProperties, ChestChangerType.DIAMOND_CRYSTAL));
            itemRegistry.register(new ChestChangerItem(itemProperties, ChestChangerType.WOOD_IRON));
            itemRegistry.register(new ChestChangerItem(itemProperties, ChestChangerType.WOOD_COPPER));
            itemRegistry.register(new ChestChangerItem(itemProperties, ChestChangerType.DIAMOND_OBSIDIAN));
        }
    }
}
