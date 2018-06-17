package cpw.mods.ironchest.common.util;

import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.config.Config;
import cpw.mods.ironchest.common.core.IronChestBlocks;
import cpw.mods.ironchest.common.items.ChestChangerType;
import cpw.mods.ironchest.common.items.ShulkerBoxChangerType;
import cpw.mods.ironchest.common.lib.BlockLists;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabItems
{
    @SideOnly(Side.CLIENT)
    public static void getSubItems(NonNullList<ItemStack> subItems)
    {
        for (ChestChangerType type : ChestChangerType.VALUES)
        {
            subItems.add(new ItemStack(type.item));
        }

        if (Config.addShulkerBoxesToCreative)
        {
            for (ShulkerBoxChangerType type : ShulkerBoxChangerType.VALUES)
            {
                subItems.add(new ItemStack(type.item));
            }
        }

        for (IronChestType type : IronChestType.VALUES)
        {
            if (type.isValidForCreativeMode())
            {
                subItems.add(new ItemStack(IronChestBlocks.ironChestBlock, 1, type.ordinal()));
            }
        }

        if (Config.addShulkerBoxesToCreative)
        {
            for (Block shulker : BlockLists.SHULKER_BLOCKS)
            {
                for (IronShulkerBoxType type : IronShulkerBoxType.VALUES)
                {
                    if (type.isValidForCreativeMode())
                    {
                        subItems.add(new ItemStack(shulker, 1, type.ordinal()));
                    }
                }
            }
        }
    }
}
