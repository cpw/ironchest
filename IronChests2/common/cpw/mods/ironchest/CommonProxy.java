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

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.ironchest.client.GUIChest;


public class CommonProxy implements IGuiHandler {
	public void registerRenderInformation()
	{

	}

	public void registerTileEntitySpecialRenderer(IronChestType typ)
	{

	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te != null && te instanceof TileEntityIronChest) {
			return GUIChest.GUI.buildGUI(IronChestType.values()[ID], player.inventory, (TileEntityIronChest) te);
		} else {
			return null;
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int X, int Y, int Z) {
		TileEntity te = world.getBlockTileEntity(X, Y, Z);
		if (te != null && te instanceof TileEntityIronChest) {
			TileEntityIronChest icte = (TileEntityIronChest) te;
			return new ContainerIronChestBase(player.inventory, icte, icte.getType(), 0, 0);
		} else {
			return null;
		}
	}

	public World getClientWorld() {
		return null;
	}

}
