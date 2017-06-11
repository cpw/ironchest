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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum ShulkerBoxChangerType
{
    //@formatter:off
    IRON_GOLD(IRON, GOLD, "iron_gold_shulker_upgrade", "mmm", "msm", "mmm"),
    GOLD_DIAMOND(GOLD, DIAMOND, "gold_diamond_shulker_upgrade", "GGG", "msm", "GGG"),
    COPPER_SILVER(COPPER, SILVER, "copper_silver_shulker_upgrade", "mmm", "msm", "mmm"),
    SILVER_GOLD(SILVER, GOLD, "silver_gold_shulker_upgrade", "mGm", "GsG", "mGm"),
    COPPER_IRON(COPPER, IRON, "copper_iron_shulker_upgrade", "mGm", "GsG", "mGm"),
    DIAMOND_CRYSTAL(DIAMOND, CRYSTAL, "diamond_crystal_shulker_upgrade", "GGG", "GOG", "GGG"),
    VANILLA_IRON(VANILLA, IRON, "vanilla_iron_shulker_upgrade", "mmm", "msm", "mmm"),
    VANILLA_COPPER(VANILLA, COPPER, "vanilla_copper_shulker_upgrade", "mmm", "msm", "mmm"),
    DIAMOND_OBSIDIAN(DIAMOND, OBSIDIAN, "diamond_obsidian_shulker_upgrade", "mmm", "mGm", "mmm");
    //@formatter:on

    public static final ShulkerBoxChangerType[] VALUES = values();

    public final IronShulkerBoxType source;
    public final IronShulkerBoxType target;
    public final String itemName;
    public ItemShulkerBoxChanger item;
    private String[] recipe;

    ShulkerBoxChangerType(IronShulkerBoxType source, IronShulkerBoxType target, String itemName, String... recipe)
    {
        this.source = source;
        this.target = target;
        this.itemName = itemName;
        this.recipe = recipe;
    }

    public boolean canUpgrade(IronShulkerBoxType from)
    {
        return from == this.source;
    }

    public ItemShulkerBoxChanger buildItem()
    {
        this.item = new ItemShulkerBoxChanger(this);

        this.item.setRegistryName(this.itemName);

        GameRegistry.register(this.item);

        return this.item;
    }

    public void addRecipes()
    {
        for (String sourceMat : this.source.matList)
        {
            for (String targetMat : this.target.matList)
            {
                Object targetMaterial = IronShulkerBoxType.translateOreName(targetMat);
                Object sourceMaterial = IronShulkerBoxType.translateOreName(sourceMat);

                //@formatter:off
                IronShulkerBoxType.addRecipe(new ItemStack(this.item), this.recipe, 'm', targetMaterial, 's', sourceMaterial, 'G', "blockGlass", 'O', Blocks.OBSIDIAN);
                //@formatter:on
            }
        }
    }

    public static void buildItems()
    {
        for (ShulkerBoxChangerType type : VALUES)
        {
            type.buildItem();
        }
    }

    public static void generateRecipes()
    {
        for (ShulkerBoxChangerType item : VALUES)
        {
            item.addRecipes();
        }
    }
}
