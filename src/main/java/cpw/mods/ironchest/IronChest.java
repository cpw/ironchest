/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors:
 * cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import java.util.Properties;

import cpw.mods.ironchest.common.CommonProxy;
import cpw.mods.ironchest.common.lib.BlockLists;
import cpw.mods.ironchest.common.network.MessageCrystalChestSync;
import cpw.mods.ironchest.common.network.MessageCrystalShulkerSync;
import cpw.mods.ironchest.common.util.MissingMappingsHandler;
import cpw.mods.ironchest.common.util.OcelotsSitOnChestsHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = IronChest.MOD_ID, name = "Iron Chests", dependencies = "required-after:forge@[14.21.0.2359,)", acceptedMinecraftVersions = "[1.12, 1.13)")
public class IronChest
{
    public static final String MOD_ID = "ironchest";

    @Instance(IronChest.MOD_ID)
    public static IronChest instance;

    @SidedProxy(clientSide = "cpw.mods.ironchest.client.ClientProxy", serverSide = "cpw.mods.ironchest.common.CommonProxy")
    public static CommonProxy proxy;

    public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Properties properties = event.getVersionProperties();

        if (properties != null)
        {
            String major = properties.getProperty("IronChest.build.major.number");
            String minor = properties.getProperty("IronChest.build.minor.number");
            String rev = properties.getProperty("IronChest.build.revision.number");
            String build = properties.getProperty("IronChest.build.number");

            event.getModMetadata().version = String.format("%s.%s.%s build %s", major, minor, rev, build);
        }

        proxy.preInit();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

        MinecraftForge.EVENT_BUS.register(new OcelotsSitOnChestsHandler());

        MinecraftForge.EVENT_BUS.register(new MissingMappingsHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        int messageId = 0;
        packetHandler.registerMessage(MessageCrystalChestSync.Handler.class, MessageCrystalChestSync.class, messageId++, Side.CLIENT);
        packetHandler.registerMessage(MessageCrystalShulkerSync.Handler.class, MessageCrystalShulkerSync.class, messageId++, Side.CLIENT);

        BlockLists.createShulkerItemList();
    }

}
