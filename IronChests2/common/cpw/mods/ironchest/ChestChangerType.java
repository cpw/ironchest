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

import static cpw.mods.ironchest.IronChestType.COPPER;
import static cpw.mods.ironchest.IronChestType.DIAMOND;
import static cpw.mods.ironchest.IronChestType.GOLD;
import static cpw.mods.ironchest.IronChestType.IRON;
import static cpw.mods.ironchest.IronChestType.SILVER;
import static cpw.mods.ironchest.IronChestType.CRYSTAL;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForge;
public enum ChestChangerType {
	IRONGOLD(IRON,GOLD,"ironGoldUpgrade","Iron to Gold Chest Upgrade","mmm","msm","mmm"),
	GOLDDIAMOND(GOLD,DIAMOND,"goldDiamondUpgrade","Gold to Diamond Chest Upgrade","GGG","msm","GGG"),
	COPPERSILVER(COPPER,SILVER,"copperSilverUpgrade","Copper to Silver Chest Upgrade","mmm","msm","mmm"),
	SILVERGOLD(SILVER,GOLD,"silverGoldUpgrade","Silver to Gold Chest Upgrade","mGm","GsG","mGm"),
	COPPERIRON(COPPER,IRON,"copperIronUpgrade","Copper to Iron Chest Upgrade","mGm","GsG","mGm"),
	DIAMONDCRYSTAL(DIAMOND,CRYSTAL,"diamondCrystalUpgrade", "Diamond to Crystal Chest Upgrade","GGG","GOG","GGG");

	private IronChestType source;
	private IronChestType target;
	public String itemName;
	public String descriptiveName;
	private ItemChestChanger item;
	private String[] recipe;

	private ChestChangerType(IronChestType source, IronChestType target, String itemName, String descriptiveName, String... recipe) {
		this.source=source;
		this.target=target;
		this.itemName=itemName;
		this.descriptiveName=descriptiveName;
		this.recipe=recipe;
	}
	
	public boolean canUpgrade(IronChestType from) {
		return from==this.source;
	}
	
	public int getTarget() {
		return this.target.ordinal();
	}

	public ItemChestChanger buildItem(Configuration cfg, int id) {
		int itemId=Integer.parseInt(cfg.getOrCreateIntProperty(itemName, Configuration.CATEGORY_ITEM, id).value);
		item=new ItemChestChanger(itemId,this);
		return item;
	}

	public void addRecipe() {
		IronChestType.addRecipe(new ItemStack(item), recipe, 'm', target.mat,'s',source.mat,'G',Block.glass,'O',Block.obsidian);
	}
	
	public static void buildItems(Configuration cfg, int defaultId) {
		for (ChestChangerType type: values()) {
			type.buildItem(cfg, defaultId++);
		}
	}

	public static void generateRecipe(IronChestType type) {
		for (ChestChangerType item: values()) {
			if (item.source==type || item.target==type) {
				for (Object[] recipe : MinecraftForge.generateRecipes(item.recipe[0],item.recipe[1],item.recipe[2],'s',item.source.getMatList(),'m',item.target.getMatList(),'G',Block.glass,'O',Block.obsidian)) {
					if (recipe[4]==null || recipe[6]==null) {
						continue;
					}
					IronChestType.addRecipe(new ItemStack(item.item), recipe);
				}
			}
		}
	}
}
