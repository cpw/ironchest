package cpw.mods.ironchest.server;

import java.io.File;

import net.minecraft.src.Container;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
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
	public File getMinecraftDir() {
		return new File(".");
	}

	@Override
	public void applyExtraDataToDrops(EntityItem entityitem, NBTTagCompound data) {
        entityitem.item.setTagCompound(data);
	}

	@Override
	public boolean isRemote() {
		return false;
	}

  @Override
  public Container getGuiContainer(int ID, EntityPlayerMP player, World world, int X, int Y, int Z) {
    TileEntity te=world.getBlockTileEntity(X, Y, Z);
    if (te!=null && te instanceof TileEntityIronChest) {
      TileEntityIronChest icte=(TileEntityIronChest) te;
      return new ContainerIronChestBase(player.inventory, icte, icte.getType(), 0, 0);
    } else {
      return null;
    }
  }

  @Override
  public World getCurrentWorld() {
    // NOOP on server: there's lots
    return null;
  }

}
