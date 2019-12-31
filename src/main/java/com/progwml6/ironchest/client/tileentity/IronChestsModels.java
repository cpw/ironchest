package com.progwml6.ironchest.client.tileentity;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.profiler.EmptyProfiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class IronChestsModels {

  public static final ResourceLocation chestAtlas = new ResourceLocation("textures/atlas/chest.png");

  protected static final Set<Material> LOCATIONS_BUILTIN_TEXTURES = Util.make(Sets.newHashSet(), (p_229337_0_) -> {
    addTextures(p_229337_0_::add);
  });

  public IronChestsModels() {
    System.out.println("IronChestsModels");
    //ModelBakery.LOCATIONS_BUILTIN_TEXTURES.addAll(LOCATIONS_BUILTIN_TEXTURES);
  }

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
    return new Material(Atlases.field_228747_f_, new ResourceLocation(IronChests.MODID, "model/" + name + "_chest"));
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
      case DIRTCHEST9000:
        return DIRT_CHEST_LOCATION;
      case WOOD:
      default:
        return Atlases.field_228758_q_;
    }
  }
}
