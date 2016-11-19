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

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = IronChest.MOD_ID, name = "Iron Chests", dependencies = "required-after:forge@[13.19.0.2142,)", acceptedMinecraftVersions = "[1.11, 1.12)")
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
            event.getModMetadata().version = String.format("%s.%s.%s build %s", major, minor, rev, build);
        }

        ChestChangerType.buildItems();
        ironChestBlock = GameRegistry.register(new BlockIronChest());
        ironChestItemBlock = GameRegistry.register(new ItemIronChest(ironChestBlock));

        for (IronChestType typ : IronChestType.VALUES)
        {
            if (typ.clazz != null)
            {
                GameRegistry.registerTileEntity(typ.clazz, "IronChest." + typ.name());
            }
        }

        IronChestType.registerBlocksAndRecipes(ironChestBlock);
        ChestChangerType.generateRecipes();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        proxy.registerRenderInformation();
        MinecraftForge.EVENT_BUS.register(new OcelotsSitOnChestsHandler());
    }

    @Mod.EventHandler
    public void onMissingMappings(FMLMissingMappingsEvent event)
    {
        for (MissingMapping mapping : event.get())
        {
            if (mapping.resourceLocation.getResourceDomain().equals(IronChest.MOD_ID))
            {
                @Nonnull
                String path = mapping.resourceLocation.getResourcePath();

                if (path.endsWith("blockironchest"))
                {
                    path = path.replace("blockironchest", "iron_chest");
                    ResourceLocation newRes = new ResourceLocation(mapping.resourceLocation.getResourceDomain(), path);
                    Block block = ForgeRegistries.BLOCKS.getValue(newRes);

                    if (block != null)
                    {
                        if (mapping.type == GameRegistry.Type.BLOCK)
                        {
                            mapping.remap(block);
                        }
                        else
                        {
                            mapping.remap(Item.getItemFromBlock(block));
                        }
                    }
                }

                if (path.endsWith("irongoldupgrade"))
                {
                    path = path.replace("irongoldupgrade", "iron_gold_upgrade");
                    replaceUpgradeItem(path, mapping);
                }

                if (path.endsWith("golddiamondupgrade"))
                {
                    path = path.replace("golddiamondupgrade", "gold_diamond_upgrade");
                    replaceUpgradeItem(path, mapping);
                }

                if (path.endsWith("coppersilverupgrade"))
                {
                    path = path.replace("coppersilverupgrade", "copper_silver_upgrade");
                    replaceUpgradeItem(path, mapping);
                }

                if (path.endsWith("silvergoldupgrade"))
                {
                    path = path.replace("silvergoldupgrade", "silver_gold_upgrade");
                    replaceUpgradeItem(path, mapping);
                }

                if (path.endsWith("copperironupgrade"))
                {
                    path = path.replace("copperironupgrade", "copper_iron_upgrade");
                    replaceUpgradeItem(path, mapping);
                }

                if (path.endsWith("diamondcrystalupgrade"))
                {
                    path = path.replace("diamondcrystalupgrade", "diamond_crystal_upgrade");
                    replaceUpgradeItem(path, mapping);
                }

                if (path.endsWith("woodironupgrade"))
                {
                    path = path.replace("woodironupgrade", "wood_iron_upgrade");
                    replaceUpgradeItem(path, mapping);
                }

                if (path.endsWith("woodcopperupgrade"))
                {
                    path = path.replace("woodcopperupgrade", "wood_copper_upgrade");
                    replaceUpgradeItem(path, mapping);
                }

                if (path.endsWith("diamondobsidianupgrade"))
                {
                    path = path.replace("diamondobsidianupgrade", "diamond_obsidian_upgrade");
                    replaceUpgradeItem(path, mapping);
                }
            }
        }
    }

    private static void replaceUpgradeItem(String path, MissingMapping mapping)
    {
        ResourceLocation newRes = new ResourceLocation(mapping.resourceLocation.getResourceDomain(), path);
        Item item = ForgeRegistries.ITEMS.getValue(newRes);

        if (item != null)
        {
            mapping.remap(item);
        }
    }
}
