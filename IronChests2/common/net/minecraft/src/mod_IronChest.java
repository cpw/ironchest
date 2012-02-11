package net.minecraft.src;

import java.io.File;
import java.lang.reflect.Method;

import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.IOreHandler;
import net.minecraft.src.forge.MinecraftForge;
import cpw.mods.ironchest.BlockIronChest;
import cpw.mods.ironchest.ChestChangerType;
import cpw.mods.ironchest.IProxy;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemChestChanger;
import cpw.mods.ironchest.ItemIronChest;
import cpw.mods.ironchest.ServerClientProxy;
import cpw.mods.ironchest.TileEntityIronChest;

public class mod_IronChest extends BaseModMp {

	public static BlockIronChest ironChestBlock;
	public static ItemChestChanger itemChestChanger;
	public static IProxy proxy;

	@Override
	public String getVersion() {
		return "2.4";
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
			ChestChangerType.buildItems(cfg, 19501);
			IronChestType.initGUIs(cfg);
		} catch (Exception e) {
			ModLoader.getLogger().severe("IronChest was unable to load it's configuration successfully");
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		} finally {
			cfg.save();
		}

		ModLoader.RegisterBlock(ironChestBlock, ItemIronChest.class);
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
        IronChestType.generateTieredRecipies(ironChestBlock);

        proxy.registerRenderInformation();
	}


	@Override
	public void ModsLoaded() {
		try {
			Class<?> equivexmaps=Class.forName("ee.EEMaps");
			Method addEMC=equivexmaps.getMethod("addEMC",int.class,int.class,int.class);
			Method addMeta=equivexmaps.getMethod("addMeta",int.class,int.class);
			int[] chestEMCValues=new int[] { 8*8+256*8, 8*8+256*8+2048*8, 2*8192+8*8+256*8+2048*8+6, 85*8+8*8, 85*8+8*8+512*8 };
			for (IronChestType icType : IronChestType.values()) { 
				addEMC.invoke(null,ironChestBlock.blockID,icType.ordinal(),chestEMCValues[icType.ordinal()]);
			}
			addMeta.invoke(null,ironChestBlock.blockID,IronChestType.values().length-1);
			ModLoader.getLogger().fine("mod_IronChest registered chests with Equivalent Exchange");
		} catch (Exception ex) {
			ModLoader.getLogger().fine("mod_IronChest unable to load Equivalent Exchange integration");
		}
	}
	
	public static void openGUI(EntityPlayer player, TileEntityIronChest te) {
		proxy.showGUI(te,player);
	}
}
