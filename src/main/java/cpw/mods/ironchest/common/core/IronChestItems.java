package cpw.mods.ironchest.common.core;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.items.ChestChangerType;
import cpw.mods.ironchest.common.items.ShulkerBoxChangerType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class IronChestItems
{
    @EventBusSubscriber(modid = IronChest.MOD_ID)
    public static class Registration
    {
        @SubscribeEvent
        public static void registerItems(Register<Item> event)
        {
            IForgeRegistry<Item> itemRegistry = event.getRegistry();

            // Chest Start
            ChestChangerType.buildItems(itemRegistry);
            // Chest End

            // Shulker Start
            ShulkerBoxChangerType.buildItems(itemRegistry);
            // Shulker End
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event)
        {
            // Chest Start
            for (ChestChangerType type : ChestChangerType.VALUES)
            {
                ModelLoader.setCustomModelResourceLocation(type.item, 0, new ModelResourceLocation(new ResourceLocation(IronChest.MOD_ID, "iron_chest_upgrades"), "variant=" + type.itemName.toLowerCase()));
            }
            // Chest End

            // Shulker Start
            for (ShulkerBoxChangerType type : ShulkerBoxChangerType.VALUES)
            {
                ModelLoader.setCustomModelResourceLocation(type.item, 0, new ModelResourceLocation(new ResourceLocation(IronChest.MOD_ID, "iron_shulker_box_upgrades"), "variant=" + type.itemName.toLowerCase()));
            }
            // Shulker End
        }
    }
}
