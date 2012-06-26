/*******************************************************************************
 * Copyright (c) 2012 cpw. All rights reserved. This program and the accompanying materials are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at http://www.gnu.org/licenses/gpl.html
 *
 * Contributors: cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLCommonHandler;

public class Version {
  private static String major;
  private static String minor;
  @SuppressWarnings("unused")
  private static String rev;
  @SuppressWarnings("unused")
  private static String build;
  @SuppressWarnings("unused")
  private static String mcversion;
  private static boolean loaded;

  private static void init() {
    InputStream stream = Version.class.getClassLoader().getResourceAsStream("ironchestversion.properties");
    Properties properties = new Properties();

    if (stream != null) {
      try {
        properties.load(stream);
        major = properties.getProperty("ironchest.build.major.number");
        minor = properties.getProperty("ironchest.build.minor.number");
        rev = properties.getProperty("ironchest.build.revision.number");
        build = properties.getProperty("ironchest.build.build.number");
        mcversion = properties.getProperty("ironchest.build.mcversion");
      } catch (IOException ex) {
        FMLCommonHandler.instance().getFMLLogger().log(Level.SEVERE, "Could not get IronChest version information - corrupted installation detected!", ex);
        throw new RuntimeException(ex);
      }
    }
    loaded = true;
  }
  public static final String version() {
    if (!loaded) {
      init();
    }
    return major+"."+minor;
  }
}
