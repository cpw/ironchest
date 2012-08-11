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
package cpw.mods.ironchest.server;

import java.io.File;

import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
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
  public World getCurrentWorld() {
    // NOOP on server: there's lots
    return null;
  }

}
