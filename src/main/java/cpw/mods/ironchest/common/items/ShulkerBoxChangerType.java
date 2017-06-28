/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.common.items;

import static cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType.COPPER;
import static cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType.CRYSTAL;
import static cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType.DIAMOND;
import static cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType.GOLD;
import static cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType.IRON;
import static cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType.OBSIDIAN;
import static cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType.SILVER;
import static cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType.VANILLA;

import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.items.shulker.ItemShulkerBoxChanger;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public enum ShulkerBoxChangerType
{
    //@formatter:off
    IRON_GOLD(IRON, GOLD, "iron_gold_shulker_upgrade"),
    GOLD_DIAMOND(GOLD, DIAMOND, "gold_diamond_shulker_upgrade"),
    COPPER_SILVER(COPPER, SILVER, "copper_silver_shulker_upgrade"),
    SILVER_GOLD(SILVER, GOLD, "silver_gold_shulker_upgrade"),
    COPPER_IRON(COPPER, IRON, "copper_iron_shulker_upgrade"),
    DIAMOND_CRYSTAL(DIAMOND, CRYSTAL, "diamond_crystal_shulker_upgrade"),
    VANILLA_IRON(VANILLA, IRON, "vanilla_iron_shulker_upgrade"),
    VANILLA_COPPER(VANILLA, COPPER, "vanilla_copper_shulker_upgrade"),
    DIAMOND_OBSIDIAN(DIAMOND, OBSIDIAN, "diamond_obsidian_shulker_upgrade");
    //@formatter:on

    public static final ShulkerBoxChangerType[] VALUES = values();

    public final IronShulkerBoxType source;

    public final IronShulkerBoxType target;

    public final String itemName;

    public ItemShulkerBoxChanger item;

    ShulkerBoxChangerType(IronShulkerBoxType source, IronShulkerBoxType target, String itemName)
    {
        this.source = source;
        this.target = target;
        this.itemName = itemName;
    }

    public boolean canUpgrade(IronShulkerBoxType from)
    {
        return from == this.source;
    }

    public ItemShulkerBoxChanger buildItem(IForgeRegistry<Item> itemRegistry)
    {
        this.item = new ItemShulkerBoxChanger(this);

        this.item.setRegistryName(this.itemName);

        itemRegistry.register(this.item);

        return this.item;
    }

    public static void buildItems(IForgeRegistry<Item> itemRegistry)
    {
        for (ShulkerBoxChangerType type : VALUES)
        {
            type.buildItem(itemRegistry);
        }
    }
}
