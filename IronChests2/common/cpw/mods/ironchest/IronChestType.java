/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public enum IronChestType {
    IRON(54, 9, true, "Iron Chest", "ironchest.png", 0, Arrays.asList("ingotIron", "ingotRefinedIron"), TileEntityIronChest.class, "mmmmPmmmm", "mGmG3GmGm"),
    GOLD(81, 9, true, "Gold Chest", "goldchest.png", 1, Arrays.asList("ingotGold"), TileEntityGoldChest.class, "mmmmPmmmm", "mGmG4GmGm"),
    DIAMOND(108, 12, true, "Diamond Chest", "diamondchest.png", 2, Arrays.asList("gemDiamond"), TileEntityDiamondChest.class, "GGGmPmGGG", "GGGG4Gmmm"),
    COPPER(45, 9, false, "Copper Chest", "copperchest.png", 3, Arrays.asList("ingotCopper"), TileEntityCopperChest.class, "mmmmCmmmm"),
    SILVER(72, 9, false, "Silver Chest", "silverchest.png", 4, Arrays.asList("ingotSilver"), TileEntitySilverChest.class, "mmmm3mmmm", "mGmG0GmGm"),
    CRYSTAL(108, 12, true, "Crystal Chest", "crystalchest.png", 5, Arrays.asList("blockGlass"), TileEntityCrystalChest.class, "GGGGPGGGG"),
    OBSIDIAN(108, 12, false, "Obsidian Chest", "obsidianchest.png", 6, Arrays.asList("obsidian"), TileEntityObsidianChest.class, "mmmm2mmmm"),
    WOOD(0, 0, false, "", "", -1, Arrays.asList("plankWood"), null);
    int size;
    private int rowLength;
    public String friendlyName;
    private boolean tieredChest;
    private String modelTexture;
    private int textureRow;
    public Class<? extends TileEntityIronChest> clazz;
    private String[] recipes;
    private ArrayList<String> matList;

    IronChestType(int size, int rowLength, boolean tieredChest, String friendlyName, String modelTexture, int textureRow, List<String> mats,
            Class<? extends TileEntityIronChest> clazz, String... recipes)
    {
        this.size = size;
        this.rowLength = rowLength;
        this.tieredChest = tieredChest;
        this.friendlyName = friendlyName;
        this.modelTexture = "/cpw/mods/ironchest/sprites/" + modelTexture;
        this.textureRow = textureRow;
        this.clazz = clazz;
        this.recipes = recipes;
        this.matList = new ArrayList<String>();
        matList.addAll(mats);
    }

    public String getModelTexture()
    {
        return modelTexture;
    }

    public int getTextureRow()
    {
        return textureRow;
    }

    public static TileEntityIronChest makeEntity(int metadata)
    {
        // Compatibility
        int chesttype = validateMeta(metadata);
        if (chesttype == metadata)
        {
            try
            {
                TileEntityIronChest te = values()[chesttype].clazz.newInstance();
                return te;
            }
            catch (InstantiationException e)
            {
                // unpossible
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                // unpossible
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void registerTranslations()
    {
    }

    public static void generateTieredRecipes(BlockIronChest blockResult)
    {
        ItemStack previous = new ItemStack(Block.chest);
        for (IronChestType typ : values())
        {
            generateRecipesForType(blockResult, previous, typ);
            if (typ.tieredChest) previous = new ItemStack(blockResult, 1, typ.ordinal());
        }
    }

    public static void generateRecipesForType(BlockIronChest blockResult, Object previousTier, IronChestType type)
    {
        for (String recipe : type.recipes)
        {
            String[] recipeSplit = new String[] { recipe.substring(0, 3), recipe.substring(3, 6), recipe.substring(6, 9) };
            Object mainMaterial = null;
            for (String mat : type.matList)
            {
                mainMaterial = translateOreName(mat);
                addRecipe(new ItemStack(blockResult, 1, type.ordinal()), recipeSplit,
                        'm', mainMaterial, 'P', previousTier, /* previous tier of chest */
                        'G', Block.glass, 'C', Block.chest,
                        '0', new ItemStack(blockResult, 1, 0), /* Iron Chest */
                        '1', new ItemStack(blockResult, 1, 1), /* Gold Chest */
                        '2', new ItemStack(blockResult, 1, 2), /* Diamond Chest */
                        '3', new ItemStack(blockResult, 1, 3), /* Copper Chest */
                        '4', new ItemStack(blockResult, 1, 4)/* Silver Chest */
                );
            }
        }
    }

    public static Object translateOreName(String mat)
    {
        if (mat == "ingotIron")
        {
            return Item.ingotIron;
        }
        else if (mat == "ingotGold")
        {
            return Item.ingotGold;
        }
        else if (mat == "gemDiamond")
        {
            return Item.diamond;
        }
        else if (mat == "blockGlass")
        {
            return Block.glass;
        }
        else if (mat == "obsidian")
        {
            return Block.obsidian;
        }
        return mat;
    }

    public static void addRecipe(ItemStack is, Object... parts)
    {
        ShapedOreRecipe oreRecipe = new ShapedOreRecipe(is, parts);
        GameRegistry.addRecipe(oreRecipe);
    }

    public int getRowCount()
    {
        return size / rowLength;
    }

    public int getRowLength()
    {
        return rowLength;
    }

    public boolean isTransparent()
    {
        return this == CRYSTAL;
    }

    public List<String> getMatList()
    {
        return matList;
    }

    public static int validateMeta(int i)
    {
        if (i < values().length && values()[i].size > 0)
        {
            return i;
        }
        else
        {
            return 0;
        }
    }

    public boolean isValidForCreativeMode()
    {
        return validateMeta(ordinal()) == ordinal();
    }

    public boolean isExplosionResistant()
    {
        return this == OBSIDIAN;
    }

}
