package com.progwml6.ironchest.client.tileentity;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = IronChests.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IronChestsModels {

  public static final ResourceLocation chestAtlas = new ResourceLocation("textures/atlas/chest.png");

  public static final Material IRON_CHEST_LOCATION = getChestMaterial("iron");
  public static final Material GOLD_CHEST_LOCATION = getChestMaterial("gold");
  public static final Material DIAMOND_CHEST_LOCATION = getChestMaterial("diamond");
  public static final Material COPPER_CHEST_LOCATION = getChestMaterial("copper");
  public static final Material SILVER_CHEST_LOCATION = getChestMaterial("silver");
  public static final Material CRYSTAL_CHEST_LOCATION = getChestMaterial("crystal");
  public static final Material OBSIDIAN_CHEST_LOCATION = getChestMaterial("obsidian");
  public static final Material DIRT_CHEST_LOCATION = getChestMaterial("dirt");

  public static void addTextures(Consumer<Material> p_228775_0_) {
    p_228775_0_.accept(IRON_CHEST_LOCATION);
    p_228775_0_.accept(GOLD_CHEST_LOCATION);
    p_228775_0_.accept(DIAMOND_CHEST_LOCATION);
    p_228775_0_.accept(COPPER_CHEST_LOCATION);
    p_228775_0_.accept(SILVER_CHEST_LOCATION);
    p_228775_0_.accept(CRYSTAL_CHEST_LOCATION);
    p_228775_0_.accept(OBSIDIAN_CHEST_LOCATION);
    p_228775_0_.accept(DIRT_CHEST_LOCATION);
  }

  private static Material getChestMaterial(String name) {
    return new Material(Atlases.CHEST_ATLAS, new ResourceLocation(IronChests.MODID, "model/" + name + "_chest"));
  }

  public static Material chooseChestModel(TileEntity tileEntity, IronChestsTypes type) {
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
        return Atlases.CHEST_MATERIAL;
    }
  }

  @SubscribeEvent
  public static void onStitch(TextureStitchEvent.Pre event) {
    if (!event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
      return;
    }

    event.addSprite(IRON_CHEST_LOCATION.getTextureLocation());
    event.addSprite(GOLD_CHEST_LOCATION.getTextureLocation());
    event.addSprite(DIAMOND_CHEST_LOCATION.getTextureLocation());
    event.addSprite(COPPER_CHEST_LOCATION.getTextureLocation());
    event.addSprite(SILVER_CHEST_LOCATION.getTextureLocation());
    event.addSprite(CRYSTAL_CHEST_LOCATION.getTextureLocation());
    event.addSprite(OBSIDIAN_CHEST_LOCATION.getTextureLocation());
    event.addSprite(DIRT_CHEST_LOCATION.getTextureLocation());
  }
}
