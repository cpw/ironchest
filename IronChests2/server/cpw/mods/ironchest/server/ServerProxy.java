package cpw.mods.ironchest.server;

import java.io.File;

import net.minecraft.src.BaseModMp;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ModLoaderMp;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.mod_IronChest;
import cpw.mods.ironchest.ContainerIronChestBase;
import cpw.mods.ironchest.IProxy;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;

public class ServerProxy implements IProxy {

	@Override
	public void registerRenderInformation() {
		// NOOP on server
	}

	@Override
	public void registerTileEntities() {
		for (IronChestType typ : IronChestType.values()) {
			ModLoader.registerTileEntity(typ.clazz, typ.name());
		}
	}

	@Override
	public void registerTranslations() {
		// NOOP on server
	}

	@Override
	public void showGUI(TileEntityIronChest te, EntityPlayer player) {
		ModLoader.openGUI(player, te.getType().guiId, te, new ContainerIronChestBase(player.inventory,te, te.getType(), 1, 1));
	}

	@Override
	public File getMinecraftDir() {
		return new File(".");
	}

	@Override
	public void applyExtraDataToDrops(EntityItem entityitem, NBTTagCompound data) {
        entityitem.item.setTagCompound(data);
	}

	@Override
	public void registerGUI(int guiId) {
		// NOOP on server
	}

	@Override
	public void handleTileEntityPacket(int x, int y, int z, int type, int[] intData, float[] floatData, String[] stringData) {
		// NOOP on server
	}

	@Override
	public Packet getDescriptionPacket(TileEntityIronChest tile) {
		return ModLoaderMp.getTileEntityPacket(ModLoaderMp.getModInstance(mod_IronChest.class), tile.xCoord, tile.yCoord, tile.zCoord, tile.getType().ordinal(), tile.buildIntDataList(),null,null);
	}

	@Override
	public void sendTileEntityUpdate(TileEntityIronChest tile) {
		ModLoaderMp.sendTileEntityPacket(tile);
	}

	@Override
	public boolean isRemote() {
		return false;
	}

}
