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

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.client.renderer.chest.TileEntityIronChestRenderer;
import cpw.mods.ironchest.client.renderer.shulker.TileEntityIronShulkerBoxRenderer;
import cpw.mods.ironchest.common.blocks.chest.BlockIronChest;
import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.blocks.shulker.BlockIronShulkerBox;
import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.items.ChestChangerType;
import cpw.mods.ironchest.common.items.ShulkerBoxChangerType;
import cpw.mods.ironchest.common.items.chest.ItemIronChest;
import cpw.mods.ironchest.common.items.shulker.ItemIronShulkerBox;
import cpw.mods.ironchest.common.lib.BlockLists;
import cpw.mods.ironchest.common.util.BlockNames;
import cpw.mods.ironchest.common.util.CreativeTab;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(IronChest.MOD_ID)
public class ICContent
{
    //@formatter:off
    public static CreativeTab tabGeneral = new CreativeTab("IronChest", new ItemStack(Item.getItemFromBlock(Blocks.SLIME_BLOCK)));

    @ObjectHolder(BlockNames.IRON_CHEST)
    public static BlockIronChest ironChestBlock = new BlockIronChest();

    @ObjectHolder(BlockNames.IRON_CHEST)
    public static Item ironChestItemBlock = new ItemIronChest(ironChestBlock);

