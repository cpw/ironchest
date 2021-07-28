package com.progwml6.ironchest.common.inventory;

import com.progwml6.ironchest.IronChests;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class IronChestsContainerTypes {

  public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, IronChests.MOD_ID);

  public static final RegistryObject<MenuType<IronChestMenu>> IRON_CHEST = CONTAINERS.register("iron_chest", () -> new MenuType<>(IronChestMenu::createIronContainer));

  public static final RegistryObject<MenuType<IronChestMenu>> GOLD_CHEST = CONTAINERS.register("gold_chest", () -> new MenuType<>(IronChestMenu::createGoldContainer));

  public static final RegistryObject<MenuType<IronChestMenu>> DIAMOND_CHEST = CONTAINERS.register("diamond_chest", () -> new MenuType<>(IronChestMenu::createDiamondContainer));

  public static final RegistryObject<MenuType<IronChestMenu>> CRYSTAL_CHEST = CONTAINERS.register("crystal_chest", () -> new MenuType<>(IronChestMenu::createCrystalContainer));

  public static final RegistryObject<MenuType<IronChestMenu>> COPPER_CHEST = CONTAINERS.register("copper_chest", () -> new MenuType<>(IronChestMenu::createCopperContainer));

  public static final RegistryObject<MenuType<IronChestMenu>> SILVER_CHEST = CONTAINERS.register("silver_chest", () -> new MenuType<>(IronChestMenu::createSilverContainer));

  public static final RegistryObject<MenuType<IronChestMenu>> OBSIDIAN_CHEST = CONTAINERS.register("obsidian_chest", () -> new MenuType<>(IronChestMenu::createObsidianContainer));

  public static final RegistryObject<MenuType<IronChestMenu>> DIRT_CHEST = CONTAINERS.register("dirt_chest", () -> new MenuType<>(IronChestMenu::createDirtContainer));
}
