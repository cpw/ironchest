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

import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
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

@Mod(modid = "IronChest", name = "Iron Chests", dependencies="required-after:FML@[3.1.15,)")
@NetworkMod(channels = { "IronChest" }, versionBounds = "[4.0,)", clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class IronChest {

	public static BlockIronChest ironChestBlock;
	@SidedProxy(clientSide = "cpw.mods.ironchest.client.ClientProxy", serverSide = "cpw.mods.ironchest.CommonProxy")
	public static CommonProxy proxy;
	@Instance("IronChest")
	public static IronChest instance;
	public static boolean CACHE_RENDER = true;
	public static boolean OCELOTS_SITONCHESTS = true;
	private int blockId;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		Version.init(event.getVersionProperties());
		event.getModMetadata().version = Version.fullVersionString();
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try {
			cfg.load();
			blockId = cfg.getOrCreateBlockIdProperty("ironChests", 181).getInt(181);
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
		ironChestBlock = new BlockIronChest(blockId);
		GameRegistry.registerBlock(ironChestBlock, ItemIronChest.class);
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
		if (OCELOTS_SITONCHESTS)
		{
			MinecraftForge.EVENT_BUS.register(new OcelotsSitOnChestsHandler());
		}
	}

	@PostInit
	public void modsLoaded(FMLPostInitializationEvent evt) {
	}
}
