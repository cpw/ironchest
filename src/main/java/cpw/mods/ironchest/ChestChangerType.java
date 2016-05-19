/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import static cpw.mods.ironchest.IronChestType.COPPER;
import static cpw.mods.ironchest.IronChestType.CRYSTAL;
import static cpw.mods.ironchest.IronChestType.DIAMOND;
import static cpw.mods.ironchest.IronChestType.GOLD;
import static cpw.mods.ironchest.IronChestType.IRON;
import static cpw.mods.ironchest.IronChestType.OBSIDIAN;
import static cpw.mods.ironchest.IronChestType.SILVER;
import static cpw.mods.ironchest.IronChestType.WOOD;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum ChestChangerType
{
    //@formatter:off
    IRONGOLD(IRON, GOLD, "ironGoldUpgrade", "mmm", "msm", "mmm"),
    GOLDDIAMOND(GOLD, DIAMOND, "goldDiamondUpgrade", "GGG", "msm", "GGG"),
    COPPERSILVER(COPPER, SILVER, "copperSilverUpgrade", "mmm", "msm", "mmm"),
    SILVERGOLD(SILVER, GOLD, "silverGoldUpgrade", "mGm", "GsG", "mGm"),
    COPPERIRON(COPPER, IRON, "copperIronUpgrade", "mGm", "GsG", "mGm"),
    DIAMONDCRYSTAL(DIAMOND, CRYSTAL, "diamondCrystalUpgrade", "GGG", "GOG", "GGG"),
    WOODIRON(WOOD, IRON, "woodIronUpgrade", "mmm", "msm", "mmm"),
    WOODCOPPER(WOOD, COPPER, "woodCopperUpgrade", "mmm", "msm", "mmm"),
    DIAMONDOBSIDIAN(DIAMOND, OBSIDIAN, "diamondObsidianUpgrade", "mmm", "mGm", "mmm");
    //@formatter:on

    public static final ChestChangerType[] VALUES = values();

    public final IronChestType source;
    public final IronChestType target;
    public final String itemName;
    public ItemChestChanger item;
    private String[] recipe;

    ChestChangerType(IronChestType source, IronChestType target, String itemName, String... recipe)
    {
        this.source = source;
        this.target = target;
        this.itemName = itemName;
        this.recipe = recipe;
    }

    public boolean canUpgrade(IronChestType from)
    {
        return from == this.source;
    }

    public ItemChestChanger buildItem()
    {
        this.item = new ItemChestChanger(this);
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
                Object targetMaterial = IronChestType.translateOreName(targetMat);
                Object sourceMaterial = IronChestType.translateOreName(sourceMat);
                //@formatter:off
                IronChestType.addRecipe(new ItemStack(this.item), this.recipe, 'm', targetMaterial, 's', sourceMaterial, 'G', "blockGlass", 'O', Blocks.OBSIDIAN);
                //@formatter:on
            }
        }
    }

    public static void buildItems()
    {
        for (ChestChangerType type : VALUES)
        {
            type.buildItem();
        }
    }

    public static void generateRecipes()
    {
        for (ChestChangerType item : VALUES)
        {
            item.addRecipes();
        }
    }
}
