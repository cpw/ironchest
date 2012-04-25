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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;

public enum IronChestType {
  IRON(54, 9, true, "Iron Chest", "ironchest.png", 0, Item.ingotIron, TileEntityIronChest.class, "mmmmPmmmm", "mGmG3GmGm"),
  GOLD(81, 9, true, "Gold Chest", "goldchest.png", 1, Item.ingotGold, TileEntityGoldChest.class, "mmmmPmmmm", "mGmG4GmGm"),
  DIAMOND(108, 12, true, "Diamond Chest", "diamondchest.png", 2, Item.diamond, TileEntityDiamondChest.class, "GGGmPmGGG", "GGGG4Gmmm"),
  COPPER(45, 9, false, "Copper Chest", "copperchest.png", 3, null, TileEntityCopperChest.class, "mmmmCmmmm"),
  SILVER(72, 9, false, "Silver Chest", "silverchest.png", 4, null, TileEntitySilverChest.class, "mmmm0mmmm", "mGmG3GmGm"),
  CRYSTAL(108, 12, true, "Crystal Chest", "crystalchest.png", 5, Item.itemsList[Block.glass.blockID], TileEntityCrystalChest.class, "GGGGPGGGG");
  int size;
  private int rowLength;
  public String friendlyName;
  private boolean tieredChest;
  private String modelTexture;
  private int textureRow;
  public Class<? extends TileEntityIronChest> clazz;
  Item mat;
  private String[] recipes;
  private ArrayList<ItemStack> matList;

  IronChestType(int size, int rowLength, boolean tieredChest, String friendlyName, String modelTexture, int textureRow, Item mat,
      Class<? extends TileEntityIronChest> clazz, String... recipes) {
    this.size = size;
    this.rowLength = rowLength;
    this.tieredChest = tieredChest;
    this.friendlyName = friendlyName;
    this.modelTexture = "/cpw/mods/ironchest/sprites/" + modelTexture;
    this.textureRow = textureRow;
    this.clazz = clazz;
    this.mat = mat;
    this.recipes = recipes;
    this.matList = new ArrayList<ItemStack>();
    if (mat != null) {
      matList.add(new ItemStack(mat));
    }
  }

  public String getModelTexture() {
    return modelTexture;
  }

  public int getTextureRow() {
    return textureRow;
  }

  public static TileEntityIronChest makeEntity(int metadata) {
    // Compatibility
    int chesttype = metadata;
    try {
      TileEntityIronChest te = values()[chesttype].clazz.newInstance();
      return te;
    } catch (InstantiationException e) {
      // unpossible
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // unpossible
      e.printStackTrace();
    }
    return null;
  }

  public static void registerTranslations() {
  }

  public static void generateTieredRecipies(BlockIronChest blockResult) {
    ItemStack previous = new ItemStack(Block.chest);
    for (IronChestType typ : values()) {
      if (!typ.tieredChest)
        continue;
      generateRecipesForType(blockResult, previous, typ, typ.mat);
      previous = new ItemStack(blockResult, 1, typ.ordinal());
    }
  }

  public static void generateRecipesForType(BlockIronChest blockResult, Object previousTier, IronChestType type, Object mat) {
    for (String recipe : type.recipes) {
      String[] recipeSplit = new String[] { recipe.substring(0, 3), recipe.substring(3, 6), recipe.substring(6, 9) };
      addRecipe(new ItemStack(blockResult, 1, type.ordinal()), recipeSplit, 'm', mat, 'P', previousTier, 'G', Block.glass, 'C', Block.chest,
          '0', new ItemStack(blockResult, 1, 0)/* Iron */, '1', new ItemStack(blockResult, 1, 1)/* GOLD */, '3', new ItemStack(blockResult,
              1, 3)/* Copper */, '4', new ItemStack(blockResult, 1, 4));
    }
  }

  public static void addRecipe(ItemStack is, Object... parts) {
    ModLoader.addRecipe(is, parts);
  }

  public int getRowCount() {
    return size / rowLength;
  }

  public int getRowLength() {
    return rowLength;
  }

  public void addMat(ItemStack ore) {
    this.matList.add(ore);
  }

  public List<ItemStack> getMatList() {
    return matList;
  }

  public boolean isTransparent() {
    return this == CRYSTAL;
  }

}
