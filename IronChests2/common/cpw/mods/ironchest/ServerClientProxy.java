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

import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.MinecraftForge;

public enum ServerClientProxy {
	CLIENT("cpw.mods.ironchest.client.ClientProxy"),
	SERVER("cpw.mods.ironchest.server.ServerProxy");
	
	private String className;
	private ServerClientProxy(String proxyClassName) {
		className=proxyClassName;
	}
	
	private IProxy buildProxy() {
		try {
			return (IProxy) Class.forName(className).newInstance();
		} catch (Exception e) {
			ModLoader.getLogger().severe("A fatal error has occured initializing IronChests");
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}
	public static IProxy getProxy() {
	  if (MinecraftForge.isClient()) {
			return CLIENT.buildProxy();
		} else {
		  return SERVER.buildProxy();
		}
	}
}
