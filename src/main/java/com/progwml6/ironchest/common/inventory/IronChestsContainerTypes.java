package com.progwml6.ironchest.common.inventory;

import com.progwml6.ironchest.IronChests;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class IronChestsContainerTypes {

  public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, IronChests.MODID);

  public static final RegistryObject<MenuType<IronChestContainer>> IRON_CHEST = CONTAINERS.register("iron_chest", () -> new MenuType<>(IronChestContainer::createIronContainer));

  public static final RegistryObject<MenuType<IronChestContainer>> GOLD_CHEST = CONTAINERS.register("gold_chest", () -> new MenuType<>(IronChestContainer::createGoldContainer));

  public static final RegistryObject<MenuType<IronChestContainer>> DIAMOND_CHEST = CONTAINERS.register("diamond_chest", () -> new MenuType<>(IronChestContainer::createDiamondContainer));

  public static final RegistryObject<MenuType<IronChestContainer>> CRYSTAL_CHEST = CONTAINERS.register("crystal_chest", () -> new MenuType<>(IronChestContainer::createCrystalContainer));

  public static final RegistryObject<MenuType<IronChestContainer>> COPPER_CHEST = CONTAINERS.register("copper_chest", () -> new MenuType<>(IronChestContainer::createCopperContainer));

  public static final RegistryObject<MenuType<IronChestContainer>> SILVER_CHEST = CONTAINERS.register("silver_chest", () -> new MenuType<>(IronChestContainer::createSilverContainer));

  public static final RegistryObject<MenuType<IronChestContainer>> OBSIDIAN_CHEST = CONTAINERS.register("obsidian_chest", () -> new MenuType<>(IronChestContainer::createObsidianContainer));

  public static final RegistryObject<MenuType<IronChestContainer>> DIRT_CHEST = CONTAINERS.register("dirt_chest", () -> new MenuType<>(IronChestContainer::createDirtContainer));
}
