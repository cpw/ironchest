package net.minecraft.src;

import java.io.File;

import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.IOreHandler;
import net.minecraft.src.forge.MinecraftForge;
import cpw.mods.ironchest.BlockIronChest;
import cpw.mods.ironchest.IProxy;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemIronChest;
import cpw.mods.ironchest.ServerClientProxy;
import cpw.mods.ironchest.TileEntityIronChest;

public class mod_IronChest extends BaseModMp {

	public static BlockIronChest ironChestBlock;
	public static IProxy proxy;

	@Override
	public String getVersion() {
		return "2.1";
	}

	@Override
	public void load() {
		MinecraftForge.versionDetect("IronChest", 1, 3, 0);
		proxy = ServerClientProxy.getProxy();
		File cfgFile = new File(proxy.getMinecraftDir(), "config/IronChest.cfg");
		Configuration cfg = new Configuration(cfgFile);
		try {
			cfg.load();
			ironChestBlock = new BlockIronChest(Integer.parseInt(cfg.getOrCreateBlockIdProperty("ironChests", 181).value));
			IronChestType.initGUIs(cfg);
		} catch (Exception e) {
			ModLoader.getLogger().severe("IronChest was unable to load it's configuration successfully");
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		} finally {
			cfg.save();
		}

		MinecraftForge.registerOreHandler(new IOreHandler() {
			@Override
			public void registerOre(String oreClass, ItemStack ore) {
				if ("ingotCopper".equals(oreClass)) {
					IronChestType.generateRecipesForType(ironChestBlock, Block.chest, IronChestType.COPPER, ore);
				}
				if ("ingotSilver".equals(oreClass)) {
					IronChestType.generateRecipesForType(ironChestBlock, ironChestBlock, IronChestType.SILVER, ore);
				}
				if ("ingotRefinedIron".equals(oreClass)) {
					IronChestType.generateRecipesForType(ironChestBlock, Block.chest, IronChestType.IRON, ore);
				}
			}
		});
		ModLoader.RegisterBlock(ironChestBlock, ItemIronChest.class);
		proxy.registerTranslations();
		proxy.registerTileEntities();
        IronChestType.generateTieredRecipies(ironChestBlock);

        proxy.registerRenderInformation();
	}


	public static void openGUI(EntityPlayer player, TileEntityIronChest te) {
		proxy.showGUI(te,player);
	}
}
