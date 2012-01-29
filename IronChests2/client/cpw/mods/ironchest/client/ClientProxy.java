package cpw.mods.ironchest.client;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseModMp;
import net.minecraft.src.ChestItemRenderHelper;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ModLoaderMp;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.forge.MinecraftForgeClient;
import cpw.mods.ironchest.IProxy;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;

public class ClientProxy extends BaseModMp implements IProxy {
	@Override
	public void registerRenderInformation() {
		ChestItemRenderHelper.instance=new IronChestRenderHelper();
        MinecraftForgeClient.preloadTexture("cpw/mods/ironchest/sprites/block_textures.png");
	}

	@Override
	public void registerTileEntities() {
		for (IronChestType typ : IronChestType.values()) {
			ModLoader.RegisterTileEntity(typ.clazz, typ.name(), new TileEntityIronChestRenderer());
		}
	}

	@Override
	public void registerTranslations() {
		for (IronChestType typ : IronChestType.values()) {
			ModLoader.AddLocalization(typ.name() + ".name", typ.friendlyName);
		}
	}

	@Override
	public void showGUI(TileEntityIronChest te, EntityPlayer player) {
		GUIChest.GUI.showGUI(te, player);
		
	}

	@Override
	public File getMinecraftDir() {
		return Minecraft.getMinecraftDir();
	}

	@Override
	public void applyExtraDataToDrops(EntityItem entityitem, NBTTagCompound data) {
        entityitem.item.setTagCompound(data);
		
	}

	@Override
	public void registerGUI(int guiId) {
		ModLoaderMp.RegisterGUI(this, guiId);
	}

	@Override
	public String getVersion() {
		// Do nothing, we never get loaded like that
		return "";
	}

	@Override
	public void load() {
		// Do Nothing, we never get loaded like that
	}

	@Override
	public GuiScreen HandleGUI(int i) {
		for (IronChestType type: IronChestType.values()) {
			if (type.guiId==i) {
				return GUIChest.GUI.buildGUI(type,ModLoader.getMinecraftInstance().thePlayer.inventory,IronChestType.makeEntity(type.ordinal()));
			}
		}
		return null;
	}
}
