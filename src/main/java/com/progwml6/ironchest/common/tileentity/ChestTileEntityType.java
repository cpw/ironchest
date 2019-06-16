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
package com.progwml6.ironchest.common.tileentity;

import com.progwml6.ironchest.IronChest;
import com.progwml6.ironchest.common.core.IronChestBlocks;
import com.progwml6.ironchest.common.util.TileEntityNames;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

public class ChestTileEntityType
{
    @ObjectHolder(TileEntityNames.IRON_CHEST)
    public static TileEntityType<?> IRON_CHEST;

    @ObjectHolder(TileEntityNames.GOLD_CHEST)
    public static TileEntityType<?> GOLD_CHEST;

    @ObjectHolder(TileEntityNames.DIAMOND_CHEST)
    public static TileEntityType<?> DIAMOND_CHEST;

    @ObjectHolder(TileEntityNames.CRYSTAL_CHEST)
    public static TileEntityType<?> CRYSTAL_CHEST;

    @ObjectHolder(TileEntityNames.DIRT_CHEST)
    public static TileEntityType<?> DIRT_CHEST;

    @ObjectHolder(TileEntityNames.COPPER_CHEST)
    public static TileEntityType<?> COPPER_CHEST;

    @ObjectHolder(TileEntityNames.SILVER_CHEST)
    public static TileEntityType<?> SILVER_CHEST;

    @ObjectHolder(TileEntityNames.OBSIDIAN_CHEST)
    public static TileEntityType<?> OBSIDIAN_CHEST;

    public ChestTileEntityType()
    {

    }

    @Mod.EventBusSubscriber(modid = IronChest.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration
    {
        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> e)
        {
            e.getRegistry().registerAll(
                    TileEntityType.Builder.create((Supplier<TileEntity>) IronChestTileEntity::new, IronChestBlocks.ironChestBlock).build(null).setRegistryName(TileEntityNames.IRON_CHEST),
                    TileEntityType.Builder.create((Supplier<TileEntity>) GoldChestTileEntity::new, IronChestBlocks.goldChestBlock).build(null).setRegistryName(TileEntityNames.GOLD_CHEST),
                    TileEntityType.Builder.create((Supplier<TileEntity>) DiamondChestTileEntity::new, IronChestBlocks.diamondChestBlock).build(null).setRegistryName(TileEntityNames.DIAMOND_CHEST),
                    TileEntityType.Builder.create((Supplier<TileEntity>) CrystalChestTileEntity::new, IronChestBlocks.crystalChestBlock).build(null).setRegistryName(TileEntityNames.CRYSTAL_CHEST),
                    TileEntityType.Builder.create((Supplier<TileEntity>) DirtChestTileEntity::new, IronChestBlocks.dirtChestBlock).build(null).setRegistryName(TileEntityNames.DIRT_CHEST),
                    TileEntityType.Builder.create((Supplier<TileEntity>) CopperChestTileEntity::new, IronChestBlocks.copperChestBlock).build(null).setRegistryName(TileEntityNames.COPPER_CHEST),
                    TileEntityType.Builder.create((Supplier<TileEntity>) SilverChestTileEntity::new, IronChestBlocks.silverChestBlock).build(null).setRegistryName(TileEntityNames.SILVER_CHEST),
                    TileEntityType.Builder.create((Supplier<TileEntity>) ObsidianChestTileEntity::new, IronChestBlocks.obsidianChestBlock).build(null).setRegistryName(TileEntityNames.OBSIDIAN_CHEST)
            );
        }
    }
}
