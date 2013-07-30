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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {
    @Override
    public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player)
    {
        ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
        int x = dat.readInt();
        int y = dat.readInt();
        int z = dat.readInt();
        int typDat = dat.readByte();
        byte typ = (byte)(typDat & 0xf);
        byte facing = (byte)((typDat >> 4) & 0xf);
        boolean hasStacks = dat.readByte() != 0;
        int[] items = new int[0];
        if (hasStacks)
        {
            items = new int[24];
            for (int i = 0; i < items.length; i++)
            {
                items[i] = dat.readInt();
            }
        }
        World world = IronChest.proxy.getClientWorld();
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityIronChest)
        {
            TileEntityIronChest icte = (TileEntityIronChest) te;
            icte.setFacing(facing);
            icte.handlePacketData(typ, items);
        }
    }

    public static Packet getPacket(TileEntityIronChest tileEntityIronChest)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
        DataOutputStream dos = new DataOutputStream(bos);
        int x = tileEntityIronChest.xCoord;
        int y = tileEntityIronChest.yCoord;
        int z = tileEntityIronChest.zCoord;
        int typ = (tileEntityIronChest.getType().ordinal() | (tileEntityIronChest.getFacing() << 4)) & 0xFF;
        int[] items = tileEntityIronChest.buildIntDataList();
        boolean hasStacks = (items != null);
        try
        {
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(z);
            dos.writeByte(typ);
            dos.writeByte(hasStacks ? 1 : 0);
            if (hasStacks)
            {
                for (int i = 0; i < 24; i++)
                {
                    dos.writeInt(items[i]);
                }
            }
        }
        catch (IOException e)
        {
            // UNPOSSIBLE?
        }
        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "IronChest";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = true;
        return pkt;
    }
}
