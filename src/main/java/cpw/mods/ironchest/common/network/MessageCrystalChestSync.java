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
package cpw.mods.ironchest.common.network;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityIronChest;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCrystalChestSync implements IMessage
{
    int dimension;
    BlockPos pos;
    private NonNullList<ItemStack> topStacks;

    public MessageCrystalChestSync(TileEntityIronChest tile, NonNullList<ItemStack> stack)
    {
        this.dimension = tile.getWorld().provider.getDimension();
        this.pos = tile.getPos();
        this.topStacks = stack;
    }

    public MessageCrystalChestSync()
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.dimension = buf.readInt();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());

        int size = buf.readInt();
        this.topStacks = NonNullList.<ItemStack> withSize(size, ItemStack.EMPTY);

        for (int i = 0; i < size; i++)
        {
            ItemStack itemstack = ByteBufUtils.readItemStack(buf);

            this.topStacks.set(i, itemstack);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.dimension);
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        buf.writeInt(this.topStacks.size());

        for (ItemStack stack : this.topStacks)
        {
            ByteBufUtils.writeItemStack(buf, stack);
        }
    }

    public static class Handler implements IMessageHandler<MessageCrystalChestSync, IMessage>
    {
        @Override
        public IMessage onMessage(MessageCrystalChestSync message, MessageContext ctx)
        {
            World world = IronChest.proxy.getClientWorld();

            if (world != null)
            {
                TileEntity tile = world.getTileEntity(message.pos);

                if (tile instanceof TileEntityIronChest)
                    ((TileEntityIronChest) tile).receiveMessageFromServer(message.topStacks);
            }

            return null;
        }
    }
}