package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.tileentity.CopperChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.CrystalChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.DiamondChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.DirtChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.GoldChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.IronChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.ObsidianChestTileEntity;
import com.progwml6.ironchest.common.block.tileentity.SilverChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public enum IronChestsTypes implements IStringSerializable {

  IRON(54, 9, IronChestTileEntity.class, 184, 222, new ResourceLocation("ironchest", "textures/gui/iron_container.png"), 256, 256),
  GOLD(81, 9, GoldChestTileEntity.class, 184, 276, new ResourceLocation("ironchest", "textures/gui/gold_container.png"), 256, 276),
  DIAMOND(108, 12, DiamondChestTileEntity.class, 238, 276, new ResourceLocation("ironchest", "textures/gui/diamond_container.png"), 256, 276),
  COPPER(45, 9, CopperChestTileEntity.class, 184, 204, new ResourceLocation("ironchest", "textures/gui/copper_container.png"), 256, 256),
  SILVER(72, 9, SilverChestTileEntity.class, 184, 258, new ResourceLocation("ironchest", "textures/gui/silver_container.png"), 256, 276),
  CRYSTAL(108, 12, CrystalChestTileEntity.class, 238, 276, new ResourceLocation("ironchest", "textures/gui/diamond_container.png"), 256, 276),
  OBSIDIAN(108, 12, ObsidianChestTileEntity.class, 238, 276, new ResourceLocation("ironchest", "textures/gui/diamond_container.png"), 256, 276),
  DIRTCHEST9000(1, 1, DirtChestTileEntity.class, 184, 184, new ResourceLocation("ironchest", "textures/gui/dirt_container.png"), 256, 256),
  WOOD(0, 0, null, 0, 0, null, 0, 0);

  private final String name;
  public final int size;
  public final int rowLength;
  public final Class<? extends TileEntity> clazz;
  public final int xSize;
  public final int ySize;
  public final ResourceLocation guiTexture;
  public final int textureXSize;
  public final int textureYSize;

  IronChestsTypes(int size, int rowLength, Class<? extends GenericIronChestTileEntity> clazz, int xSize, int ySize, ResourceLocation guiTexture, int textureXSize, int textureYSize) {
    this(null, size, rowLength, clazz, xSize, ySize, guiTexture, textureXSize, textureYSize);
  }

  IronChestsTypes(@Nullable String name, int size, int rowLength, Class<? extends GenericIronChestTileEntity> clazz, int xSize, int ySize, ResourceLocation guiTexture, int textureXSize, int textureYSize) {
    this.name = name == null ? toEnglishName(this.name()) : name;
    this.size = size;
    this.rowLength = rowLength;
    this.clazz = clazz;
    this.xSize = xSize;
    this.ySize = ySize;
    this.guiTexture = guiTexture;
    this.textureXSize = textureXSize;
    this.textureYSize = textureYSize;
  }

  public String getId() {
    return this.name().toLowerCase(Locale.ROOT);
  }

  public String getEnglishName() {
    return this.name;
  }

  public static final String toEnglishName(String internalName) {
    return Arrays.stream(internalName.toLowerCase(Locale.ROOT).split("_"))
            .map(StringUtils::capitalize)
            .collect(Collectors.joining(" "));
  }

  @Override
  public String getName() {
    return this.getEnglishName();
  }

  public int getRowCount() {
    return this.size / this.rowLength;
  }

  public boolean isTransparent() {
    return this == CRYSTAL;
  }
}
