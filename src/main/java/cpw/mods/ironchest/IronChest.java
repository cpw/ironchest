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
import cpw.mods.ironchest.common.ai.OcelotsSitOnChestsHandler;
import cpw.mods.ironchest.common.blocks.BlockChest;
import cpw.mods.ironchest.common.network.PacketHandler;
import cpw.mods.ironchest.common.util.BlockNames;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreRegistrationEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;

@Mod(value = IronChest.MOD_ID)
public class IronChest
{
    public static final String MOD_ID = "ironchest";

    private static final boolean DEBUG = false;

    public static IronChest instance;

    public static ServerProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public IronChest()
    {
        instance = this;
        FMLModLoadingContext.get().getModEventBus().addListener(this::preInit);
        MinecraftForge.EVENT_BUS.register(new OcelotsSitOnChestsHandler());
    }

    private void preInit(final FMLPreRegistrationEvent event)
    {
        proxy.preInit();

        if (IronChest.DEBUG)
        {
            debugPrints();
        }

        PacketHandler.register();
    }

    private void debugPrints()
    {
        EnumFacing[] e = { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST };

        System.out.println("--------------------------");

        for (EnumFacing facing : e)
        {
            IBlockState iBlockState = Block.func_149684_b(BlockNames.IRON_CHEST).getDefaultState().with(BlockChest.FACING, facing);

            System.out.println("iBlockState " + iBlockState);

            int stateID = Block.getStateId(iBlockState);

            System.out.println("stateID " + stateID);

            IBlockState iBlockStateOut = Block.getStateById(stateID);

            System.out.println("iBlockStateOut " + iBlockStateOut);

            Block blockOut = Block.func_149729_e(stateID);

            System.out.println("blockOut " + blockOut);

            System.out.println("--------------------------");
        }

        System.out.println("--------------------------");

        for (Object i : Block.BLOCK_STATE_IDS)
        {
            System.out.println("Test: " + i);
        }
        System.out.println("--------------------------");
    }
}
