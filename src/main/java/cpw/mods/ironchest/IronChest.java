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

import cpw.mods.ironchest.client.ClientProxy;
import cpw.mods.ironchest.common.ServerProxy;
import cpw.mods.ironchest.common.core.IronChestBlocks;
import cpw.mods.ironchest.common.core.IronChestItems;
import cpw.mods.ironchest.common.tileentity.IronChestEntityType;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;

@Mod(value = IronChest.MOD_ID)
public class IronChest
{
    public static final String MOD_ID = "ironchest";

    public static IronChest instance;

    public static ServerProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public IronChestBlocks ironChestBlocks = new IronChestBlocks();

    public IronChestItems ironChestItems = new IronChestItems();

    public IronChestEntityType ironChestEntityType = new IronChestEntityType();

    public IronChest()
    {
        instance = this;
        FMLModLoadingContext.get().getModEventBus().addListener(this::preInit);
    }

    private void preInit(final FMLPreInitializationEvent event)
    {
        proxy.preInit();
        ironChestBlocks.registerBlocks();
        ironChestBlocks.registerItems();
        ironChestItems.registerItems();

        ironChestEntityType.registerTileEntities();
        ironChestEntityType.createEntries();
    }
}
