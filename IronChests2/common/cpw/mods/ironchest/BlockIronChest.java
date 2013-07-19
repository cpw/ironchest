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

import java.util.List;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import static net.minecraftforge.common.ForgeDirection.*;

public class BlockIronChest extends BlockContainer {

    private Random random;

    @SideOnly(Side.CLIENT)
    private Icon[][] icons;

    public BlockIronChest(int id)
    {
        super(id, Material.iron);
        setUnlocalizedName("IronChest");
        setHardness(3.0F);
        setBlockBounds(0.0625F, 0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        random = new Random();
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public TileEntity createNewTileEntity(World w)
    {
        return null;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return 22;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return IronChestType.makeEntity(metadata);
    }

//    public Icon getBlockTexture(IBlockAccess worldAccess, int i, int j, int k, int l)
//    {
//        int meta = worldAccess.getBlockMetadata(i, j, k);
//        IronChestType type = IronChestType.values()[meta];
//        TileEntity te = worldAccess.getBlockTileEntity(i, j, k);
//        TileEntityIronChest icte = null;
//        if (te != null && te instanceof TileEntityIronChest)
//        {
//            icte = (TileEntityIronChest) te;
//        }
//        if (l == 0 || l == 1)
//        { // Top and Bottom
//            return type.getTextureRow() * 16 + 1;
//        }
//        else if (icte != null && l == icte.getFacing())
//        { // Front
//            return type.getTextureRow() * 16 + 2;
//        }
//        else
//        { // Back and Sides
//            return type.getTextureRow() * 16;
//        }
//    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int i, int j)
    {
        if (j < IronChestType.values().length)
        {
            IronChestType type = IronChestType.values()[j];
            return type.getIcon(i);
        }
        return null;
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3)
    {
        TileEntity te = world.getBlockTileEntity(i, j, k);

        if (te == null || !(te instanceof TileEntityIronChest))
        {
            return true;
        }

        if (world.isBlockSolidOnSide(i, j + 1, k, ForgeDirection.DOWN))
        {
            return true;
        }

        if (world.isRemote)
        {
            return true;
        }

        player.openGui(IronChest.instance, ((TileEntityIronChest) te).getType().ordinal(), world, i, j, k);
        return true;
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k)
    {
        super.onBlockAdded(world, i, j, k);
        world.markBlockForUpdate(i, j, k);
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack itemStack)
    {
        byte chestFacing = 0;
        int facing = MathHelper.floor_double((double) ((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if (facing == 0)
        {
            chestFacing = 2;
        }
        if (facing == 1)
        {
            chestFacing = 5;
        }
        if (facing == 2)
        {
            chestFacing = 3;
        }
        if (facing == 3)
        {
            chestFacing = 4;
        }
        TileEntity te = world.getBlockTileEntity(i, j, k);
        if (te != null && te instanceof TileEntityIronChest)
        {
            ((TileEntityIronChest) te).setFacing(chestFacing);
            world.markBlockForUpdate(i, j, k);
        }
    }

    @Override
    public int damageDropped(int i)
    {
        return i;
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int i1, int i2)
    {
        TileEntityIronChest tileentitychest = (TileEntityIronChest) world.getBlockTileEntity(i, j, k);
        if (tileentitychest != null)
        {
            dropContent(0, tileentitychest, world, tileentitychest.xCoord, tileentitychest.yCoord, tileentitychest.zCoord);
        }
        super.breakBlock(world, i, j, k, i1, i2);
    }

    public void dropContent(int newSize, IInventory chest, World world, int xCoord, int yCoord, int zCoord)
    {
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
                EntityItem entityitem = new EntityItem(world, (float) xCoord + f, (float) yCoord + (newSize > 0 ? 1 : 0) + f1, (float) zCoord + f2,
                        new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (IronChestType type : IronChestType.values())
        {
            if (type.isValidForCreativeMode())
            {
                par3List.add(new ItemStack(this, 1, type.ordinal()));
            }
        }
    }

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
       TileEntity te = world.getBlockTileEntity(x, y, z);
       if (te instanceof TileEntityIronChest)
       {
           TileEntityIronChest teic = (TileEntityIronChest) te;
           if (teic.getType().isExplosionResistant())
           {
               return 10000f;
           }
       }
       return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
    }


    @Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return Container.calcRedstoneFromInventory((TileEntityIronChest) par1World.getBlockTileEntity(par2, par3, par4));
    }

    @Override
    public boolean hasComparatorInputOverride() 
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (IronChestType typ: IronChestType.values())
        {
            typ.makeIcons(par1IconRegister);
        }
    }

    private static final ForgeDirection[] validRotationAxes = new ForgeDirection[] { UP, DOWN };
    @Override
    public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z)
    {
        return validRotationAxes;
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        if (worldObj.isRemote)
        {
            return false;
        }
        if (axis == UP || axis == DOWN)
        {
            TileEntity tileEntity = worldObj.getBlockTileEntity(x, y, z);
            if (tileEntity instanceof TileEntityIronChest) {
                TileEntityIronChest icte = (TileEntityIronChest) tileEntity;
                icte.rotateAround(axis);
            }
            return true;
        }
        return false;
    }
}
