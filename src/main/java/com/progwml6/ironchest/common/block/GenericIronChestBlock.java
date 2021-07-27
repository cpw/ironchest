package com.progwml6.ironchest.common.block;

import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class GenericIronChestBlock extends Block implements SimpleWaterloggedBlock {

  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  protected static final VoxelShape IRON_CHEST_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

  private final IronChestsTypes type;
  private final Supplier<BlockEntityType<? extends GenericIronChestTileEntity>> tileEntityTypeSupplier;

  public GenericIronChestBlock(IronChestsTypes typeIn, Supplier<BlockEntityType<? extends GenericIronChestTileEntity>> tileEntityTypeSupplierIn, Properties propertiesIn) {
    super(propertiesIn);

    this.type = typeIn;
    this.tileEntityTypeSupplier = tileEntityTypeSupplierIn;

    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.valueOf(false)));
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.ENTITYBLOCK_ANIMATED;
  }

  @Override
  public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
    if (stateIn.getValue(WATERLOGGED)) {
      worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
    }

    return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return IRON_CHEST_SHAPE;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    Direction direction = context.getHorizontalDirection().getOpposite();
    FluidState ifluidstate = context.getLevel().getFluidState(context.getClickedPos());

    return this.defaultBlockState().setValue(FACING, direction).setValue(WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    BlockEntity tileentity = worldIn.getBlockEntity(pos);

    if (tileentity instanceof GenericIronChestTileEntity) {
      ((GenericIronChestTileEntity) tileentity).wasPlaced(placer, stack);

      if (stack.hasCustomHoverName()) {
        ((GenericIronChestTileEntity) tileentity).setCustomName(stack.getHoverName());
      }
    }
  }

  @Override
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      BlockEntity tileentity = worldIn.getBlockEntity(pos);

      if (tileentity instanceof GenericIronChestTileEntity) {
        ((GenericIronChestTileEntity) tileentity).removeAdornments();
        Containers.dropContents(worldIn, pos, (GenericIronChestTileEntity) tileentity);
        worldIn.updateNeighbourForOutputSignal(pos, this);
      }

      super.onRemove(state, worldIn, pos, newState, isMoving);
    }
  }

  @Override
  public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
    if (!worldIn.isClientSide) {
      MenuProvider inamedcontainerprovider = this.getMenuProvider(state, worldIn, pos);
      if (inamedcontainerprovider != null) {
        player.openMenu(inamedcontainerprovider);
        player.awardStat(this.getOpenStat());
      }

    }
    return InteractionResult.SUCCESS;
  }

  protected Stat<ResourceLocation> getOpenStat() {
    return Stats.CUSTOM.get(Stats.OPEN_CHEST);
  }

  @Override
  @Nullable
  public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
    BlockEntity tileentity = world.getBlockEntity(pos);
    return tileentity instanceof MenuProvider ? (MenuProvider) tileentity : null;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public boolean triggerEvent(BlockState state, Level worldIn, BlockPos pos, int id, int param) {
    super.triggerEvent(state, worldIn, pos, id, param);
    BlockEntity tileentity = worldIn.getBlockEntity(pos);
    return tileentity == null ? false : tileentity.triggerEvent(id, param);
  }

  private static boolean isBlocked(LevelAccessor iWorld, BlockPos blockPos) {
    return isBelowSolidBlock(iWorld, blockPos) || isCatSittingOn(iWorld, blockPos);
  }

  private static boolean isBelowSolidBlock(BlockGetter iBlockReader, BlockPos worldIn) {
    BlockPos blockpos = worldIn.above();
    return iBlockReader.getBlockState(blockpos).isRedstoneConductor(iBlockReader, blockpos);
  }

  private static boolean isCatSittingOn(LevelAccessor iWorld, BlockPos blockPos) {
    List<Cat> list = iWorld.getEntitiesOfClass(Cat.class, new AABB((double) blockPos.getX(), (double) (blockPos.getY() + 1), (double) blockPos.getZ(), (double) (blockPos.getX() + 1), (double) (blockPos.getY() + 2), (double) (blockPos.getZ() + 1)));
    if (!list.isEmpty()) {
      for (Cat catentity : list) {
        if (catentity.isSleeping()) {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
    return AbstractContainerMenu.getRedstoneSignalFromContainer((Container) worldIn.getBlockEntity(pos));
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rot) {
    return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirrorIn) {
    return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, WATERLOGGED);
  }

  @Override
  public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
    return false;
  }

  public static IronChestsTypes getTypeFromItem(Item itemIn) {
    return getTypeFromBlock(Block.byItem(itemIn));
  }

  public static IronChestsTypes getTypeFromBlock(Block blockIn) {
    return blockIn instanceof GenericIronChestBlock ? ((GenericIronChestBlock) blockIn).getType() : null;
  }

  public IronChestsTypes getType() {
    return this.type;
  }

  @OnlyIn(Dist.CLIENT)
  public static DoubleBlockCombiner.Combiner<GenericIronChestTileEntity, Float2FloatFunction> getLid(final LidBlockEntity p_226917_0_) {
    return new DoubleBlockCombiner.Combiner<GenericIronChestTileEntity, Float2FloatFunction>() {
      @Override
      public Float2FloatFunction acceptDouble(GenericIronChestTileEntity p_225539_1_, GenericIronChestTileEntity p_225539_2_) {
        return (p_226921_2_) -> {
          return Math.max(p_225539_1_.getOpenNess(p_226921_2_), p_225539_2_.getOpenNess(p_226921_2_));
        };
      }

      @Override
      public Float2FloatFunction acceptSingle(GenericIronChestTileEntity p_225538_1_) {
        return p_225538_1_::getOpenNess;
      }

      @Override
      public Float2FloatFunction acceptNone() {
        return p_226917_0_::getOpenNess;
      }
    };
  }

  public DoubleBlockCombiner.NeighborCombineResult<? extends GenericIronChestTileEntity> getWrapper(BlockState blockState, Level world, BlockPos blockPos, boolean p_225536_4_) {
    BiPredicate<LevelAccessor, BlockPos> biPredicate;
    if (p_225536_4_) {
      biPredicate = (p_226918_0_, p_226918_1_) -> false;
    }
    else {
      biPredicate = GenericIronChestBlock::isBlocked;
    }

    return DoubleBlockCombiner.combineWithNeigbour(this.tileEntityTypeSupplier.get(), GenericIronChestBlock::getMergerType, GenericIronChestBlock::getDirectionToAttached, FACING, blockState, world, blockPos, biPredicate);
  }

  public static DoubleBlockCombiner.BlockType getMergerType(BlockState blockState) {
    return DoubleBlockCombiner.BlockType.SINGLE;
  }

  public static Direction getDirectionToAttached(BlockState state) {
    Direction direction = state.getValue(FACING);
    return direction.getCounterClockWise();
  }
}
