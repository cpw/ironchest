/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * Contributors:
 *     cpw - initial API and implementation
 ******************************************************************************/
package cpw.mods.ironchest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.EnumMap;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public enum PacketHandler {
    INSTANCE;
    private EnumMap<Side, FMLEmbeddedChannel> channels;

    private PacketHandler()
    {
        this.channels = NetworkRegistry.INSTANCE.newChannel("IronChest", new IronChestCodec());
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            addClientHandler();
        }
    }

    @SideOnly(Side.CLIENT)
    private void addClientHandler() {
        FMLEmbeddedChannel clientChannel = this.channels.get(Side.CLIENT);
        String codec = clientChannel.findChannelHandlerNameForType(IronChestCodec.class);
        clientChannel.pipeline().addAfter(codec, "ClientHandler", new IronChestMessageHandler());
    }

    private static class IronChestMessageHandler extends SimpleChannelInboundHandler<IronChestMessage>
    {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, IronChestMessage msg) throws Exception
        {
            World world = IronChest.proxy.getClientWorld();
            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (te instanceof TileEntityIronChest)
            {
                TileEntityIronChest icte = (TileEntityIronChest) te;
                icte.setFacing(msg.facing);
                icte.handlePacketData(msg.type, msg.items);
            }
        }
    }
    public static class IronChestMessage
    {
        int x;
        int y;
        int z;
        int type;
        int facing;
        int[] items;
    }
    private class IronChestCodec extends FMLIndexedMessageToMessageCodec<IronChestMessage>
    {
        public IronChestCodec()
        {
            addDiscriminator(0, IronChestMessage.class);
        }
        @Override
        public void encodeInto(ChannelHandlerContext ctx, IronChestMessage msg, ByteBuf target) throws Exception
        {
            target.writeInt(msg.x);
            target.writeInt(msg.y);
            target.writeInt(msg.z);
            int typeAndFacing = ((msg.type & 0x0F) | ((msg.facing & 0x0F) << 4)) & 0xFF;
            target.writeByte(typeAndFacing);
            target.writeBoolean(msg.items != null);
            if (msg.items != null)
            {
                int[] items = msg.items;
                for (int j = 0; j < items.length; j++)
                {
                    int i = items[j];
                    target.writeInt(i);
                }
            }
        }

        @Override
        public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, IronChestMessage msg)
        {
            msg.x = dat.readInt();
            msg.y = dat.readInt();
            msg.z = dat.readInt();
            int typDat = dat.readByte();
            msg.type = (byte)(typDat & 0xf);
            msg.facing = (byte)((typDat >> 4) & 0xf);
            boolean hasStacks = dat.readBoolean();
            msg.items = new int[0];
            if (hasStacks)
            {
                msg.items = new int[24];
                for (int i = 0; i < msg.items.length; i++)
                {
                    msg.items[i] = dat.readInt();
                }
            }
        }

    }

    public static Packet getPacket(TileEntityIronChest tileEntityIronChest)
    {
        IronChestMessage msg = new IronChestMessage();
        msg.x = tileEntityIronChest.xCoord;
        msg.y = tileEntityIronChest.yCoord;
        msg.z = tileEntityIronChest.zCoord;
        msg.type = tileEntityIronChest.getType().ordinal();
        msg.facing = tileEntityIronChest.getFacing();
        msg.items = tileEntityIronChest.buildIntDataList();
        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }
}
