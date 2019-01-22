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
package cpw.mods.ironchest.common.core;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.items.ChestChangerType;
import cpw.mods.ironchest.common.items.ItemChestChanger;
import cpw.mods.ironchest.common.util.ItemNames;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Builder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class IronChestItems
{
    public static Builder itemBuilder;

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

            itemBuilder = (new Builder()).group(IronChestCreativeTabs.IRON_CHESTS).maxStackSize(1);

            itemRegistry.register(new ItemChestChanger(itemBuilder, ChestChangerType.IRON_GOLD));
            itemRegistry.register(new ItemChestChanger(itemBuilder, ChestChangerType.GOLD_DIAMOND));
            itemRegistry.register(new ItemChestChanger(itemBuilder, ChestChangerType.COPPER_SILVER));
            itemRegistry.register(new ItemChestChanger(itemBuilder, ChestChangerType.SILVER_GOLD));
            itemRegistry.register(new ItemChestChanger(itemBuilder, ChestChangerType.COPPER_IRON));
            itemRegistry.register(new ItemChestChanger(itemBuilder, ChestChangerType.DIAMOND_CRYSTAL));
            itemRegistry.register(new ItemChestChanger(itemBuilder, ChestChangerType.WOOD_IRON));
            itemRegistry.register(new ItemChestChanger(itemBuilder, ChestChangerType.WOOD_COPPER));
            itemRegistry.register(new ItemChestChanger(itemBuilder, ChestChangerType.DIAMOND_OBSIDIAN));
        }
    }
}
