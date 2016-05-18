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

import java.util.List;
import java.util.Random;

import cpw.mods.ironchest.client.IronChestTextureHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockIronChest extends Block
{
    public static final PropertyEnum<IronChestType> VARIANT_PROP = PropertyEnum.create("variant", IronChestType.class);
    protected static final AxisAlignedBB IRON_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

    public BlockIronChest()
    {
        super(Material.IRON);
        this.setRegistryName(new ResourceLocation(IronChest.MOD_ID, "BlockIronChest"));

        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, IronChestType.IRON));

        this.setHardness(3.0F);
        this.setUnlocalizedName("IronChest");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return IRON_CHEST_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    //@formatter:off
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState blockState, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing direction, float hitX, float hitY, float hitZ)
    //@formatter:on
    {
        TileEntity te = world.getTileEntity(pos);

        if (te == null || !(te instanceof TileEntityIronChest))
        {
            return true;
        }

        if (world.isSideSolid(pos.add(0, 1, 0), EnumFacing.DOWN))
        {
            return true;
        }

        if (world.isRemote)
        {
            return true;
        }

        player.openGui(IronChest.instance, ((TileEntityIronChest) te).getType().ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return state.getValue(VARIANT_PROP).makeEntity();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        for (IronChestType type : IronChestType.VALUES)
        {
            if (type.isValidForCreativeMode())
            {
                list.add(new ItemStack(itemIn, 1, type.ordinal()));
            }
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT_PROP, IronChestType.VALUES[meta]);
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
    public void onBlockAdded(World world, BlockPos pos, IBlockState blockState)
    {
        super.onBlockAdded(world, pos, blockState);
        world.notifyBlockUpdate(pos, blockState, blockState, 3);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState blockState, EntityLivingBase entityliving, ItemStack itemStack)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te instanceof TileEntityIronChest)
        {
            TileEntityIronChest teic = (TileEntityIronChest) te;
            teic.wasPlaced(entityliving, itemStack);
            teic.setFacing(entityliving.getHorizontalFacing().getOpposite());
            world.notifyBlockUpdate(pos, blockState, blockState, 3);
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(VARIANT_PROP).ordinal();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState)
    {
        TileEntityIronChest tileentitychest = (TileEntityIronChest) world.getTileEntity(pos);
        if (tileentitychest != null)
        {
            tileentitychest.removeAdornments();
            this.dropContent(0, tileentitychest, world, tileentitychest.getPos());
        }
        super.breakBlock(world, pos, blockState);
    }

    public void dropContent(int newSize, IInventory chest, World world, BlockPos pos)
    {
        Random random = world.rand;

        for (int l = newSize; l < chest.getSizeInventory(); l++)
        {
            ItemStack itemstack = chest.getStackInSlot(l);
            if (itemstack == null)
            {
                continue;
            }
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            while (itemstack.stackSize > 0)
            {
                int i1 = random.nextInt(21) + 10;
                if (i1 > itemstack.stackSize)
                {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, pos.getX() + f, (float) pos.getY() + (newSize > 0 ? 1 : 0) + f1, pos.getZ() + f2,
                        new ItemStack(itemstack.getItem(), i1, itemstack.getMetadata()));
                float f3 = 0.05F;
                entityitem.motionX = (float) random.nextGaussian() * f3;
                entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float) random.nextGaussian() * f3;
                if (itemstack.hasTagCompound())
                {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                }
                world.spawnEntityInWorld(entityitem);
            }
        }
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityIronChest)
        {
            TileEntityIronChest teic = (TileEntityIronChest) te;
            if (teic.getType().isExplosionResistant())
            {
                return 10000F;
            }
        }
        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    /*
     * @Override public boolean addLandingEffects(IBlockState state, WorldServer world, BlockPos pos, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
     * IronChestTextureHandler.addDestroyEffects(world, pos, state); return true; }
     */

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, EffectRenderer effectRenderer)
    {
        IronChestTextureHandler.addHitEffects(worldObj, target.getBlockPos(), target.sideHit);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer)
    {
        IronChestTextureHandler.addDestroyEffects(world, pos, world.getBlockState(pos));
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IInventory)
        {
            return Container.calcRedstoneFromInventory((IInventory) te);
        }
        return 0;
    }

    private static final EnumFacing[] validRotationAxes = new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN };

    @Override
    public EnumFacing[] getValidRotations(World worldObj, BlockPos pos)
    {
        return validRotationAxes;
    }

    @Override
    public boolean rotateBlock(World worldObj, BlockPos pos, EnumFacing axis)
    {
        if (worldObj.isRemote)
        {
            return false;
        }
        if (axis == EnumFacing.UP || axis == EnumFacing.DOWN)
        {
            TileEntity tileEntity = worldObj.getTileEntity(pos);
            if (tileEntity instanceof TileEntityIronChest)
            {
                TileEntityIronChest icte = (TileEntityIronChest) tileEntity;
                icte.rotateAround();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
    {
        super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
    }
}
