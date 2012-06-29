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
package cpw.mods.ironchest;

import java.io.File;
import java.lang.reflect.Method;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.modloader.ModLoaderModContainer;

import net.minecraft.src.ModLoader;
import net.minecraft.src.SidedProxy;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.NetworkMod;

public class mod_IronChest extends NetworkMod {

  public static BlockIronChest ironChestBlock;
  public static ItemChestChanger itemChestChanger;
  @SidedProxy(clientSide="cpw.mods.ironchest.client.ClientProxy", serverSide="cpw.mods.ironchest.server.ServerProxy")
  public static IProxy proxy;
  public static mod_IronChest instance;

  @Override
  public String getVersion() {
    return Version.version();
  }

  @Override
  public void load() {
    MinecraftForge.versionDetect("IronChest", 3, 3, 7);
    ModContainer fml=ModLoaderModContainer.findContainerFor(this);
    if (fml.getMetadata()!=null) {
      fml.getMetadata().version=Version.fullVersionString();
    }
    instance = this;
    File cfgFile = new File(proxy.getMinecraftDir(), "config/IronChest.cfg");
    Configuration cfg = new Configuration(cfgFile);
    try {
      cfg.load();
      int bId = cfg.getOrCreateBlockIdProperty("ironChests", 502).getInt(502);
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
    proxy.registerTranslations();
    proxy.registerTileEntities();
    IronChestType.generateTieredRecipes(ironChestBlock);
    ChestChangerType.generateRecipes();

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
      int[] chestEMCValues = new int[]
      {
          8 * 8 + 256 * 8, /* iron chest */
          8 * 8 + 256 * 8 + 2048 * 8, /* gold chest */
          2 * 8192 + 8 * 8 + 256 * 8 + 2048 * 8 + 6, /* diamond chest */
          85 * 8 + 8 * 8, /* copper chest */
          85 * 8 + 8 * 8 + 512 * 8, /* silver chest */
          2 * 8192 + 8 * 8 + 256 * 8 + 2048 * 8 + 6 + 8 /* crystal chest */
      };
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
