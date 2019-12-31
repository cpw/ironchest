package com.progwml6.ironchest.common.inventory;

import com.progwml6.ironchest.IronChests;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class IronChestsContainerTypes {

  public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, IronChests.MODID);

  public static final RegistryObject<ContainerType<IronChestContainer>> IRON_CHEST = CONTAINERS.register("iron_chest", () -> new ContainerType<>(IronChestContainer::createIronContainer));

  public static final RegistryObject<ContainerType<IronChestContainer>> GOLD_CHEST = CONTAINERS.register("gold_chest", () -> new ContainerType<>(IronChestContainer::createGoldContainer));

  public static final RegistryObject<ContainerType<IronChestContainer>> DIAMOND_CHEST = CONTAINERS.register("diamond_chest", () -> new ContainerType<>(IronChestContainer::createDiamondContainer));

  public static final RegistryObject<ContainerType<IronChestContainer>> CRYSTAL_CHEST = CONTAINERS.register("crystal_chest", () -> new ContainerType<>(IronChestContainer::createCrystalContainer));

  public static final RegistryObject<ContainerType<IronChestContainer>> COPPER_CHEST = CONTAINERS.register("copper_chest", () -> new ContainerType<>(IronChestContainer::createCopperContainer));

  public static final RegistryObject<ContainerType<IronChestContainer>> SILVER_CHEST = CONTAINERS.register("silver_chest", () -> new ContainerType<>(IronChestContainer::createSilverContainer));

  public static final RegistryObject<ContainerType<IronChestContainer>> OBSIDIAN_CHEST = CONTAINERS.register("obsidian_chest", () -> new ContainerType<>(IronChestContainer::createObsidianContainer));

  public static final RegistryObject<ContainerType<IronChestContainer>> DIRT_CHEST = CONTAINERS.register("dirt_chest", () -> new ContainerType<>(IronChestContainer::createDirtContainer));
}
