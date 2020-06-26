package com.progwml6.ironchest.client.tileentity;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = IronChests.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IronChestsModels {

  public static final ResourceLocation IRON_CHEST_LOCATION = new ResourceLocation(IronChests.MODID, "model/iron_chest");
  public static final ResourceLocation GOLD_CHEST_LOCATION = new ResourceLocation(IronChests.MODID, "model/gold_chest");
  public static final ResourceLocation DIAMOND_CHEST_LOCATION = new ResourceLocation(IronChests.MODID, "model/diamond_chest");
  public static final ResourceLocation COPPER_CHEST_LOCATION = new ResourceLocation(IronChests.MODID, "model/copper_chest");
  public static final ResourceLocation SILVER_CHEST_LOCATION = new ResourceLocation(IronChests.MODID, "model/silver_chest");
  public static final ResourceLocation CRYSTAL_CHEST_LOCATION = new ResourceLocation(IronChests.MODID, "model/crystal_chest");
  public static final ResourceLocation OBSIDIAN_CHEST_LOCATION = new ResourceLocation(IronChests.MODID, "model/obsidian_chest");
  public static final ResourceLocation DIRT_CHEST_LOCATION = new ResourceLocation(IronChests.MODID, "model/dirt_chest");
  public static final ResourceLocation VANLLA_CHEST_LOCATION = new ResourceLocation("entity/chest/normal");

  public static ResourceLocation chooseChestTexture(IronChestsTypes type) {
    switch (type) {
      case IRON:
        return IRON_CHEST_LOCATION;
      case GOLD:
        return GOLD_CHEST_LOCATION;
      case DIAMOND:
        return DIAMOND_CHEST_LOCATION;
      case COPPER:
        return COPPER_CHEST_LOCATION;
      case SILVER:
        return SILVER_CHEST_LOCATION;
      case CRYSTAL:
        return CRYSTAL_CHEST_LOCATION;
      case OBSIDIAN:
        return OBSIDIAN_CHEST_LOCATION;
      case DIRT:
        return DIRT_CHEST_LOCATION;
      case WOOD:
      default:
        return VANLLA_CHEST_LOCATION;
    }
  }

  @SubscribeEvent
  public static void onStitch(TextureStitchEvent.Pre event) {
    if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
      return;
    }

    event.addSprite(IRON_CHEST_LOCATION);
    event.addSprite(GOLD_CHEST_LOCATION);
    event.addSprite(DIAMOND_CHEST_LOCATION);
    event.addSprite(COPPER_CHEST_LOCATION);
    event.addSprite(SILVER_CHEST_LOCATION);
    event.addSprite(CRYSTAL_CHEST_LOCATION);
    event.addSprite(OBSIDIAN_CHEST_LOCATION);
    event.addSprite(DIRT_CHEST_LOCATION);
  }
}
