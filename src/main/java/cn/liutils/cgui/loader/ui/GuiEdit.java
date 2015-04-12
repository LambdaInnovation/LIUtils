package cn.liutils.cgui.loader.ui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.gui.GuiHandlerBase;
import cn.annoreg.mc.gui.RegGuiHandler;
import cn.liutils.api.key.IKeyHandler;
import cn.liutils.cgui.gui.LIGui;
import cn.liutils.cgui.gui.LIGuiScreen;
import cn.liutils.cgui.gui.Widget;
import cn.liutils.registry.AttachKeyHandlerRegistry.RegAttachKeyHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeAthFolD
 */
@RegistrationClass
public class GuiEdit extends LIGuiScreen {
	
	LIGui toEdit = new LIGuiPlayground(this); //The edit playground gui!
	
	Widget selectedEditor;
	
	public static double
		COLOR[] = { 0.2, 0.4, 0.65, 0.9 };
	
	public static double[] COLOR_STYLE = new double[] { .3, .56, 1 };
	
	public static void bindColor(int n) {
		double c = COLOR[n];
		GL11.glColor4d(c * COLOR_STYLE[0], c * COLOR_STYLE[1], c * COLOR_STYLE[2], 0.7);
	}
	
	public GuiEdit() {
		gui.addWidget(new Toolbar());
		this.drawBack = false;
	}
	
	@Override
    public void drawScreen(int mx, int my, float w) {
		LIGui.drawBlackout();
		toEdit.draw(mx, my);
		super.drawScreen(mx, my, w);
	}
	
    @Override
    protected void mouseClicked(int mx, int my, int btn) {
    	if(!gui.mouseClicked(mx, my, btn)) {
    		//Fallthrough only if edit gui wasn't interrupting.
    		toEdit.mouseClicked(mx, my, btn);
    	}
    }
    
    @Override
    protected void mouseClickMove(int mx, int my, int btn, long time) {
    	if(!gui.mouseClickMove(mx, my, btn, time)) {
    		toEdit.mouseClickMove(mx, my, btn, time);
    	}
    }
	
	@RegAttachKeyHandler(clazz = KeyHandler.class)
	public static final int OPEN = Keyboard.KEY_HOME;
	
	public static class KeyHandler implements IKeyHandler {
		@Override
		public void onKeyDown(int keyCode, boolean tickEnd) {
			guiHandler.openClientGui();
		}
		@Override
		public void onKeyUp(int keyCode, boolean tickEnd) {}
		@Override
		public void onKeyTick(int keyCode, boolean tickEnd) {}
	}
	
	@RegGuiHandler
	public static GuiHandlerBase guiHandler = new GuiHandlerBase() {
		@SideOnly(Side.CLIENT)
		protected GuiScreen getClientGui() {
			return new GuiEdit();
		}
	};
	
	public static final ResourceLocation tex(String name) {
		return new ResourceLocation("liutils:textures/cgui/" + name + ".png");
	}
}
