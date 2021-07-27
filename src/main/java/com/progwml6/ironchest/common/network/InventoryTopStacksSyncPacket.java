package com.progwml6.ironchest.common.network;

import com.progwml6.ironchest.common.block.tileentity.CrystalChestTileEntity;
import com.progwml6.ironchest.common.network.helper.IThreadsafePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class InventoryTopStacksSyncPacket implements IThreadsafePacket {

  private final BlockPos pos;
  private final NonNullList<ItemStack> topStacks;

  public InventoryTopStacksSyncPacket(NonNullList<ItemStack> topStacks, BlockPos pos) {
    this.topStacks = topStacks;
    this.pos = pos;
  }

  public InventoryTopStacksSyncPacket(PacketBuffer buffer) {
    int size = buffer.readInt();
    NonNullList<ItemStack> topStacks = NonNullList.<ItemStack>withSize(size, ItemStack.EMPTY);

    for (int item = 0; item < size; item++) {
      ItemStack itemStack = buffer.readItemStack();

      topStacks.set(item, itemStack);
    }

    this.topStacks = topStacks;

    this.pos = buffer.readBlockPos();
  }

  @Override
  public void encode(PacketBuffer packetBuffer) {
    packetBuffer.writeInt(this.topStacks.size());

    for (ItemStack stack : this.topStacks) {
      packetBuffer.writeItemStack(stack);
    }

    packetBuffer.writeBlockPos(this.pos);
  }

  @Override
  public void handleThreadsafe(Context context) {
    HandleClient.handle(this);
  }

  /**
   * Safely runs client side only code in a method only called on client
   */
  private static class HandleClient {

    private static void handle(InventoryTopStacksSyncPacket packet) {
      World world = Minecraft.getInstance().world;

      if (world != null) {
        TileEntity te = world.getTileEntity(packet.pos);

        if (te != null) {
          if (te instanceof CrystalChestTileEntity) {
            ((CrystalChestTileEntity) te).receiveMessageFromServer(packet.topStacks);
            Minecraft.getInstance().worldRenderer.notifyBlockUpdate(null, packet.pos, null, null, 0);
          }
        }
      }
    }
  }
}
