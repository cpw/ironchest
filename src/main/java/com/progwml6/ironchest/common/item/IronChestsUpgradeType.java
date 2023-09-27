package com.progwml6.ironchest.common.item;

import com.progwml6.ironchest.common.Util;
import com.progwml6.ironchest.common.block.IronChestsTypes;

import static com.progwml6.ironchest.common.block.IronChestsTypes.COPPER;
import static com.progwml6.ironchest.common.block.IronChestsTypes.CRYSTAL;
import static com.progwml6.ironchest.common.block.IronChestsTypes.DIAMOND;
import static com.progwml6.ironchest.common.block.IronChestsTypes.GOLD;
import static com.progwml6.ironchest.common.block.IronChestsTypes.IRON;
import static com.progwml6.ironchest.common.block.IronChestsTypes.OBSIDIAN;
import static com.progwml6.ironchest.common.block.IronChestsTypes.WOOD;

public enum IronChestsUpgradeType {

  IRON_TO_GOLD(IRON, GOLD),
  GOLD_TO_DIAMOND(GOLD, DIAMOND),
  COPPER_TO_IRON(COPPER, IRON),
  DIAMOND_TO_CRYSTAL(DIAMOND, CRYSTAL),
  WOOD_TO_IRON(WOOD, IRON),
  WOOD_TO_COPPER(WOOD, COPPER),
  DIAMOND_TO_OBSIDIAN(DIAMOND, OBSIDIAN);

  public final String name;
  public final IronChestsTypes source;
  public final IronChestsTypes target;

  IronChestsUpgradeType(IronChestsTypes source, IronChestsTypes target) {
    this.name = Util.toEnglishName(this.name());
    this.source = source;
    this.target = target;
  }

  public boolean canUpgrade(IronChestsTypes from) {
    return from == this.source;
  }
}