    @ObjectHolder(BlockNames.WHITE_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxWhiteBlock = new BlockIronShulkerBox(EnumDyeColor.WHITE);
    @ObjectHolder(BlockNames.ORANGE_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxOrangeBlock = new BlockIronShulkerBox(EnumDyeColor.ORANGE);
    @ObjectHolder(BlockNames.MAGENTA_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxMagentaBlock = new BlockIronShulkerBox(EnumDyeColor.MAGENTA);
    @ObjectHolder(BlockNames.LIGHT_BLUE_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxLightBlueBlock = new BlockIronShulkerBox(EnumDyeColor.LIGHT_BLUE);
    @ObjectHolder(BlockNames.YELLOW_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxYellowBlock = new BlockIronShulkerBox(EnumDyeColor.YELLOW);
    @ObjectHolder(BlockNames.LIME_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxLimeBlock = new BlockIronShulkerBox(EnumDyeColor.LIME);
    @ObjectHolder(BlockNames.PINK_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxPinkBlock = new BlockIronShulkerBox(EnumDyeColor.PINK);
    @ObjectHolder(BlockNames.GRAY_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxGrayBlock = new BlockIronShulkerBox(EnumDyeColor.GRAY);
    @ObjectHolder(BlockNames.SILVER_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxSilverBlock = new BlockIronShulkerBox(EnumDyeColor.SILVER);
    @ObjectHolder(BlockNames.CYAN_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxCyanBlock = new BlockIronShulkerBox(EnumDyeColor.CYAN);
    @ObjectHolder(BlockNames.PURPLE_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxPurpleBlock = new BlockIronShulkerBox(EnumDyeColor.PURPLE);
    @ObjectHolder(BlockNames.BLUE_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxBlueBlock = new BlockIronShulkerBox(EnumDyeColor.BLUE);
    @ObjectHolder(BlockNames.BROWN_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxBrownBlock = new BlockIronShulkerBox(EnumDyeColor.BROWN);
    @ObjectHolder(BlockNames.GREEN_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxGreenBlock = new BlockIronShulkerBox(EnumDyeColor.GREEN);
    @ObjectHolder(BlockNames.RED_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxRedBlock = new BlockIronShulkerBox(EnumDyeColor.RED);
    @ObjectHolder(BlockNames.BLACK_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxBlackBlock = new BlockIronShulkerBox(EnumDyeColor.BLACK);

    @ObjectHolder(BlockNames.WHITE_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxWhiteItemBlock = new ItemIronShulkerBox(ironShulkerBoxWhiteBlock, EnumDyeColor.WHITE);
    @ObjectHolder(BlockNames.ORANGE_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxOrangeItemBlock = new ItemIronShulkerBox(ironShulkerBoxOrangeBlock, EnumDyeColor.ORANGE);
    @ObjectHolder(BlockNames.MAGENTA_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxMagentaItemBlock = new ItemIronShulkerBox(ironShulkerBoxMagentaBlock, EnumDyeColor.MAGENTA);
    @ObjectHolder(BlockNames.LIGHT_BLUE_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxLightBlueItemBlock = new ItemIronShulkerBox(ironShulkerBoxLightBlueBlock, EnumDyeColor.LIGHT_BLUE);
    @ObjectHolder(BlockNames.YELLOW_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxYellowItemBlock = new ItemIronShulkerBox(ironShulkerBoxYellowBlock, EnumDyeColor.YELLOW);
    @ObjectHolder(BlockNames.LIME_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxLimeItemBlock = new ItemIronShulkerBox(ironShulkerBoxLimeBlock, EnumDyeColor.LIME);
    @ObjectHolder(BlockNames.PINK_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxPinkItemBlock = new ItemIronShulkerBox(ironShulkerBoxPinkBlock, EnumDyeColor.PINK);
    @ObjectHolder(BlockNames.GRAY_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxGrayItemBlock = new ItemIronShulkerBox(ironShulkerBoxGrayBlock, EnumDyeColor.GRAY);
    @ObjectHolder(BlockNames.SILVER_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxSilverItemBlock = new ItemIronShulkerBox(ironShulkerBoxSilverBlock, EnumDyeColor.SILVER);
    @ObjectHolder(BlockNames.CYAN_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxCyanItemBlock = new ItemIronShulkerBox(ironShulkerBoxCyanBlock, EnumDyeColor.CYAN);
    @ObjectHolder(BlockNames.PURPLE_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxPurpleItemBlock = new ItemIronShulkerBox(ironShulkerBoxPurpleBlock, EnumDyeColor.PURPLE);
    @ObjectHolder(BlockNames.BLUE_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxBlueItemBlock = new ItemIronShulkerBox(ironShulkerBoxBlueBlock, EnumDyeColor.BLUE);
    @ObjectHolder(BlockNames.BROWN_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxBrownItemBlock = new ItemIronShulkerBox(ironShulkerBoxBrownBlock, EnumDyeColor.BROWN);
    @ObjectHolder(BlockNames.GREEN_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxGreenItemBlock = new ItemIronShulkerBox(ironShulkerBoxGreenBlock, EnumDyeColor.GREEN);
    @ObjectHolder(BlockNames.RED_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxRedItemBlock = new ItemIronShulkerBox(ironShulkerBoxRedBlock, EnumDyeColor.RED);
    @ObjectHolder(BlockNames.BLACK_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxBlackItemBlock = new ItemIronShulkerBox(ironShulkerBoxBlackBlock, EnumDyeColor.BLACK);
    //@formatter:on

    @SubscribeEvent
    public void registerBlocks(Register<Block> event)
    {
        // Chests Start
        event.getRegistry().register(ironChestBlock);

        for (IronChestType typ : IronChestType.VALUES)
        {
            if (typ.clazz != null)
            {
                GameRegistry.registerTileEntity(typ.clazz, "IronChest." + typ.name());
            }
        }

        tabGeneral.setDisplayIcon(new ItemStack(ironChestBlock, 1, IronChestType.IRON.ordinal()));
        // Chests End

        // Shulkers Start
        BlockLists.createVanillaShulkerBlockList();

        registerShulkerBlocks(event);

        for (IronShulkerBoxType typ : IronShulkerBoxType.VALUES)
        {
            if (typ.clazz != null)
            {
                GameRegistry.registerTileEntity(typ.clazz, "IronShulkerBox." + typ.name());
            }
        }

        BlockLists.createIronShulkerBlockList();
        // Shulkers End
    }

    @SubscribeEvent
    public void registerItems(Register<Item> event)
    {
        // Chests Start
        event.getRegistry().register(ironChestItemBlock);

        ChestChangerType.buildItems(event);
        // Chests End

        // Shulkers Start
        registerShulkerItemBlocks(event);

        ShulkerBoxChangerType.buildItems(event);

        BlockLists.createShulkerItemList();

        BlockLists.registerBlockBehavior();
        // Shulkers End
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event)
    {
        // Chests Start
        Item chestItem = Item.getItemFromBlock(ICContent.ironChestBlock);

        for (IronChestType type : IronChestType.values())
        {
            if (type != IronChestType.WOOD)
            {
                //@formatter:off
                ModelLoader.setCustomModelResourceLocation(chestItem, type.ordinal(), new ModelResourceLocation(chestItem.getRegistryName(), "variant=" + type.getName()));
                //@formatter:on
            }

            ClientRegistry.bindTileEntitySpecialRenderer(type.clazz, new TileEntityIronChestRenderer());
        }

        for (ChestChangerType type : ChestChangerType.VALUES)
        {
            //@formatter:off
            ModelLoader.setCustomModelResourceLocation(type.item, 0, new ModelResourceLocation(new ResourceLocation(IronChest.MOD_ID, "iron_chest_upgrades"), "variant=" + type.itemName.toLowerCase()));
            //@formatter:on
        }
        // Chests End

        // Shulkers Start
        for (Block shulker : BlockLists.SHULKER_BLOCKS)
        {
            Item shulkerBoxItem = Item.getItemFromBlock(shulker);

            for (IronShulkerBoxType type : IronShulkerBoxType.values())
            {
                if (type != IronShulkerBoxType.VANILLA)
                {
                    //@formatter:off
                    ModelLoader.setCustomModelResourceLocation(shulkerBoxItem, type.ordinal(), new ModelResourceLocation(shulkerBoxItem.getRegistryName(), "variant=" + type.getName()));
                    //@formatter:on
                }
            }
        }

        for (IronShulkerBoxType type : IronShulkerBoxType.values())
        {
            ClientRegistry.bindTileEntitySpecialRenderer(type.clazz, new TileEntityIronShulkerBoxRenderer());
        }

        for (ShulkerBoxChangerType type : ShulkerBoxChangerType.VALUES)
        {
            //@formatter:off
            ModelLoader.setCustomModelResourceLocation(type.item, 0, new ModelResourceLocation(new ResourceLocation(IronChest.MOD_ID, "iron_shulker_box_upgrades"), "variant=" + type.itemName.toLowerCase()));
            //@formatter:on
        }
        // Shulker End
    }

    private static void registerShulkerBlocks(Register<Block> event)
    {
        event.getRegistry().registerAll(ironShulkerBoxWhiteBlock, ironShulkerBoxOrangeBlock, ironShulkerBoxMagentaBlock, ironShulkerBoxLightBlueBlock, ironShulkerBoxYellowBlock, ironShulkerBoxLimeBlock, ironShulkerBoxPinkBlock, ironShulkerBoxGrayBlock, ironShulkerBoxSilverBlock, ironShulkerBoxCyanBlock, ironShulkerBoxPurpleBlock, ironShulkerBoxBlueBlock, ironShulkerBoxBrownBlock, ironShulkerBoxGreenBlock, ironShulkerBoxRedBlock, ironShulkerBoxBlackBlock);
    }

    private static void registerShulkerItemBlocks(Register<Item> event)
    {
        event.getRegistry().registerAll(ironShulkerBoxWhiteItemBlock, ironShulkerBoxOrangeItemBlock, ironShulkerBoxMagentaItemBlock, ironShulkerBoxLightBlueItemBlock, ironShulkerBoxYellowItemBlock, ironShulkerBoxLimeItemBlock, ironShulkerBoxPinkItemBlock, ironShulkerBoxGrayItemBlock, ironShulkerBoxSilverItemBlock, ironShulkerBoxCyanItemBlock, ironShulkerBoxPurpleItemBlock, ironShulkerBoxBlueItemBlock, ironShulkerBoxBrownItemBlock, ironShulkerBoxGreenItemBlock, ironShulkerBoxRedItemBlock, ironShulkerBoxBlackItemBlock);
    }
}
