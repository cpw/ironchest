package cpw.mods.ironchest.common.core;

import cpw.mods.ironchest.common.items.ChestChangerType;
import cpw.mods.ironchest.common.items.ItemChestChanger;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Builder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class IronChestItems
{
    public static Builder itemBuilder;

    public static Item ironToGoldUpgrade;

    public static Item goldToDiamondUpgrade;

    public static Item copperToSilverUpgrade;

    public static Item silverToGoldUpgrade;

    public static Item copperToIronUpgrade;

    public static Item diamondToCrystalUpgrade;

    public static Item woodToIronUpgrade;

    public static Item woodToCopperUpgrade;

    public static Item diamondToObsidianUpgrade;

    public IronChestItems()
    {

    }

    @SubscribeEvent
    public void registerItems(final RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> itemRegistry = event.getRegistry();

        itemBuilder = (new Builder()).group(IronChestCreativeTabs.IRON_CHESTS).maxStackSize(1);

        itemRegistry.register(ironToGoldUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.IRON_GOLD));
        itemRegistry.register(goldToDiamondUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.GOLD_DIAMOND));
        itemRegistry.register(copperToSilverUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.COPPER_SILVER));
        itemRegistry.register(silverToGoldUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.SILVER_GOLD));
        itemRegistry.register(copperToIronUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.COPPER_IRON));
        itemRegistry.register(diamondToCrystalUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.DIAMOND_CRYSTAL));
        itemRegistry.register(woodToIronUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.WOOD_IRON));
        itemRegistry.register(woodToCopperUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.WOOD_COPPER));
        itemRegistry.register(diamondToObsidianUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.DIAMOND_OBSIDIAN));
    }
}
