package cpw.mods.ironchest.common.core;

import cpw.mods.ironchest.common.items.ChestChangerType;
import cpw.mods.ironchest.common.items.ItemChestChanger;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class IronChestItems
{
    //@formatter:off
    public static Item.Builder itemBuilder = (new Item.Builder()).group(IronChestCreativeTabs.IRON_CHESTS).maxStackSize(1);
    public static Item ironToGoldUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.IRON_GOLD);
    public static Item goldToDiamondUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.GOLD_DIAMOND);
    public static Item copperToSilverUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.COPPER_SILVER);
    public static Item silverToGoldUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.SILVER_GOLD);
    public static Item copperToIronUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.COPPER_IRON);
    public static Item diamondToCrystalUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.DIAMOND_CRYSTAL);
    public static Item woodToIronUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.WOOD_IRON);
    public static Item woodToCopperUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.WOOD_COPPER);
    public static Item diamondToObsidianUpgrade = new ItemChestChanger(itemBuilder, ChestChangerType.DIAMOND_OBSIDIAN);
    //@formatter:on

    public IronChestItems()
    {

    }

    public void registerItems()
    {
        // Chest Start
        GameRegistry.findRegistry(Item.class).registerAll(ironToGoldUpgrade, goldToDiamondUpgrade, copperToSilverUpgrade, silverToGoldUpgrade, copperToIronUpgrade, diamondToCrystalUpgrade, woodToIronUpgrade, woodToCopperUpgrade, diamondToObsidianUpgrade);
        // Chest End
    }

    public static void registerItems(Register<Item> event)
    {
        IForgeRegistry<Item> itemRegistry = event.getRegistry();

        ChestChangerType.buildItems(itemRegistry);
    }
}
