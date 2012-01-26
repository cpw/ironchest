package net.minecraft.src;

import java.io.File;

import cpw.mods.ironchest.BlockIronChest;

import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.Configuration;

public class mod_IronChest extends BaseModMp {

	public static BlockIronChest ironChestBlock;

	@Override
	public String getVersion() {
		return "2.0";
	}

	@Override
	public void load() {
		Configuration cfg=new Configuration(new File(Minecraft.getMinecraftDir(),"config/IronChest.cfg"));
		try {
			cfg.load();
			ironChestBlock=new BlockIronChest(Integer.parseInt(cfg.getOrCreateBlockIdProperty("blockVeryLargeChest", 181).value));
		} catch (Exception e) {
			ModLoader.getLogger().severe("IronChest was unable to load it's configuration successfully");
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		} finally {
			cfg.save();
		}
	}

}
