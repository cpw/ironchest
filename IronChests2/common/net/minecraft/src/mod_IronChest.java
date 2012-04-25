/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package net.minecraft.src;

import java.io.File;
import java.lang.reflect.Method;

import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.IOreHandler;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.NetworkMod;
import cpw.mods.ironchest.BlockIronChest;
import cpw.mods.ironchest.ChestChangerType;
import cpw.mods.ironchest.IProxy;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemChestChanger;
import cpw.mods.ironchest.ItemIronChest;
import cpw.mods.ironchest.PacketHandler;
import cpw.mods.ironchest.ServerClientProxy;
import cpw.mods.ironchest.Version;

public class mod_IronChest extends NetworkMod {

  public static BlockIronChest ironChestBlock;
  public static ItemChestChanger itemChestChanger;
  public static IProxy proxy;
  public static mod_IronChest instance;

  @Override
  public String getVersion() {
    return Version.version();
  }

  @Override
  public void load() {
    MinecraftForge.versionDetect("IronChest", 3, 0, 1);
    instance = this;
    proxy = ServerClientProxy.getProxy();
    File cfgFile = new File(proxy.getMinecraftDir(), "config/IronChest.cfg");
    Configuration cfg = new Configuration(cfgFile);
    try {
      cfg.load();
      int bId = cfg.getOrCreateBlockIdProperty("ironChests", 181).getInt(181);
      if (bId >= 256) {
        throw new RuntimeException(String.format("IronChest detected an invalid block id %s\n", bId));
      }
      ironChestBlock = new BlockIronChest(bId);
      ChestChangerType.buildItems(cfg, 19501);
    } catch (Exception e) {
      ModLoader.getLogger().severe("IronChest was unable to load it's configuration successfully");
      e.printStackTrace(System.err);
      throw new RuntimeException(e);
    } finally {
      cfg.save();
    }

    ModLoader.registerBlock(ironChestBlock, ItemIronChest.class);
    MinecraftForge.registerOreHandler(new IOreHandler() {
      @Override
      public void registerOre(String oreClass, ItemStack ore) {
        if ("ingotCopper".equals(oreClass)) {
          IronChestType.COPPER.addMat(ore);
          IronChestType.generateRecipesForType(ironChestBlock, Block.chest, IronChestType.COPPER, ore);
          ChestChangerType.generateRecipe(IronChestType.COPPER);
        }
        if ("ingotSilver".equals(oreClass)) {
          IronChestType.SILVER.addMat(ore);
          IronChestType.generateRecipesForType(ironChestBlock, ironChestBlock, IronChestType.SILVER, ore);
          ChestChangerType.generateRecipe(IronChestType.SILVER);
        }
        if ("ingotRefinedIron".equals(oreClass)) {
          IronChestType.IRON.addMat(ore);
          IronChestType.generateRecipesForType(ironChestBlock, Block.chest, IronChestType.IRON, ore);
          ChestChangerType.generateRecipe(IronChestType.IRON);
        }
      }
    });
    proxy.registerTranslations();
    proxy.registerTileEntities();
    ChestChangerType.generateRecipe(IronChestType.IRON);
    ChestChangerType.generateRecipe(IronChestType.GOLD);
    ChestChangerType.generateRecipe(IronChestType.DIAMOND);
    IronChestType.generateTieredRecipies(ironChestBlock);

    MinecraftForge.setGuiHandler(this, proxy);
    MinecraftForge.registerConnectionHandler(new PacketHandler());
    proxy.registerRenderInformation();
  }

  @Override
  public void modsLoaded() {
    try {
      Class<?> equivexmaps = Class.forName("ee.EEMaps");
      Method addEMC = equivexmaps.getMethod("addEMC", int.class, int.class, int.class);
      Method addMeta = equivexmaps.getMethod("addMeta", int.class, int.class);
      int[] chestEMCValues = new int[] { 8 * 8 + 256 * 8, 8 * 8 + 256 * 8 + 2048 * 8, 2 * 8192 + 8 * 8 + 256 * 8 + 2048 * 8 + 6, 85 * 8 + 8 * 8,
          85 * 8 + 8 * 8 + 512 * 8, 2 * 8192 + 8 * 8 + 256 * 8 + 2048 * 8 + 6 + 8 };
      for (IronChestType icType : IronChestType.values()) {
        addEMC.invoke(null, ironChestBlock.blockID, icType.ordinal(), chestEMCValues[icType.ordinal()]);
      }
      addMeta.invoke(null, ironChestBlock.blockID, IronChestType.values().length - 1);
      ModLoader.getLogger().fine("mod_IronChest registered chests with Equivalent Exchange");
    } catch (Exception ex) {
      ModLoader.getLogger().fine("mod_IronChest unable to load Equivalent Exchange integration");
    }
  }

  @Override
  public boolean clientSideRequired() {
    return true;
  }

  @Override
  public boolean serverSideRequired() {
    return false;
  }
}
