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
package cpw.mods.ironchest.common.items;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.blocks.IronChestType;
import cpw.mods.ironchest.common.core.IronChestCreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import static cpw.mods.ironchest.common.blocks.IronChestType.COPPER;
import static cpw.mods.ironchest.common.blocks.IronChestType.CRYSTAL;
import static cpw.mods.ironchest.common.blocks.IronChestType.DIAMOND;
import static cpw.mods.ironchest.common.blocks.IronChestType.GOLD;
import static cpw.mods.ironchest.common.blocks.IronChestType.IRON;
import static cpw.mods.ironchest.common.blocks.IronChestType.OBSIDIAN;
import static cpw.mods.ironchest.common.blocks.IronChestType.SILVER;
import static cpw.mods.ironchest.common.blocks.IronChestType.WOOD;

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

    public final IronChestType source;

    public final IronChestType target;

    public final ResourceLocation itemName;

    ChestChangerType(IronChestType source, IronChestType target, String itemName)
    {
        this.source = source;
        this.target = target;
        this.itemName = new ResourceLocation(IronChest.MOD_ID, itemName);
    }

    public boolean canUpgrade(IronChestType from)
    {
        return from == this.source;
    }
}
