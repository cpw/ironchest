package com.progwml6.ironchest;

import com.progwml6.ironchest.client.data.IronChestsLangProvider;
import com.progwml6.ironchest.client.screen.IronChestScreen;
import com.progwml6.ironchest.client.tileentity.IronChestTileEntityRenderer;
import com.progwml6.ironchest.client.tileentity.IronChestsModels;
import com.progwml6.ironchest.common.block.IronChestsBlocks;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.tileentity.IronChestsTileEntityTypes;
import com.progwml6.ironchest.common.inventory.IronChestsContainerTypes;
import com.progwml6.ironchest.common.item.IronChestsItems;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(IronChests.MODID)
public class IronChests {

  public static final String MODID = "ironchest";

  public static final ItemGroup IRONCHESTS_ITEM_GROUP = (new ItemGroup("ironchest") {
    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack createIcon() {
      return new ItemStack(IronChestsBlocks.IRON_CHEST.get());
    }
  });

  public IronChests() {
    IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

    // General mod setup
    modBus.addListener(this::setup);
    modBus.addListener(this::gatherData);

    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
      // Client setup
      modBus.addListener(this::setupClient);
      new IronChestsModels();
    });

    // Registry objects
    IronChestsBlocks.BLOCKS.register(modBus);
    IronChestsItems.ITEMS.register(modBus);
    IronChestsTileEntityTypes.TILE_ENTITIES.register(modBus);
    IronChestsContainerTypes.CONTAINERS.register(modBus);
  }

  @OnlyIn(Dist.CLIENT)
  private void setupClient(final FMLClientSetupEvent event) {
    ScreenManager.registerFactory(IronChestsContainerTypes.IRON_CHEST.get(), IronChestScreen::new);
    ScreenManager.registerFactory(IronChestsContainerTypes.GOLD_CHEST.get(), IronChestScreen::new);
    ScreenManager.registerFactory(IronChestsContainerTypes.DIAMOND_CHEST.get(), IronChestScreen::new);
    ScreenManager.registerFactory(IronChestsContainerTypes.CRYSTAL_CHEST.get(), IronChestScreen::new);
    ScreenManager.registerFactory(IronChestsContainerTypes.COPPER_CHEST.get(), IronChestScreen::new);
    ScreenManager.registerFactory(IronChestsContainerTypes.SILVER_CHEST.get(), IronChestScreen::new);
    ScreenManager.registerFactory(IronChestsContainerTypes.OBSIDIAN_CHEST.get(), IronChestScreen::new);
    ScreenManager.registerFactory(IronChestsContainerTypes.DIRT_CHEST.get(), IronChestScreen::new);

    ClientRegistry.bindTileEntityRenderer(IronChestsTileEntityTypes.IRON_CHEST.get(), IronChestTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(IronChestsTileEntityTypes.GOLD_CHEST.get(), IronChestTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(IronChestsTileEntityTypes.DIAMOND_CHEST.get(), IronChestTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(IronChestsTileEntityTypes.COPPER_CHEST.get(), IronChestTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(IronChestsTileEntityTypes.SILVER_CHEST.get(), IronChestTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(IronChestsTileEntityTypes.CRYSTAL_CHEST.get(), IronChestTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(IronChestsTileEntityTypes.OBSIDIAN_CHEST.get(), IronChestTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(IronChestsTileEntityTypes.DIRT_CHEST.get(), IronChestTileEntityRenderer::new);
  }

  private void setup(final FMLCommonSetupEvent event) {

  }

  private void gatherData(GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();

    if (event.includeClient()) {
      gen.addProvider(new IronChestsLangProvider(gen));
    }
    if (event.includeServer()) {
      //gen.addProvider(new IronChestsLootTableProvider(gen));
    }
  }

}
