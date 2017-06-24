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
package cpw.mods.ironchest.common.util;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class MissingMappingsHandler
{
    @SubscribeEvent
    public void missingBlockMappings(MissingMappings<Block> event)
    {
        for (Mapping<Block> entry : event.getAllMappings())
        {
            @Nonnull
            String path = entry.key.getResourcePath();

            replaceOldChestBlock(path, entry);
        }
    }

    @SubscribeEvent
    public void missingItemMappings(MissingMappings<Item> event)
    {
        for (Mapping<Item> entry : event.getAllMappings())
        {
            @Nonnull
            String path = entry.key.getResourcePath();

            replaceOldChestItem(path, entry);

            replaceOldUpgrades(path, entry);

            replaceNewUpgrades(path, entry);
        }
    }

    private static void replaceOldChestBlock(String path, Mapping<Block> mapping)
    {
        if (path.endsWith("blockironchest"))
        {
            path = path.replace("blockironchest", "iron_chest");
            ResourceLocation newRes = new ResourceLocation(mapping.key.getResourceDomain(), path);
            Block block = ForgeRegistries.BLOCKS.getValue(newRes);

            if (block != null)
            {
                mapping.remap(block);
            }
        }
    }

    private static void replaceOldChestItem(String path, Mapping<Item> mapping)
    {
        if (path.endsWith("blockironchest"))
        {
            path = path.replace("blockironchest", "iron_chest");
            ResourceLocation newRes = new ResourceLocation(mapping.key.getResourceDomain(), path);
            Item item = ForgeRegistries.ITEMS.getValue(newRes);

            if (item != null)
            {
                mapping.remap(item);
            }
        }
    }

    private static void replaceOldUpgrades(String path, Mapping<Item> mapping)
    {
        if (path.endsWith("irongoldupgrade"))
        {
            path = path.replace("irongoldupgrade", "iron_gold_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("golddiamondupgrade"))
        {
            path = path.replace("golddiamondupgrade", "gold_diamond_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("coppersilverupgrade"))
        {
            path = path.replace("coppersilverupgrade", "copper_silver_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("silvergoldupgrade"))
        {
            path = path.replace("silvergoldupgrade", "silver_gold_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("copperironupgrade"))
        {
            path = path.replace("copperironupgrade", "copper_iron_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("diamondcrystalupgrade"))
        {
            path = path.replace("diamondcrystalupgrade", "diamond_crystal_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("woodironupgrade"))
        {
            path = path.replace("woodironupgrade", "wood_iron_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("woodcopperupgrade"))
        {
            path = path.replace("woodcopperupgrade", "wood_copper_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("diamondobsidianupgrade"))
        {
            path = path.replace("diamondobsidianupgrade", "diamond_obsidian_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }
    }

    private static void replaceNewUpgrades(String path, Mapping<Item> mapping)
    {
        if (path.endsWith("iron_gold_upgrade"))
        {
            path = path.replace("iron_gold_upgrade", "iron_gold_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("gold_diamond_upgrade"))
        {
            path = path.replace("gold_diamond_upgrade", "gold_diamond_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("copper_silver_upgrade"))
        {
            path = path.replace("copper_silver_upgrade", "copper_silver_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("silver_gold_upgrade"))
        {
            path = path.replace("silver_gold_upgrade", "silver_gold_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("copper_iron_upgrade"))
        {
            path = path.replace("copper_iron_upgrade", "copper_iron_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("diamond_crystal_upgrade"))
        {
            path = path.replace("diamond_crystal_upgrade", "diamond_crystal_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("wood_iron_upgrade"))
        {
            path = path.replace("wood_iron_upgrade", "wood_iron_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("wood_copper_upgrade"))
        {
            path = path.replace("wood_copper_upgrade", "wood_copper_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }

        if (path.endsWith("diamond_obsidian_upgrade"))
        {
            path = path.replace("diamond_obsidian_upgrade", "diamond_obsidian_chest_upgrade");
            replaceUpgradeItem(path, mapping);
        }
    }

    private static void replaceUpgradeItem(String path, Mapping<Item> mapping)
    {
        ResourceLocation newRes = new ResourceLocation(mapping.key.getResourceDomain(), path);
        Item item = ForgeRegistries.ITEMS.getValue(newRes);

        if (item != null)
        {
            mapping.remap(item);
        }
    }
}
