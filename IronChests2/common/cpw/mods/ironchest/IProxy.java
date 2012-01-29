package cpw.mods.ironchest;

import java.io.File;

import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;

public interface IProxy {

	public abstract void registerRenderInformation();

	public abstract void registerTileEntities();

	public abstract void registerTranslations();

	public abstract void showGUI(TileEntityIronChest te, EntityPlayer player);

	public abstract File getMinecraftDir();
	
	public abstract void applyExtraDataToDrops(EntityItem item, NBTTagCompound data);

	public abstract void registerGUI(int guiId);

}
