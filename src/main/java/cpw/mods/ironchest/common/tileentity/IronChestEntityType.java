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
package cpw.mods.ironchest.common.tileentity;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import cpw.mods.ironchest.IronChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class IronChestEntityType
{
    public static TileEntityType<?> IRON_CHEST;

    public static TileEntityType<?> GOLD_CHEST;

    public static TileEntityType<?> DIAMOND_CHEST;

    public static TileEntityType<?> CRYSTAL_CHEST;

    public static TileEntityType<?> DIRT_CHEST;

    public static TileEntityType<?> COPPER_CHEST;

    public static TileEntityType<?> SILVER_CHEST;

    public static TileEntityType<?> OBSIDIAN_CHEST;

    public IronChestEntityType()
    {

    }

    @Mod.EventBusSubscriber(modid = IronChest.MOD_ID)
    public static class Registration
    {
        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> e)
        {
            IronChestEntityType.IRON_CHEST = registerTileEntityType(e.getRegistry(),
                    register("iron_chest", TileEntityType.Builder.create(TileEntityIronChest::new)), "iron_chest");
            IronChestEntityType.GOLD_CHEST = registerTileEntityType(e.getRegistry(),
                    register("gold_chest", TileEntityType.Builder.create(TileEntityGoldChest::new)), "gold_chest");
            IronChestEntityType.DIAMOND_CHEST = registerTileEntityType(e.getRegistry(),
                    register("diamond_chest", TileEntityType.Builder.create(TileEntityDiamondChest::new)), "diamond_chest");
            IronChestEntityType.CRYSTAL_CHEST = registerTileEntityType(e.getRegistry(),
                    register("crystal_chest", TileEntityType.Builder.create(TileEntityCrystalChest::new)), "crystal_chest");
            IronChestEntityType.DIRT_CHEST = registerTileEntityType(e.getRegistry(),
                    register("dirt_chest", TileEntityType.Builder.create(TileEntityDirtChest::new)), "dirt_chest");
            IronChestEntityType.COPPER_CHEST = registerTileEntityType(e.getRegistry(),
                    register("copper_chest", TileEntityType.Builder.create(TileEntityCopperChest::new)), "copper_chest");
            IronChestEntityType.SILVER_CHEST = registerTileEntityType(e.getRegistry(),
                    register("silver_chest", TileEntityType.Builder.create(TileEntitySilverChest::new)), "silver_chest");
            IronChestEntityType.OBSIDIAN_CHEST = registerTileEntityType(e.getRegistry(),
                    register("obsidian_chest", TileEntityType.Builder.create(TileEntityObsidianChest::new)), "obsidian_chest");
        }
    }

    protected static <T extends TileEntityType<?>> T registerTileEntityType(IForgeRegistry<TileEntityType<?>> registry, T tileEntityType, String name)
    {
        register(registry, tileEntityType, new ResourceLocation("ironchest", name));
        return tileEntityType;
    }

    protected static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, T thing, ResourceLocation name)
    {
        thing.setRegistryName(name);
        registry.register(thing);
        return thing;
    }

    public static <T extends TileEntity> TileEntityType<T> register(String id, TileEntityType.Builder<T> builder)
    {
        Type<?> type = null;

        try
        {
            type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(1519)).getChoiceType(TypeReferences.BLOCK_ENTITY, id);
        }
        catch (IllegalArgumentException illegalstateexception)
        {
            if (SharedConstants.developmentMode)
            {
                throw illegalstateexception;
            }
        }

        TileEntityType<T> tileEntityType = builder.build(type);
        return tileEntityType;
    }
}
