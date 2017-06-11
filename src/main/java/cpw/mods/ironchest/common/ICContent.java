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
package cpw.mods.ironchest.common;

import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.blocks.chest.BlockIronChest;
import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.blocks.shulker.BlockIronShulkerBox;
import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.crafting.IronShulkerBoxColoring;
import cpw.mods.ironchest.common.items.ChestChangerType;
import cpw.mods.ironchest.common.items.ShulkerBoxChangerType;
import cpw.mods.ironchest.common.items.chest.ItemIronChest;
import cpw.mods.ironchest.common.items.shulker.ItemIronShulkerBox;
import cpw.mods.ironchest.common.util.BehaviorDispenseIronShulkerBox;
import cpw.mods.ironchest.common.util.CreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class ICContent
{
    public static CreativeTab tabGeneral = new CreativeTab("IronChest", new ItemStack(Item.getItemFromBlock(Blocks.SLIME_BLOCK)));

    public static BlockIronChest ironChestBlock;
    public static ItemIronChest ironChestItemBlock;

    public static BlockIronShulkerBox ironShulkerBoxWhiteBlock;
    public static BlockIronShulkerBox ironShulkerBoxOrangeBlock;
    public static BlockIronShulkerBox ironShulkerBoxMagentaBlock;
    public static BlockIronShulkerBox ironShulkerBoxLightBlueBlock;
    public static BlockIronShulkerBox ironShulkerBoxYellowBlock;
    public static BlockIronShulkerBox ironShulkerBoxLimeBlock;
    public static BlockIronShulkerBox ironShulkerBoxPinkBlock;
    public static BlockIronShulkerBox ironShulkerBoxGrayBlock;
    public static BlockIronShulkerBox ironShulkerBoxSilverBlock;
    public static BlockIronShulkerBox ironShulkerBoxCyanBlock;
    public static BlockIronShulkerBox ironShulkerBoxPurpleBlock;
    public static BlockIronShulkerBox ironShulkerBoxBlueBlock;
    public static BlockIronShulkerBox ironShulkerBoxBrownBlock;
    public static BlockIronShulkerBox ironShulkerBoxGreenBlock;
    public static BlockIronShulkerBox ironShulkerBoxRedBlock;
    public static BlockIronShulkerBox ironShulkerBoxBlackBlock;

    public static ItemIronShulkerBox ironShulkerBoxWhiteItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxOrangeItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxMagentaItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxLightBlueItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxYellowItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxLimeItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxPinkItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxGrayItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxSilverItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxCyanItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxPurpleItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxBlueItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxBrownItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxGreenItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxRedItemBlock;
    public static ItemIronShulkerBox ironShulkerBoxBlackItemBlock;

    public static final List<Block> SHULKER_BLOCKS = Lists.newArrayList();
    public static final List<ItemBlock> SHULKER_ITEM_BLOCKS = Lists.newArrayList();

    public static final List<Block> VANILLA_SHULKER_BLOCKS = Lists.newArrayList();
    public static final List<EnumDyeColor> VANILLA_SHULKER_COLORS = Lists.newArrayList();

    public static void preInit()
    {
        // Chests Start
        ChestChangerType.buildItems();

        ironChestBlock = GameRegistry.register(new BlockIronChest());
        ironChestItemBlock = GameRegistry.register(new ItemIronChest(ironChestBlock));

        for (IronChestType typ : IronChestType.VALUES)
        {
            if (typ.clazz != null)
            {
                GameRegistry.registerTileEntity(typ.clazz, "IronChest." + typ.name());
            }
        }

        IronChestType.registerBlocksAndRecipes(ironChestBlock);
        ChestChangerType.generateRecipes();
        // Chests End

        // Shulkers Start
        setVanillaShulkerList();

        ShulkerBoxChangerType.buildItems();

        registerShulkerBlocks();

        registerShulkerItemBlocks();

        for (IronShulkerBoxType typ : IronShulkerBoxType.VALUES)
        {
            if (typ.clazz != null)
            {
                GameRegistry.registerTileEntity(typ.clazz, "IronShulkerBox." + typ.name());
            }
        }

        for (int i = 0; i < ICContent.SHULKER_BLOCKS.size(); i++)
        {
            IronShulkerBoxType.registerBlocksAndRecipes((BlockIronShulkerBox) SHULKER_BLOCKS.get(i), (BlockShulkerBox) VANILLA_SHULKER_BLOCKS.get(i));
        }

        for (ItemBlock block : SHULKER_ITEM_BLOCKS)
        {
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(block, new BehaviorDispenseIronShulkerBox());
        }

        GameRegistry.addRecipe(new IronShulkerBoxColoring());
        RecipeSorter.register(IronChest.MOD_ID, IronShulkerBoxColoring.class, Category.SHAPELESS, "after:forge:shapelessore");

        ShulkerBoxChangerType.generateRecipes();
        // Shulkers End

        tabGeneral.setDisplayIcon(new ItemStack(ironChestBlock, 1, IronChestType.IRON.ordinal()));
    }

    private static void registerShulkerBlocks()
    {
        SHULKER_BLOCKS.add(ironShulkerBoxWhiteBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.WHITE)));
        SHULKER_BLOCKS.add(ironShulkerBoxOrangeBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.ORANGE)));
        SHULKER_BLOCKS.add(ironShulkerBoxMagentaBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.MAGENTA)));
        SHULKER_BLOCKS.add(ironShulkerBoxLightBlueBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.LIGHT_BLUE)));
        SHULKER_BLOCKS.add(ironShulkerBoxYellowBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.YELLOW)));
        SHULKER_BLOCKS.add(ironShulkerBoxLimeBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.LIME)));
        SHULKER_BLOCKS.add(ironShulkerBoxPinkBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.PINK)));
        SHULKER_BLOCKS.add(ironShulkerBoxGrayBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.GRAY)));
        SHULKER_BLOCKS.add(ironShulkerBoxSilverBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.SILVER)));
        SHULKER_BLOCKS.add(ironShulkerBoxCyanBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.CYAN)));
        SHULKER_BLOCKS.add(ironShulkerBoxPurpleBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.PURPLE)));
        SHULKER_BLOCKS.add(ironShulkerBoxBlueBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.BLUE)));
        SHULKER_BLOCKS.add(ironShulkerBoxBrownBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.BROWN)));
        SHULKER_BLOCKS.add(ironShulkerBoxGreenBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.GREEN)));
        SHULKER_BLOCKS.add(ironShulkerBoxRedBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.RED)));
        SHULKER_BLOCKS.add(ironShulkerBoxBlackBlock = GameRegistry.register(new BlockIronShulkerBox(EnumDyeColor.BLACK)));
    }

    private static void registerShulkerItemBlocks()
    {
        //@formatter:off
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxWhiteItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxWhiteBlock, EnumDyeColor.WHITE)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxOrangeItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxOrangeBlock, EnumDyeColor.ORANGE)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxMagentaItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxMagentaBlock, EnumDyeColor.MAGENTA)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxLightBlueItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxLightBlueBlock, EnumDyeColor.LIGHT_BLUE)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxYellowItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxYellowBlock, EnumDyeColor.YELLOW)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxLimeItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxLimeBlock, EnumDyeColor.LIME)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxPinkItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxPinkBlock, EnumDyeColor.PINK)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxGrayItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxGrayBlock, EnumDyeColor.GRAY)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxSilverItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxSilverBlock, EnumDyeColor.SILVER)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxCyanItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxCyanBlock, EnumDyeColor.CYAN)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxPurpleItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxPurpleBlock, EnumDyeColor.PURPLE)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxBlueItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxBlueBlock, EnumDyeColor.BLUE)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxBrownItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxBrownBlock, EnumDyeColor.BROWN)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxGreenItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxGreenBlock, EnumDyeColor.GREEN)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxRedItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxRedBlock, EnumDyeColor.RED)));
        SHULKER_ITEM_BLOCKS.add(ironShulkerBoxBlackItemBlock = GameRegistry.register(new ItemIronShulkerBox(ironShulkerBoxBlackBlock, EnumDyeColor.BLACK)));
        //@formatter:on
    }

    private static void setVanillaShulkerList()
    {
        VANILLA_SHULKER_BLOCKS.add(Blocks.WHITE_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.ORANGE_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.MAGENTA_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.LIGHT_BLUE_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.YELLOW_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.LIME_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.PINK_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.GRAY_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.SILVER_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.CYAN_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.PURPLE_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.BLUE_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.BROWN_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.GREEN_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.RED_SHULKER_BOX);
        VANILLA_SHULKER_BLOCKS.add(Blocks.BLACK_SHULKER_BOX);

        VANILLA_SHULKER_COLORS.add(EnumDyeColor.WHITE);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.ORANGE);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.MAGENTA);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.LIGHT_BLUE);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.YELLOW);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.LIME);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.PINK);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.GRAY);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.SILVER);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.CYAN);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.PURPLE);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.BLUE);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.BROWN);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.GREEN);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.RED);
        VANILLA_SHULKER_COLORS.add(EnumDyeColor.BLACK);
    }
}
