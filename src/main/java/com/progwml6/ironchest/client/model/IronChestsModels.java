package com.progwml6.ironchest.client.model;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IronChests.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IronChestsModels {

  public static final ResourceLocation IRON_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/iron_chest");
  public static final ResourceLocation GOLD_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/gold_chest");
  public static final ResourceLocation DIAMOND_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/diamond_chest");
  public static final ResourceLocation COPPER_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/copper_chest");
  public static final ResourceLocation CRYSTAL_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/crystal_chest");
  public static final ResourceLocation OBSIDIAN_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/obsidian_chest");
  public static final ResourceLocation DIRT_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/dirt_chest");
  public static final ResourceLocation VANILLA_CHEST_LOCATION = new ResourceLocation("entity/chest/normal");

  public static final ResourceLocation TRAPPED_IRON_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/trapped_iron_chest");
  public static final ResourceLocation TRAPPED_GOLD_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/trapped_gold_chest");
  public static final ResourceLocation TRAPPED_DIAMOND_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/trapped_diamond_chest");
  public static final ResourceLocation TRAPPED_COPPER_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/trapped_copper_chest");
  public static final ResourceLocation TRAPPED_CRYSTAL_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/trapped_crystal_chest");
  public static final ResourceLocation TRAPPED_OBSIDIAN_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/trapped_obsidian_chest");
  public static final ResourceLocation TRAPPED_DIRT_CHEST_LOCATION = new ResourceLocation(IronChests.MOD_ID, "model/trapped_dirt_chest");
  public static final ResourceLocation TRAPPED_VANILLA_CHEST_LOCATION = new ResourceLocation("entity/chest/trapped");

  public static ResourceLocation chooseChestTexture(IronChestsTypes type, boolean trapped) {
    if (trapped)
      return switch (type) {
        case IRON -> TRAPPED_IRON_CHEST_LOCATION;
        case GOLD -> TRAPPED_GOLD_CHEST_LOCATION;
        case DIAMOND -> TRAPPED_DIAMOND_CHEST_LOCATION;
        case COPPER -> TRAPPED_COPPER_CHEST_LOCATION;
        case CRYSTAL -> TRAPPED_CRYSTAL_CHEST_LOCATION;
        case OBSIDIAN -> TRAPPED_OBSIDIAN_CHEST_LOCATION;
        case DIRT -> TRAPPED_DIRT_CHEST_LOCATION;
        default -> TRAPPED_VANILLA_CHEST_LOCATION;
      };
    else
      return switch (type) {
        case IRON -> IRON_CHEST_LOCATION;
        case GOLD -> GOLD_CHEST_LOCATION;
        case DIAMOND -> DIAMOND_CHEST_LOCATION;
        case COPPER -> COPPER_CHEST_LOCATION;
        case CRYSTAL -> CRYSTAL_CHEST_LOCATION;
        case OBSIDIAN -> OBSIDIAN_CHEST_LOCATION;
        case DIRT -> DIRT_CHEST_LOCATION;
        default -> VANILLA_CHEST_LOCATION;
      };
  }

  @SubscribeEvent
  public static void onStitch(TextureStitchEvent.Pre event) {
    if (!event.getMap().location().equals(Sheets.CHEST_SHEET)) {
      return;
    }

    event.addSprite(IRON_CHEST_LOCATION);
    event.addSprite(GOLD_CHEST_LOCATION);
    event.addSprite(DIAMOND_CHEST_LOCATION);
    event.addSprite(COPPER_CHEST_LOCATION);
    event.addSprite(CRYSTAL_CHEST_LOCATION);
    event.addSprite(OBSIDIAN_CHEST_LOCATION);
    event.addSprite(DIRT_CHEST_LOCATION);

    event.addSprite(TRAPPED_IRON_CHEST_LOCATION);
    event.addSprite(TRAPPED_GOLD_CHEST_LOCATION);
    event.addSprite(TRAPPED_DIAMOND_CHEST_LOCATION);
    event.addSprite(TRAPPED_COPPER_CHEST_LOCATION);
    event.addSprite(TRAPPED_CRYSTAL_CHEST_LOCATION);
    event.addSprite(TRAPPED_OBSIDIAN_CHEST_LOCATION);
    event.addSprite(TRAPPED_DIRT_CHEST_LOCATION);
  }
}
