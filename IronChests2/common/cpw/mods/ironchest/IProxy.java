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

import java.io.File;

import net.minecraft.src.EntityItem;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import net.minecraft.src.forge.IGuiHandler;

public interface IProxy extends IGuiHandler {

	public abstract void registerRenderInformation();

	public abstract void registerTileEntities();

	public abstract void registerTranslations();

	public abstract File getMinecraftDir();
	
	public abstract void applyExtraDataToDrops(EntityItem item, NBTTagCompound data);

	public abstract boolean isRemote();

  public abstract World getCurrentWorld();
}
