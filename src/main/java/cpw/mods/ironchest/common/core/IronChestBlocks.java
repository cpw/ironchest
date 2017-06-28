package cpw.mods.ironchest.common.core;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.blocks.chest.BlockIronChest;
import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.blocks.shulker.BlockIronShulkerBox;
import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.items.chest.ItemIronChest;
import cpw.mods.ironchest.common.items.shulker.ItemIronShulkerBox;
import cpw.mods.ironchest.common.lib.BlockLists;
import cpw.mods.ironchest.common.util.BlockNames;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

public class IronChestBlocks
{
    @ObjectHolder(BlockNames.IRON_CHEST)
    public static BlockIronChest ironChestBlock;

    @ObjectHolder(BlockNames.IRON_CHEST)
    public static Item ironChestItemBlock;

    //@formatter:off
    @ObjectHolder(BlockNames.WHITE_SHULKER)
    public static final BlockIronShulkerBox ironShulkerBoxWhiteBlock = null;
    @ObjectHolder(BlockNames.ORANGE_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxOrangeBlock;
    @ObjectHolder(BlockNames.MAGENTA_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxMagentaBlock;
    @ObjectHolder(BlockNames.LIGHT_BLUE_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxLightBlueBlock;
    @ObjectHolder(BlockNames.YELLOW_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxYellowBlock;
    @ObjectHolder(BlockNames.LIME_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxLimeBlock;
    @ObjectHolder(BlockNames.PINK_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxPinkBlock;
    @ObjectHolder(BlockNames.GRAY_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxGrayBlock;
    @ObjectHolder(BlockNames.SILVER_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxSilverBlock;
    @ObjectHolder(BlockNames.CYAN_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxCyanBlock;
    @ObjectHolder(BlockNames.PURPLE_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxPurpleBlock;
    @ObjectHolder(BlockNames.BLUE_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxBlueBlock;
    @ObjectHolder(BlockNames.BROWN_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxBrownBlock;
    @ObjectHolder(BlockNames.GREEN_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxGreenBlock;
    @ObjectHolder(BlockNames.RED_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxRedBlock;
    @ObjectHolder(BlockNames.BLACK_SHULKER)
    public static BlockIronShulkerBox ironShulkerBoxBlackBlock;

    @ObjectHolder(BlockNames.WHITE_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxWhiteItemBlock;
    @ObjectHolder(BlockNames.ORANGE_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxOrangeItemBlock;
    @ObjectHolder(BlockNames.MAGENTA_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxMagentaItemBlock;
    @ObjectHolder(BlockNames.LIGHT_BLUE_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxLightBlueItemBlock;
    @ObjectHolder(BlockNames.YELLOW_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxYellowItemBlock;
    @ObjectHolder(BlockNames.LIME_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxLimeItemBlock;
    @ObjectHolder(BlockNames.PINK_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxPinkItemBlock;
    @ObjectHolder(BlockNames.GRAY_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxGrayItemBlock;
    @ObjectHolder(BlockNames.SILVER_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxSilverItemBlock;
    @ObjectHolder(BlockNames.CYAN_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxCyanItemBlock;
    @ObjectHolder(BlockNames.PURPLE_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxPurpleItemBlock;
    @ObjectHolder(BlockNames.BLUE_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxBlueItemBlock;
    @ObjectHolder(BlockNames.BROWN_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxBrownItemBlock;
    @ObjectHolder(BlockNames.GREEN_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxGreenItemBlock;
    @ObjectHolder(BlockNames.RED_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxRedItemBlock;
    @ObjectHolder(BlockNames.BLACK_SHULKER)
    public static ItemIronShulkerBox ironShulkerBoxBlackItemBlock;
    //@formatter:on

    @EventBusSubscriber(modid = IronChest.MOD_ID)
    public static class Registration
    {
        @SubscribeEvent
        public static void registerBlocks(Register<Block> event)
        {
            IForgeRegistry<Block> blockRegistry = event.getRegistry();

            // Chest Start
            blockRegistry.register(new BlockIronChest());

            for (IronChestType typ : IronChestType.VALUES)
            {
                if (typ.clazz != null)
                {
                    GameRegistry.registerTileEntity(typ.clazz, "IronChest." + typ.name());
                }
            }
            // Chest End

            // Shulker Start
            for (EnumDyeColor color : EnumDyeColor.values())
            {
                blockRegistry.register(new BlockIronShulkerBox(color, BlockNames.SHULKER_NAMES[color.getMetadata()]));
            }

            for (IronShulkerBoxType typ : IronShulkerBoxType.VALUES)
            {
                if (typ.clazz != null)
                {
                    GameRegistry.registerTileEntity(typ.clazz, "IronShulkerBox." + typ.name());
                }
            }
            // Shulker End
        }

        @SubscribeEvent
        public static void registerItems(Register<Item> event)
        {
            BlockLists.createIronShulkerBlockList();

            IForgeRegistry<Item> itemRegistry = event.getRegistry();

            // Chest Start
            itemRegistry.register(new ItemIronChest(IronChestBlocks.ironChestBlock));
            // Chest End

            // Shulker Start
            for (EnumDyeColor color : EnumDyeColor.values())
            {
                itemRegistry.register(new ItemIronShulkerBox(BlockLists.SHULKER_BLOCKS.get(color.getMetadata()), color));
            }
            // Shulker End
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event)
        {
            // Chest Start
            Item chestItem = Item.getItemFromBlock(IronChestBlocks.ironChestBlock);

            for (IronChestType type : IronChestType.values())
            {
                if (type != IronChestType.WOOD)
                {
                    ModelLoader.setCustomModelResourceLocation(chestItem, type.ordinal(), new ModelResourceLocation(chestItem.getRegistryName(), "variant=" + type.getName()));
                }
            }
            // Chest End

            // Shulker Start
            for (Block shulker : BlockLists.SHULKER_BLOCKS)
            {
                Item shulkerBoxItem = Item.getItemFromBlock(shulker);

                for (IronShulkerBoxType type : IronShulkerBoxType.values())
                {
                    if (type != IronShulkerBoxType.VANILLA)
                    {
                        ModelLoader.setCustomModelResourceLocation(shulkerBoxItem, type.ordinal(), new ModelResourceLocation(shulkerBoxItem.getRegistryName(), "variant=" + type.getName()));
                    }
                }
            }
            // Shulker End
        }
    }
}
