package cpw.mods.ironchest.common.lib;

import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.ironchest.common.core.IronChestBlocks;
import cpw.mods.ironchest.common.util.BehaviorDispenseIronShulkerBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;

public class BlockLists
{
    //@formatter:off
    public static final List<Block> SHULKER_BLOCKS = Lists.newArrayList();
    public static final List<ItemBlock> SHULKER_ITEM_BLOCKS = Lists.newArrayList();

    public static final List<Block> VANILLA_SHULKER_BLOCKS = Lists.newArrayList();
    public static final List<EnumDyeColor> VANILLA_SHULKER_COLORS = Lists.newArrayList();
    //@formatter:on

    public static void createVanillaShulkerBlockList()
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

    public static void createIronShulkerBlockList()
    {
        BlockLists.createVanillaShulkerBlockList();

        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxWhiteBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxOrangeBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxMagentaBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxLightBlueBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxYellowBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxLimeBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxPinkBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxGrayBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxSilverBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxCyanBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxPurpleBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxBlueBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxBrownBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxGreenBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxRedBlock);
        SHULKER_BLOCKS.add(IronChestBlocks.ironShulkerBoxBlackBlock);
    }

    public static void createShulkerItemList()
    {
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxWhiteItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxOrangeItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxMagentaItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxLightBlueItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxYellowItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxLimeItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxPinkItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxGrayItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxSilverItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxCyanItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxPurpleItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxBlueItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxBrownItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxGreenItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxRedItemBlock);
        SHULKER_ITEM_BLOCKS.add(IronChestBlocks.ironShulkerBoxBlackItemBlock);

        BlockLists.registerBlockBehavior();
    }

    public static void registerBlockBehavior()
    {
        for (ItemBlock block : SHULKER_ITEM_BLOCKS)
        {
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(block, new BehaviorDispenseIronShulkerBox());
        }
    }
}
