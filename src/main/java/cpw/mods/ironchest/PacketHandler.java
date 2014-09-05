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

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.EnumMap;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Handles the packet wrangling for IronChest
 * @author cpw
 *
 */
public enum PacketHandler {
    INSTANCE;

    /**
     * Our channel "pair" from {@link NetworkRegistry}
     */
    private EnumMap<Side, FMLEmbeddedChannel> channels;

    /**
     * Make our packet handler, and add an {@link IronChestCodec} always
     */
    private PacketHandler()
    {
        // request a channel pair for IronChest from the network registry
        // Add the IronChestCodec as a member of both channel pipelines
        this.channels = NetworkRegistry.INSTANCE.newChannel("IronChest", new IronChestCodec());
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            addClientHandler();
        }
    }

    /**
     * This is only called on the client side - it adds an
     * {@link IronChestMessageHandler} to the client side pipeline, since the
     * only place we expect to <em>handle</em> messages is on the client.
     */
    @SideOnly(Side.CLIENT)
    private void addClientHandler() {
        FMLEmbeddedChannel clientChannel = this.channels.get(Side.CLIENT);
        // These two lines find the existing codec (Ironchestcodec) and insert our message handler after it
        // in the pipeline
        String codec = clientChannel.findChannelHandlerNameForType(IronChestCodec.class);
        clientChannel.pipeline().addAfter(codec, "ClientHandler", new IronChestMessageHandler());
    }

    /**
     * This class simply handles the {@link IronChestMessage} when it's received
     * at the client side It can contain client only code, because it's only run
     * on the client.
     *
     * @author cpw
     *
     */
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
                icte.handlePacketData(msg.type, msg.itemStacks);
            }
        }
    }

    /**
     * This is our "message". In fact, {@link FMLIndexedMessageToMessageCodec}
     * can handle many messages on the same channel at once, using a
     * discriminator byte. But for IronChest, we only need the one message, so
     * we have just this.
     *
     * @author cpw
     *
     */
    public static class IronChestMessage
    {
        int x;
        int y;
        int z;
        int type;
        int facing;
        ItemStack[] itemStacks;
    }

    /**
     * This is the codec that automatically transforms the
     * {@link FMLProxyPacket} which wraps the client and server custom payload
     * packets into a message we care about.
     *
     * @author cpw
     *
     */
    private class IronChestCodec extends FMLIndexedMessageToMessageCodec<IronChestMessage>
    {
        /**
         * We register our discriminator bytes here. We only have the one type, so we only
         * register one.
         */
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
            target.writeBoolean(msg.itemStacks != null);
            if (msg.itemStacks != null)
            {
                for (ItemStack i: msg.itemStacks)
                {
                    ByteBufUtils.writeItemStack(target, i);
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
            msg.itemStacks = new ItemStack[0];
            if (hasStacks)
            {
                msg.itemStacks = new ItemStack[8];
                for (int i = 0; i < msg.itemStacks.length; i++)
                {
                    msg.itemStacks[i] = ByteBufUtils.readItemStack(dat);
                }
            }
        }

    }

    /**
     * This is a utility method called to transform a packet from a custom
     * packet into a "system packet". We're called from
     * {@link TileEntity#getDescriptionPacket()} in this case, but there are
     * others. All network packet methods in minecraft have been adapted to
     * handle {@link FMLProxyPacket} but general purpose objects can't be
     * handled sadly.
     *
     * This method uses the {@link IronChestCodec} to transform a custom packet
     * {@link IronChestMessage} into an {@link FMLProxyPacket} by using the
     * utility method {@link FMLEmbeddedChannel#generatePacketFrom(Object)} on
     * the channel to do exactly that.
     *
     * @param tileEntityIronChest
     * @return
     */
    public static Packet getPacket(TileEntityIronChest tileEntityIronChest)
    {
        IronChestMessage msg = new IronChestMessage();
        msg.x = tileEntityIronChest.xCoord;
        msg.y = tileEntityIronChest.yCoord;
        msg.z = tileEntityIronChest.zCoord;
        msg.type = tileEntityIronChest.getType().ordinal();
        msg.facing = tileEntityIronChest.getFacing();
        msg.itemStacks = tileEntityIronChest.buildItemStackDataList();
        return INSTANCE.channels.get(Side.SERVER).generatePacketFrom(msg);
    }
}
