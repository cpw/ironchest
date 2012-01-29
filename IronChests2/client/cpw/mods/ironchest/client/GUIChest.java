package cpw.mods.ironchest.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.ironchest.ContainerDiamondChest;
import cpw.mods.ironchest.ContainerGoldChest;
import cpw.mods.ironchest.ContainerIronChestBase;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.TileEntityIronChest;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ModLoader;

public class GUIChest extends GuiContainer {
	public enum GUI {
		GOLD(ContainerGoldChest.class,184,256,"/ic2/sprites/goldcontainer.png",IronChestType.GOLD),
		DIAMOND(ContainerDiamondChest.class,238,256,"/ic2/sprites/diamondcontainer.png",IronChestType.DIAMOND);
		
		private Class<? extends ContainerIronChestBase> clazz;
		private int xSize;
		private int ySize;
		private String guiTexture;
		private IronChestType mainType;

		private GUI(Class<? extends ContainerIronChestBase> clazz, int xSize, int ySize, String guiTexture, IronChestType mainType) {
			this.clazz=clazz;
			this.xSize=xSize;
			this.ySize=ySize;
			this.guiTexture=guiTexture;
			this.mainType=mainType;
		}
		
		protected Container makeContainer(IInventory player, IInventory chest) {
			try {
				return clazz.getConstructor(IInventory.class,IInventory.class).newInstance(player,chest);
			} catch (Exception e) {
				// unpossible
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		public static void showGUI(TileEntityIronChest te, EntityPlayer player) {
			for (GUI gui : values()) {
				if (te.getType()==gui.mainType) {
					ModLoader.OpenGUI(player, new GUIChest(gui,player.inventory,te));
					return;
				}
			}
			player.displayGUIChest(te);
		}
	}

	private GUI type;

	private GUIChest(GUI type, IInventory player, IInventory chest) {
		super(type.makeContainer(player,chest));
		this.type=type;
		this.xSize=type.xSize;
		this.ySize=type.ySize;
		this.allowUserInput=false;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int tex = mc.renderEngine.getTexture(type.guiTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(tex);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
