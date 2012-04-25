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


public class Version {
	public static final String MAJOR="@MAJOR@";
	public static final String MINOR="@MINOR@";
	public static final String REV="@REV@";
	public static final String BUILD="@BUILD@";
	
	public static final String version() {
		return MAJOR+"."+MINOR;
	}
	
	public static final String name() {
		return "Iron Chest ("+MAJOR+"."+MINOR+") rev "+REV+" build "+BUILD;
	}
}
