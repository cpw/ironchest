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
package cpw.mods.ironchest.common.network.packets;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.tileentity.TileEntityCrystalChest;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketTopStackSyncChest
{
    private final int dimension;

    private final BlockPos pos;

    private final NonNullList<ItemStack> topStacks;

    public PacketTopStackSyncChest(int dimension, BlockPos pos, NonNullList<ItemStack> topStacks)
    {
        this.dimension = dimension;
        this.pos = pos;
        this.topStacks = topStacks;
    }

    public static void encode(PacketTopStackSyncChest msg, PacketBuffer buf)
    {
        buf.writeInt(msg.dimension);
        buf.writeInt(msg.pos.getX());
        buf.writeInt(msg.pos.getY());
        buf.writeInt(msg.pos.getZ());
        buf.writeInt(msg.topStacks.size());

        for (ItemStack stack : msg.topStacks)
        {
            buf.writeItemStack(stack);
        }
    }

    public static PacketTopStackSyncChest decode(PacketBuffer buf)
    {
        int dimension = buf.readInt();
        BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

        int size = buf.readInt();
        NonNullList<ItemStack> topStacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);

        for (int item = 0; item < size; item++)
        {
            ItemStack itemStack = buf.readItemStack();

            topStacks.set(item, itemStack);
        }

        return new PacketTopStackSyncChest(dimension, pos, topStacks);
    }

    public static class Handler
    {
        public static void handle(final PacketTopStackSyncChest message, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                World world = IronChest.proxy.getClientWorld();

                if (world != null)
                {
                    TileEntity tile = world.getTileEntity(message.pos);

                    if (tile instanceof TileEntityCrystalChest)
                    {
                        ((TileEntityCrystalChest) tile).receiveMessageFromServer(message.topStacks);
                    }
                }
            });
        }
    }

}
