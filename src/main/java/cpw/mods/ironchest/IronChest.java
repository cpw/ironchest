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

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = IronChest.MOD_ID, name = "Iron Chests", dependencies = "required-after:Forge@[12.17.0.1909,)", acceptedMinecraftVersions = "[1.9.4, 1.11)")
public class IronChest
{
    public static final String MOD_ID = "ironchest";

    @Instance(IronChest.MOD_ID)
    public static IronChest instance;

    @SidedProxy(clientSide = "cpw.mods.ironchest.client.ClientProxy", serverSide = "cpw.mods.ironchest.CommonProxy")
    public static CommonProxy proxy;

    public static BlockIronChest ironChestBlock;
    public static ItemIronChest ironChestItemBlock;

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
            // String mcversion = properties.getProperty("IronChest.build.mcversion");
            event.getModMetadata().version = String.format("%s.%s.%s build %s", major, minor, rev, build);
        }

        ChestChangerType.buildItems();
        ironChestBlock = GameRegistry.register(new BlockIronChest());
        ironChestItemBlock = GameRegistry.register(new ItemIronChest(ironChestBlock));

        for (IronChestType typ : IronChestType.VALUES)
        {
            GameRegistry.registerTileEntity(typ.clazz, "IronChest." + typ.name());
        }

        IronChestType.registerBlocksAndRecipes(ironChestBlock);
        ChestChangerType.generateRecipes();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        proxy.registerRenderInformation();
        MinecraftForge.EVENT_BUS.register(new OcelotsSitOnChestsHandler());
    }
}
