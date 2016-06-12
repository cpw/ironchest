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
package cpw.mods.ironchest;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public enum IronChestType implements IStringSerializable
{
    //@formatter:off
    IRON(54, 9, true, "ironchest.png", Arrays.asList("ingotIron", "ingotRefinedIron"), TileEntityIronChest.class, 184, 202, "mmmmPmmmm", "mGmG3GmGm"),
    GOLD(81, 9, true, "goldchest.png", Collections.singleton("ingotGold"), TileEntityGoldChest.class, 184, 256, "mmmmPmmmm", "mGmG4GmGm"),
    DIAMOND(108, 12, true, "diamondchest.png", Collections.singleton("gemDiamond"), TileEntityDiamondChest.class, 184, 256, "GGGmPmGGG", "GGGG4Gmmm"),
    COPPER(45, 9, false, "copperchest.png", Collections.singleton("ingotCopper"), TileEntityCopperChest.class, 184, 184, "mmmmCmmmm"),
    SILVER(72, 9, false, "silverchest.png", Collections.singleton("ingotSilver"), TileEntitySilverChest.class, 184, 238, "mmmm3mmmm", "mGmG0GmGm"),
    CRYSTAL(108, 12, true, "crystalchest.png", Collections.singleton("blockGlass"), TileEntityCrystalChest.class, 238, 256, "GGGGPGGGG"),
    OBSIDIAN(108, 12, false, "obsidianchest.png", Collections.singleton("obsidian"), TileEntityObsidianChest.class, 238, 256, "mmmm2mmmm"),
    DIRTCHEST9000(1, 1, false, "dirtchest.png", Collections.singleton("dirt"), TileEntityDirtChest.class, 184, 184, "mmmmCmmmm"),
    WOOD(0, 0, false, "", Collections.singleton("plankWood"), null, 0, 0);
    //@formatter:on

    public static final IronChestType VALUES[] = values();

    public final String name;
    public final int size;
    public final int rowLength;
    public final boolean tieredChest;
    public final ResourceLocation modelTexture;
    private String breakTexture;
    public final Class<? extends TileEntityIronChest> clazz;
    public final Collection<String> recipes;
    public final Collection<String> matList;
    public final int xSize;
    public final int ySize;

    //@formatter:off
    IronChestType(int size, int rowLength, boolean tieredChest, String modelTexture, Collection<String> mats, Class<? extends TileEntityIronChest> clazz, int xSize, int ySize, String... recipes)
    //@formatter:on
    {
        this.name = this.name().toLowerCase();
        this.size = size;
        this.rowLength = rowLength;
        this.tieredChest = tieredChest;
        this.modelTexture = new ResourceLocation("ironchest", "textures/model/" + modelTexture);
        this.matList = Collections.unmodifiableCollection(mats);
        this.clazz = clazz;
        this.recipes = Collections.unmodifiableCollection(Arrays.asList(recipes));
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public String getBreakTexture()
    {
        if (this.breakTexture == null)
        {
            switch (this)
            {
            case DIRTCHEST9000:
            {
                this.breakTexture = "minecraft:blocks/dirt";
                break;
            }
            case OBSIDIAN:
            {
                this.breakTexture = "minecraft:blocks/obsidian";
                break;
            }
            case WOOD:
            {
                this.breakTexture = "minecraft:blocks/planks_oak";
                break;
            }
            default:
            {
                this.breakTexture = "ironchest:blocks/" + this.getName() + "break";
            }
            }
        }

        return this.breakTexture;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    public static void registerBlocksAndRecipes(BlockIronChest blockResult)
    {
        Object previous = "chestWood";
        for (IronChestType typ : values())
        {
            generateRecipesForType(blockResult, previous, typ);
            ItemStack chest = new ItemStack(blockResult, 1, typ.ordinal());
            // if (typ.isValidForCreativeMode()) GameRegistry.registerCustomItemStack(typ.friendlyName, chest);//TODO fix this!!
            if (typ.tieredChest)
            {
                previous = chest;
            }
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
                //@formatter:off
                addRecipe(new ItemStack(blockResult, 1, type.ordinal()), recipeSplit, 'm', mainMaterial, 'P', previousTier, /* previous tier of chest */
                        'G', "blockGlass", 'C', "chestWood", '0', new ItemStack(blockResult, 1, 0), /* Iron Chest */
                        '1', new ItemStack(blockResult, 1, 1), /* Gold Chest */
                        '2', new ItemStack(blockResult, 1, 2), /* Diamond Chest */
                        '3', new ItemStack(blockResult, 1, 3), /* Copper Chest */
                        '4', new ItemStack(blockResult, 1, 4) /* Silver Chest */);
                //@formatter:on
            }
        }
    }

    public static Object translateOreName(String mat)
    {
        if (mat.equals("obsidian"))
        {
            return Blocks.OBSIDIAN;
        }
        else if (mat.equals("dirt"))
        {
            return Blocks.DIRT;
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
        return this.size / this.rowLength;
    }

    public boolean isTransparent()
    {
        return this == CRYSTAL;
    }

    public boolean isValidForCreativeMode()
    {
        return this != WOOD;
    }

    public boolean isExplosionResistant()
    {
        return this == OBSIDIAN;
    }

    public Slot makeSlot(IInventory chestInventory, int index, int x, int y)
    {
        return new ValidatingSlot(chestInventory, index, x, y, this);
    }

    private static final Item DIRT_ITEM = Item.getItemFromBlock(Blocks.DIRT);

    public boolean acceptsStack(ItemStack itemstack)
    {
        if (this == DIRTCHEST9000)
        {
            return itemstack == null || itemstack.getItem() == DIRT_ITEM;
        }

        return true;
    }

    public void adornItemDrop(ItemStack item)
    {
        if (this == DIRTCHEST9000)
        {
            item.setTagInfo("dirtchest", new NBTTagByte((byte) 1));
        }
    }

    public TileEntityIronChest makeEntity()
    {
        switch (this)
        {
        case IRON:
            return new TileEntityIronChest();
        case GOLD:
            return new TileEntityGoldChest();
        case DIAMOND:
            return new TileEntityDiamondChest();
        case COPPER:
            return new TileEntityCopperChest();
        case SILVER:
            return new TileEntitySilverChest();
        case CRYSTAL:
            return new TileEntityCrystalChest();
        case OBSIDIAN:
            return new TileEntityObsidianChest();
        case DIRTCHEST9000:
            return new TileEntityDirtChest();
        default:
            return null;
        }
    }
}