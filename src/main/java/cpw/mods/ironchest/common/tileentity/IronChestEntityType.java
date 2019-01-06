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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.fml.common.registry.GameRegistry;
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

    @SuppressWarnings("unchecked")
    public void registerTileEntities()
    {
        IForgeRegistry e = GameRegistry.findRegistry(TileEntityType.class);
        registerTileEntityType(e, register("iron_chest", TileEntityType.Builder.create(TileEntityIronChest::new)), "iron_chest");
        registerTileEntityType(e, register("gold_chest", TileEntityType.Builder.create(TileEntityGoldChest::new)), "gold_chest");
        registerTileEntityType(e, register("diamond_chest", TileEntityType.Builder.create(TileEntityDiamondChest::new)), "diamond_chest");
        registerTileEntityType(e, register("crystal_chest", TileEntityType.Builder.create(TileEntityCrystalChest::new)), "crystal_chest");
        registerTileEntityType(e, register("dirt_chest", TileEntityType.Builder.create(TileEntityDirtChest::new)), "dirt_chest");
        registerTileEntityType(e, register("copper_chest", TileEntityType.Builder.create(TileEntityCopperChest::new)), "copper_chest");
        registerTileEntityType(e, register("silver_chest", TileEntityType.Builder.create(TileEntitySilverChest::new)), "silver_chest");
        registerTileEntityType(e, register("obsidian_chest", TileEntityType.Builder.create(TileEntityObsidianChest::new)), "obsidian_chest");
    }

    @SuppressWarnings("unchecked")
    public void createEntries()
    {
        IForgeRegistry<?> e = GameRegistry.findRegistry(TileEntityType.class);

        IronChestEntityType.IRON_CHEST = (TileEntityType<?>) e.getValue(new ResourceLocation("ironchest", "iron_chest"));
        IronChestEntityType.GOLD_CHEST = (TileEntityType<?>) e.getValue(new ResourceLocation("ironchest", "gold_chest"));
        IronChestEntityType.DIAMOND_CHEST = (TileEntityType<?>) e.getValue(new ResourceLocation("ironchest", "diamond_chest"));
        IronChestEntityType.CRYSTAL_CHEST = (TileEntityType<?>) e.getValue(new ResourceLocation("ironchest", "crystal_chest"));
        IronChestEntityType.DIRT_CHEST = (TileEntityType<?>) e.getValue(new ResourceLocation("ironchest", "dirt_chest"));
        IronChestEntityType.COPPER_CHEST = (TileEntityType<?>) e.getValue(new ResourceLocation("ironchest", "copper_chest"));
        IronChestEntityType.SILVER_CHEST = (TileEntityType<?>) e.getValue(new ResourceLocation("ironchest", "silver_chest"));
        IronChestEntityType.OBSIDIAN_CHEST = (TileEntityType<?>) e.getValue(new ResourceLocation("ironchest", "obsidian_chest"));
    }

    /*@SubscribeEvent
    public static void onTileEntityRegistry(final Register<TileEntityType<?>> e)
    {
        System.out.println("hello from onTileEntityRegistry");
        registerTileEntityType(e.getRegistry(), register("iron_chest", TileEntityType.Builder.create(TileEntityIronChest::new)), "iron_chest");
        registerTileEntityType(e.getRegistry(), register("gold_chest", TileEntityType.Builder.create(TileEntityGoldChest::new)), "gold_chest");
        registerTileEntityType(e.getRegistry(), register("diamond_chest", TileEntityType.Builder.create(TileEntityDiamondChest::new)), "diamond_chest");
        registerTileEntityType(e.getRegistry(), register("crystal_chest", TileEntityType.Builder.create(TileEntityCrystalChest::new)), "crystal_chest");
        registerTileEntityType(e.getRegistry(), register("dirt_chest", TileEntityType.Builder.create(TileEntityDirtChest::new)), "dirt_chest");
        registerTileEntityType(e.getRegistry(), register("copper_chest", TileEntityType.Builder.create(TileEntityCopperChest::new)), "copper_chest");
        registerTileEntityType(e.getRegistry(), register("silver_chest", TileEntityType.Builder.create(TileEntitySilverChest::new)), "silver_chest");
        registerTileEntityType(e.getRegistry(), register("obsidian_chest", TileEntityType.Builder.create(TileEntityObsidianChest::new)), "obsidian_chest");
    
        IronChestEntityType.IRON_CHEST = e.getRegistry().getValue(new ResourceLocation("ironchest", "iron_chest"));
        IronChestEntityType.GOLD_CHEST = e.getRegistry().getValue(new ResourceLocation("ironchest", "gold_chest"));
        IronChestEntityType.DIAMOND_CHEST = e.getRegistry().getValue(new ResourceLocation("ironchest", "diamond_chest"));
        IronChestEntityType.CRYSTAL_CHEST = e.getRegistry().getValue(new ResourceLocation("ironchest", "crystal_chest"));
        IronChestEntityType.DIRT_CHEST = e.getRegistry().getValue(new ResourceLocation("ironchest", "dirt_chest"));
        IronChestEntityType.COPPER_CHEST = e.getRegistry().getValue(new ResourceLocation("ironchest", "copper_chest"));
        IronChestEntityType.SILVER_CHEST = e.getRegistry().getValue(new ResourceLocation("ironchest", "silver_chest"));
        IronChestEntityType.OBSIDIAN_CHEST = e.getRegistry().getValue(new ResourceLocation("ironchest", "obsidian_chest"));
    }*/

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

        TileEntityType<T> tileentitytype = builder.build(type);
        return tileentitytype;
    }
}
