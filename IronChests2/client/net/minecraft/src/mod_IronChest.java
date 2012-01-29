package net.minecraft.src;

import java.io.File;

import cpw.mods.ironchest.BlockIronChest;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemIronChest;
import cpw.mods.ironchest.TileEntityIronChest;
import cpw.mods.ironchest.client.GUIChest;
import cpw.mods.ironchest.client.IronChestRenderHelper;
import cpw.mods.ironchest.client.TileEntityIronChestRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.IOreHandler;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.MinecraftForgeClient;

public class mod_IronChest extends BaseModMp {

	public static BlockIronChest ironChestBlock;
	public static boolean compatibilityMode;

	@Override
	public String getVersion() {
		return "2.0";
	}

	@Override
	public void load() {
		File cfgFile = new File(Minecraft.getMinecraftDir(), "config/IronChest.cfg");
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
		IronChestType.registerTranslations();
        IronChestType.registerTileEntities(TileEntityIronChestRenderer.class);
        IronChestType.generateTieredRecipies(ironChestBlock);

        ChestItemRenderHelper.instance=new IronChestRenderHelper();
        MinecraftForgeClient.preloadTexture("cpw/mods/ironchest/sprites/block_textures.png");
	}

	public static void openGUI(EntityPlayer player, TileEntityIronChest te) {
		GUIChest.GUI.showGUI(te, player);
	}

}
