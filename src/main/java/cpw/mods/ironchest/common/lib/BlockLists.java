package cpw.mods.ironchest.common.lib;

import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.ironchest.common.ICContent;
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
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxWhiteBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxOrangeBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxMagentaBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxLightBlueBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxYellowBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxLimeBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxPinkBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxGrayBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxSilverBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxCyanBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxPurpleBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxBlueBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxBrownBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxGreenBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxRedBlock);
        SHULKER_BLOCKS.add(ICContent.ironShulkerBoxBlackBlock);
    }

    public static void createShulkerItemList()
    {
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxWhiteItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxOrangeItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxMagentaItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxLightBlueItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxYellowItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxLimeItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxPinkItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxGrayItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxSilverItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxCyanItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxPurpleItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxBlueItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxBrownItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxGreenItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxRedItemBlock);
        SHULKER_ITEM_BLOCKS.add(ICContent.ironShulkerBoxBlackItemBlock);
    }

    public static void registerBlockBehavior()
    {
        for (ItemBlock block : SHULKER_ITEM_BLOCKS)
        {
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(block, new BehaviorDispenseIronShulkerBox());
        }
    }
}
