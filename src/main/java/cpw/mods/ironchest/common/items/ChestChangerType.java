/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest.common.items;

import static cpw.mods.ironchest.common.blocks.chest.IronChestType.COPPER;
import static cpw.mods.ironchest.common.blocks.chest.IronChestType.CRYSTAL;
import static cpw.mods.ironchest.common.blocks.chest.IronChestType.DIAMOND;
import static cpw.mods.ironchest.common.blocks.chest.IronChestType.GOLD;
import static cpw.mods.ironchest.common.blocks.chest.IronChestType.IRON;
import static cpw.mods.ironchest.common.blocks.chest.IronChestType.OBSIDIAN;
import static cpw.mods.ironchest.common.blocks.chest.IronChestType.SILVER;
import static cpw.mods.ironchest.common.blocks.chest.IronChestType.WOOD;

import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.items.chest.ItemChestChanger;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public enum ChestChangerType
{
    //@formatter:off
    IRON_GOLD(IRON, GOLD, "iron_gold_chest_upgrade"),
    GOLD_DIAMOND(GOLD, DIAMOND, "gold_diamond_chest_upgrade"),
    COPPER_SILVER(COPPER, SILVER, "copper_silver_chest_upgrade"),
    SILVER_GOLD(SILVER, GOLD, "silver_gold_chest_upgrade"),
    COPPER_IRON(COPPER, IRON, "copper_iron_chest_upgrade"),
    DIAMOND_CRYSTAL(DIAMOND, CRYSTAL, "diamond_crystal_chest_upgrade"),
    WOOD_IRON(WOOD, IRON, "wood_iron_chest_upgrade"),
    WOOD_COPPER(WOOD, COPPER, "wood_copper_chest_upgrade"),
    DIAMOND_OBSIDIAN(DIAMOND, OBSIDIAN, "diamond_obsidian_chest_upgrade");
    //@formatter:on

    public static final ChestChangerType[] VALUES = values();

    public final IronChestType source;

    public final IronChestType target;

    public final String itemName;

    public ItemChestChanger item;

    ChestChangerType(IronChestType source, IronChestType target, String itemName)
    {
        this.source = source;
        this.target = target;
        this.itemName = itemName;
    }

    public boolean canUpgrade(IronChestType from)
    {
        return from == this.source;
    }

    public ItemChestChanger buildItem(IForgeRegistry<Item> itemRegistry)
    {
        this.item = new ItemChestChanger(this);

        this.item.setRegistryName(this.itemName);

        itemRegistry.register(this.item);

        return this.item;
    }

    public static void buildItems(IForgeRegistry<Item> itemRegistry)
    {
        for (ChestChangerType type : VALUES)
        {
            type.buildItem(itemRegistry);
        }
    }
}
