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
package cpw.mods.ironchest.common.blocks.shulker;

import java.util.List;

import javax.annotation.Nullable;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.core.IronChestBlocks;
import cpw.mods.ironchest.common.core.IronChestCreativeTabs;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityIronShulkerBox;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockIronShulkerBox extends Block
{
    public static final PropertyEnum<IronShulkerBoxType> VARIANT_PROP = PropertyEnum.create("variant", IronShulkerBoxType.class);

    private final EnumDyeColor color;

    private EnumFacing facingDirection;

    public BlockIronShulkerBox(EnumDyeColor colorIn, String nameIn)
    {
        super(Material.IRON);

        this.color = colorIn;
        this.setRegistryName(nameIn);
        this.setUnlocalizedName("IronShulkerBox" + colorIn.getName());
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, IronShulkerBoxType.IRON));
        this.setHardness(3.0F);
        this.setCreativeTab(IronChestCreativeTabs.tabIronChests);
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean causesSuffocation(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return true;
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only, LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    //@formatter:off
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing heldItem, float side, float hitX, float hitY)
    //@formatter:on
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else if (playerIn.isSpectator())
        {
            return true;
        }
        else
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityIronShulkerBox)
            {
                EnumFacing enumfacing = ((TileEntityIronShulkerBox) tileentity).getFacing();
                boolean flag;

                if (((TileEntityIronShulkerBox) tileentity).getAnimationStatus() == TileEntityIronShulkerBox.AnimationStatus.CLOSED)
                {
                    //@formatter:off
                    AxisAlignedBB axisalignedbb = FULL_BLOCK_AABB.expand(0.5F * enumfacing.getFrontOffsetX(), 0.5F * enumfacing.getFrontOffsetY(), 0.5F * enumfacing.getFrontOffsetZ()).contract(enumfacing.getFrontOffsetX(), enumfacing.getFrontOffsetY(), enumfacing.getFrontOffsetZ());
                    //@formatter:on

                    flag = !worldIn.collidesWithAnyBlock(axisalignedbb.offset(pos.offset(enumfacing)));
                }
                else
                {
                    flag = true;
                }

                if (flag)
                {
                    //@formatter:off
                    playerIn.openGui(IronChest.instance, ((TileEntityIronShulkerBox) tileentity).getType().ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
                    //@formatter:on
                }

                return true;
            }
            else
            {
                return false;
            }
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return state.getValue(VARIANT_PROP).makeEntity(this.color);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (IronShulkerBoxType type : IronShulkerBoxType.VALUES)
        {
            if (type.isValidForCreativeMode())
            {
                list.add(new ItemStack(this, 1, type.ordinal()));
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT_PROP, IronShulkerBoxType.VALUES[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState blockState)
    {
        return blockState.getValue(VARIANT_PROP).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, VARIANT_PROP);
    }

    @Override
    //@formatter:off
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    //@formatter:on
    {
        this.facingDirection = facing;

        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(world, pos, state);

        world.notifyBlockUpdate(pos, state, state, 3);
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityIronShulkerBox)
            {
                ((TileEntityIronShulkerBox) tileentity).setCustomName(stack.getDisplayName());

                ((TileEntityIronShulkerBox) tileentity).setFacing(facingDirection);

                worldIn.notifyBlockUpdate(pos, state, state, 3);
            }
        }
        else
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityIronShulkerBox)
            {
                TileEntityIronShulkerBox teic = (TileEntityIronShulkerBox) tileentity;

                teic.setFacing(facingDirection);

                worldIn.notifyBlockUpdate(pos, state, state, 3);
            }
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(VARIANT_PROP).ordinal();
    }

    /**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     *
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     *
     * @param state The current state.
     * @param world The current world
     * @param player The player damaging the block, may be null
     * @param pos Block position in world
     * @param willHarvest True if Block.harvestBlock will be called after this, if the return in true.
     *        Can be useful to delay the destruction of tile entities till after harvestBlock
     * @return True if the block is actually destroyed.
     */
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        TileEntityIronShulkerBox tileentityironshulkerbox = (TileEntityIronShulkerBox) worldIn.getTileEntity(pos);

        tileentityironshulkerbox.setDestroyedByCreativePlayer(player.capabilities.isCreativeMode);
        tileentityironshulkerbox.fillWithLoot(player);
    }

    /**
     * This gets a complete list of items dropped from this block.
     *
     * @param drops add all items this block drops to this drops list
     * @param world The current world
     * @param pos Block position in world
     * @param state Current state
     * @param fortune Breakers fortune level
     */
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TileEntityIronShulkerBox)
        {
            ItemStack itemstack = ((TileEntityIronShulkerBox) tileentity).getDrop(state, false);
            if(!itemstack.isEmpty())
            {
                drops.add(itemstack);
            }
        }
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityIronShulkerBox)
        {
            ItemStack itemstack = ((TileEntityIronShulkerBox) tileentity).getDrop(state, true);
            if(!itemstack.isEmpty())
            {
                spawnAsEntity(worldIn, pos, itemstack);
            }

            worldIn.updateComparatorOutputLevel(pos, state.getBlock());
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileEntityIronShulkerBox)
        {
            TileEntityIronShulkerBox teic = (TileEntityIronShulkerBox) te;

            if (teic.getType().isExplosionResistant())
            {
                return 10000F;
            }
        }

        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag advanced)
    {
        super.addInformation(stack, worldIn, tooltip, advanced);

        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null && nbttagcompound.hasKey("BlockEntityTag", 10))
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("BlockEntityTag");

            if (nbttagcompound1.hasKey("LootTable", 8))
            {
                tooltip.add("???????");
            }

            if (nbttagcompound1.hasKey("Items", 9))
            {
                if (nbttagcompound1.hasKey("ShulkerBoxSize", 3))
                {
                    NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack> withSize(nbttagcompound1.getInteger("ShulkerBoxSize"), ItemStack.EMPTY);
                    ItemStackHelper.loadAllItems(nbttagcompound1, nonnulllist);
                    int i = 0;
                    int j = 0;

                    for (ItemStack itemstack : nonnulllist)
                    {
                        if (!itemstack.isEmpty())
                        {
                            ++j;

                            if (i <= 4)
                            {
                                ++i;
                                tooltip.add(String.format("%s x%d", new Object[] { itemstack.getDisplayName(), Integer.valueOf(itemstack.getCount()) }));
                            }
                        }
                    }

                    if (j - i > 0)
                    {
                        //@formatter:off
                        tooltip.add(String.format(TextFormatting.ITALIC + I18n.translateToLocal("container.shulkerBox.more"), new Object[] { Integer.valueOf(j - i) }));
                        //@formatter:on
                    }
                }
                else
                {
                    NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack> withSize(27, ItemStack.EMPTY);
                    ItemStackHelper.loadAllItems(nbttagcompound1, nonnulllist);
                    int i = 0;
                    int j = 0;

                    for (ItemStack itemstack : nonnulllist)
                    {
                        if (!itemstack.isEmpty())
                        {
                            ++j;

                            if (i <= 4)
                            {
                                ++i;
                                tooltip.add(String.format("%s x%d", new Object[] { itemstack.getDisplayName(), Integer.valueOf(itemstack.getCount()) }));
                            }
                        }
                    }

                    if (j - i > 0)
                    {
                        //@formatter:off
                        tooltip.add(String.format(TextFormatting.ITALIC + I18n.translateToLocal("container.shulkerBox.more"), new Object[] { Integer.valueOf(j - i) }));
                        //@formatter:on
                    }
                }
            }
        }
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        TileEntity tileentity = source.getTileEntity(pos);

        return tileentity instanceof TileEntityIronShulkerBox ? ((TileEntityIronShulkerBox) tileentity).getBoundingBox() : FULL_BLOCK_AABB;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstoneFromInventory((IInventory) worldIn.getTileEntity(pos));
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        ItemStack itemstack = super.getItem(worldIn, pos, state);
        TileEntityIronShulkerBox tileentityironshulkerbox = (TileEntityIronShulkerBox) worldIn.getTileEntity(pos);
        NBTTagCompound nbttagcompound = tileentityironshulkerbox.saveToNbt(new NBTTagCompound());

        if (!nbttagcompound.hasNoTags())
        {
            itemstack.setTagInfo("BlockEntityTag", nbttagcompound);
        }

        return itemstack;
    }

    public static Block getBlockByColor(EnumDyeColor colorIn)
    {
        switch (colorIn)
        {
        case WHITE:
            return IronChestBlocks.ironShulkerBoxWhiteBlock;
        case ORANGE:
            return IronChestBlocks.ironShulkerBoxOrangeBlock;
        case MAGENTA:
            return IronChestBlocks.ironShulkerBoxMagentaBlock;
        case LIGHT_BLUE:
            return IronChestBlocks.ironShulkerBoxLightBlueBlock;
        case YELLOW:
            return IronChestBlocks.ironShulkerBoxYellowBlock;
        case LIME:
            return IronChestBlocks.ironShulkerBoxLimeBlock;
        case PINK:
            return IronChestBlocks.ironShulkerBoxPinkBlock;
        case GRAY:
            return IronChestBlocks.ironShulkerBoxGrayBlock;
        case SILVER:
            return IronChestBlocks.ironShulkerBoxSilverBlock;
        case CYAN:
            return IronChestBlocks.ironShulkerBoxCyanBlock;
        case PURPLE:
        default:
            return IronChestBlocks.ironShulkerBoxPurpleBlock;
        case BLUE:
            return IronChestBlocks.ironShulkerBoxBlueBlock;
        case BROWN:
            return IronChestBlocks.ironShulkerBoxBrownBlock;
        case GREEN:
            return IronChestBlocks.ironShulkerBoxGreenBlock;
        case RED:
            return IronChestBlocks.ironShulkerBoxRedBlock;
        case BLACK:
            return IronChestBlocks.ironShulkerBoxBlackBlock;
        }
    }

    public static ItemStack getColoredItemStack(EnumDyeColor colorIn, int damageIn)
    {
        return new ItemStack(getBlockByColor(colorIn), 1, damageIn);
    }

    @SideOnly(Side.CLIENT)
    public static EnumDyeColor getColorFromBlock(Block blockIn)
    {
        return blockIn instanceof BlockIronShulkerBox ? ((BlockIronShulkerBox) blockIn).getColor() : EnumDyeColor.PURPLE;
    }

    @SideOnly(Side.CLIENT)
    public EnumDyeColor getColor()
    {
        return this.color;
    }

    @Override
    @Deprecated
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);

        TileEntity tileentity = worldIn.getTileEntity(pos);

        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }
}
