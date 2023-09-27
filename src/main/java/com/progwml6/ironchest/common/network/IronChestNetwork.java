package com.progwml6.ironchest.common.network;

import com.progwml6.ironchest.IronChests;
import com.progwml6.ironchest.common.network.helper.ISimplePacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class IronChestNetwork {

  private static IronChestNetwork instance = null;

  public final SimpleChannel network;
  private int id = 0;
  private static final String PROTOCOL_VERSION = Integer.toString(1);

  public IronChestNetwork() {
    this.network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(IronChests.MOD_ID, "network"))
      .clientAcceptedVersions(PROTOCOL_VERSION::equals)
      .serverAcceptedVersions(PROTOCOL_VERSION::equals)
      .networkProtocolVersion(() -> PROTOCOL_VERSION)
      .simpleChannel();
  }

  /**
   * Gets the instance of the network
   */
  public static IronChestNetwork getInstance() {
    if (instance == null) {
      throw new IllegalStateException("Attempt to call network getInstance before network is setup");
    }

    return instance;
  }

  /**
   * Called during mod construction to setup the network
   */
  public static void setup() {
    if (instance != null) {
      return;
    }

    instance = new IronChestNetwork();
    instance.registerPacket(InventoryTopStacksSyncPacket.class, InventoryTopStacksSyncPacket::new, NetworkDirection.PLAY_TO_CLIENT);
  }

  /**
   * Registers a new {@link ISimplePacket}
   *
   * @param clazz   Packet class
   * @param decoder Packet decoder, typically the constructor
   * @param <MSG>   Packet class type
   */
  public <MSG extends ISimplePacket> void registerPacket(Class<MSG> clazz, Function<FriendlyByteBuf, MSG> decoder, @Nullable NetworkDirection direction) {
    registerPacket(clazz, ISimplePacket::encode, decoder, ISimplePacket::handle, direction);
  }

  /**
   * Registers a new generic packet
   *
   * @param clazz     Packet class
   * @param encoder   Encodes a packet to the buffer
   * @param decoder   Packet decoder, typically the constructor
   * @param consumer  Logic to handle a packet
   * @param direction Network direction for validation. Pass null for no direction
   * @param <MSG>     Packet class type
   */
  public <MSG> void registerPacket(Class<MSG> clazz, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> consumer, @Nullable NetworkDirection direction) {
    this.network.registerMessage(this.id++, clazz, encoder, decoder, consumer, Optional.ofNullable(direction));
  }

  /* Sending packets */

  /**
   * Sends a packet to the server
   *
   * @param msg Packet to send
   */
  public void sendToServer(Object msg) {
    this.network.sendToServer(msg);
  }

  /**
   * Sends a packet to the given packet distributor
   *
   * @param target  Packet target
   * @param message Packet to send
   */
  public void send(PacketDistributor.PacketTarget target, Object message) {
    network.send(target, message);
  }

  /**
   * Sends a vanilla packet to the given entity
   *
   * @param player Player receiving the packet
   * @param packet Packet
   */
  public void sendVanillaPacket(Packet<?> packet, Entity player) {
    if (player instanceof ServerPlayer && ((ServerPlayer) player).connection != null) {
      ((ServerPlayer) player).connection.send(packet);
    }
  }

  /**
   * Sends a packet to a player
   *
   * @param msg    Packet
   * @param player Player to send
   */
  public void sendTo(Object msg, Player player) {
    if (player instanceof ServerPlayer) {
      sendTo(msg, (ServerPlayer) player);
    }
  }

  /**
   * Sends a packet to a player
   *
   * @param msg    Packet
   * @param player Player to send
   */
  public void sendTo(Object msg, ServerPlayer player) {
    if (!(player instanceof FakePlayer)) {
      network.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
  }

  /**
   * Sends a packet to players near a location
   *
   * @param msg         Packet to send
   * @param serverWorld World instance
   * @param position    Position within range
   */
  public void sendToClientsAround(Object msg, ServerLevel serverWorld, BlockPos position) {
    LevelChunk chunk = serverWorld.getChunkAt(position);
    network.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), msg);
  }

  /**
   * Sends a packet to all entities tracking the given entity
   *
   * @param msg    Packet
   * @param entity Entity to check
   */
  public void sendToTrackingAndSelf(Object msg, Entity entity) {
    this.network.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
  }

  /**
   * Sends a packet to all entities tracking the given entity
   *
   * @param msg    Packet
   * @param entity Entity to check
   */
  public void sendToTracking(Object msg, Entity entity) {
    this.network.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
  }

  /**
   * Same as {@link #sendToClientsAround(Object, ServerLevel, BlockPos)}, but checks that the world is a level accessor
   *
   * @param msg      Packet to send
   * @param world    World instance
   * @param position Target position
   */
  public void sendToClientsAround(Object msg, @Nullable LevelAccessor world, BlockPos position) {
    if (world instanceof ServerLevel) {
      sendToClientsAround(msg, (ServerLevel) world, position);
    }
  }
}
