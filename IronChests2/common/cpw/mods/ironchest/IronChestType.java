package cpw.mods.ironchest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_IronChest;
import net.minecraft.src.forge.Configuration;

public enum IronChestType {
	IRON(54, 9, true, "Iron Chest", "guiIronChest", "ironchest.png", 0, Item.ingotIron, TileEntityIronChest.class, "mmmmPmmmm","mGmG3GmGm"), 
	GOLD(81, 9, true, "Gold Chest", "guiGoldChest", "goldchest.png", 1, Item.ingotGold, TileEntityGoldChest.class, "mmmmPmmmm","mGmG4GmGm"), 
	DIAMOND(108, 12, true, "Diamond Chest", "guiDiamondChest", "diamondchest.png", 2, Item.diamond, TileEntityDiamondChest.class, "GGGmPmGGG", "GGGG4Gmmm"), 
	COPPER(45, 9, false, "Copper Chest", "guiCopperChest", "copperchest.png", 3, null, TileEntityCopperChest.class, "mmmmCmmmm"), 
	SILVER(72, 9, false, "Silver Chest", "guiSilverChest", "silverchest.png", 4, null, TileEntitySilverChest.class, "mmmm0mmmm", "mmmm3mmmm"),
	CRYSTAL(108, 12, true, "Crystal Chest", "guiDiamondChest", "crystalchest.png", 5, null, TileEntityCrystalChest.class, "GGGGPGGGG");
	int size;
	private int rowLength;
	public String friendlyName;
	private boolean tieredChest;
	private String modelTexture;
	private String guiName;
	private int textureRow;
	public Class<? extends TileEntityIronChest> clazz;
	Item mat;
	private String[] recipes;
	public int guiId;
	private ArrayList<ItemStack> matList;

	IronChestType(int size, int rowLength, boolean tieredChest, String friendlyName, String guiName, String modelTexture, int textureRow, Item mat,
			Class<? extends TileEntityIronChest> clazz, String... recipes) {
		this.size = size;
		this.rowLength = rowLength;
		this.tieredChest = tieredChest;
		this.friendlyName = friendlyName;
		this.guiName = guiName;
		this.modelTexture = "/cpw/mods/ironchest/sprites/" + modelTexture;
		this.textureRow = textureRow;
		this.clazz = clazz;
		this.mat = mat;
		this.recipes = recipes;
		this.matList=new ArrayList<ItemStack>();
		if (mat!=null) {
			matList.add(new ItemStack(mat));
		}
	}

	public String getModelTexture() {
		return modelTexture;
	}

	public int getTextureRow() {
		return textureRow;
	}

	public static TileEntityIronChest makeEntity(int metadata) {
		// Compatibility
		int chesttype = metadata;
		try {
			TileEntityIronChest te = values()[chesttype].clazz.newInstance();
			return te;
		} catch (InstantiationException e) {
			// unpossible
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// unpossible
			e.printStackTrace();
		}
		return null;
	}

	public static void registerTranslations() {
	}

	public static void generateTieredRecipies(BlockIronChest blockResult) {
		ItemStack previous = new ItemStack(Block.chest);
		for (IronChestType typ : values()) {
			if (!typ.tieredChest)
				continue;
			generateRecipesForType(blockResult, previous, typ, typ.mat);
			previous = new ItemStack(blockResult, 1, typ.ordinal());
		}
	}

	public static void generateRecipesForType(BlockIronChest blockResult, Object previousTier, IronChestType type, Object mat) {
		for (String recipe : type.recipes) {
			String[] recipeSplit = new String[] { recipe.substring(0, 3), recipe.substring(3, 6), recipe.substring(6, 9) };
			addRecipe(new ItemStack(blockResult, 1, type.ordinal()), recipeSplit, 'm', mat, 'P', previousTier, 'G', Block.glass, 'C', Block.chest,
					'0', new ItemStack(blockResult, 1, 0)/* Iron */, '1', new ItemStack(blockResult, 1, 1)/* GOLD */, '3', new ItemStack(blockResult,
							1, 3)/* Copper */, '4', new ItemStack(blockResult,1,4));
		}
	}

	public static void addRecipe(ItemStack is, Object... parts) {
		ModLoader.AddRecipe(is, parts);
	}

	public int getGUI() {
		return guiId;
	}

	public static void initGUIs(Configuration cfg) {
		int defGUI = 51;
		for (IronChestType typ : values()) {
			if (typ.guiName != null) {
				typ.guiId = Integer.parseInt(cfg.getOrCreateIntProperty(typ.guiName, Configuration.GENERAL_PROPERTY, defGUI++).value);
				mod_IronChest.proxy.registerGUI(typ.guiId);
			} else {
				typ.guiId = -1;
			}
		}
	}

	public int getRowCount() {
		return size / rowLength;
	}

	public int getRowLength() {
		return rowLength;
	}

	public void addMat(ItemStack ore) {
		this.matList.add(ore);
	}

	public List<ItemStack> getMatList() {
		return matList;
	}

	public boolean isTransparent() {
		return this==CRYSTAL;
	}

}