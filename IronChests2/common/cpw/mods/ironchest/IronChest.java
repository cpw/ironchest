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

import java.lang.reflect.Method;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "IronChest", name = "Iron Chests", version = "4.0")
@NetworkMod(channels = { "IronChest" }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class IronChest {

	public static BlockIronChest ironChestBlock;
	@SidedProxy(clientSide = "cpw.mods.ironchest.client.ClientProxy", serverSide = "cpw.mods.ironchest.common.CommonProxy")
	public static CommonProxy proxy;
	@Instance
	public static IronChest instance;
	public static boolean CACHE_RENDER = true;
	public static boolean OCELOTS_SITONCHESTS = true;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		event.getModMetadata().version = Version.fullVersionString();
		try {
			cfg.load();
			int bId = cfg.getOrCreateBlockIdProperty("ironChests", 181).getInt(181);
			ironChestBlock = new BlockIronChest(bId);
			ChestChangerType.buildItems(cfg, 29501);
			CACHE_RENDER = cfg.getOrCreateBooleanProperty("cacheRenderingInformation", Configuration.CATEGORY_GENERAL, true).getBoolean(true);
			OCELOTS_SITONCHESTS = cfg.getOrCreateBooleanProperty("ocelotsSitOnChests", Configuration.CATEGORY_GENERAL, true).getBoolean(true);
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "IronChest has a problem loading it's configuration");
		} finally {
			cfg.save();
		}
	}

	@Init
	public void load(FMLInitializationEvent evt) {
		GameRegistry.registerBlock(ironChestBlock);
		for (IronChestType typ : IronChestType.values()) {
			GameRegistry.registerTileEntity(typ.clazz, typ.name());
			LanguageRegistry.instance().addStringLocalization(typ.name() + ".name", "en_US", typ.friendlyName);
			proxy.registerTileEntitySpecialRenderer(typ);
		}
		for (ChestChangerType typ : ChestChangerType.values()) {
			LanguageRegistry.instance().addStringLocalization("item." + typ.itemName + ".name", "en_US", typ.descriptiveName);
		}
		IronChestType.generateTieredRecipes(ironChestBlock);
		ChestChangerType.generateRecipes();
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		proxy.registerRenderInformation();
	}

	@PostInit
	public void modsLoaded(FMLPostInitializationEvent evt) {
		try {
			Class<?> equivexmaps = Class.forName("ee.EEMaps");
			Method addEMC = equivexmaps.getMethod("addEMC", int.class, int.class, int.class);
			Method addMeta = equivexmaps.getMethod("addMeta", int.class, int.class);
			int[] chestEMCValues = new int[] { 8 * 8 + 256 * 8, /* iron chest */
			8 * 8 + 256 * 8 + 2048 * 8, /* gold chest */
			2 * 8192 + 8 * 8 + 256 * 8 + 2048 * 8 + 6, /* diamond chest */
			85 * 8 + 8 * 8, /* copper chest */
			85 * 8 + 8 * 8 + 512 * 8, /* silver chest */
			2 * 8192 + 8 * 8 + 256 * 8 + 2048 * 8 + 6 + 8 /* crystal chest */
			};
			for (IronChestType icType : IronChestType.values()) {
				if (icType.ordinal() >= chestEMCValues.length)
					break;
				addEMC.invoke(null, ironChestBlock.blockID, icType.ordinal(), chestEMCValues[icType.ordinal()]);
			}
			addMeta.invoke(null, ironChestBlock.blockID, IronChestType.values().length - 1);
			FMLLog.fine("mod_IronChest registered chests with Equivalent Exchange");
		} catch (Exception ex) {
			FMLLog.fine("mod_IronChest unable to load Equivalent Exchange integration");
		}
	}
}
