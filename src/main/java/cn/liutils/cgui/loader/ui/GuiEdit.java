package cn.liutils.cgui.loader.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import javax.vecmath.Vector2d;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.gui.GuiHandlerBase;
import cn.annoreg.mc.gui.RegGuiHandler;
import cn.liutils.api.key.IKeyHandler;
import cn.liutils.cgui.gui.LIGui;
import cn.liutils.cgui.gui.LIGuiScreen;
import cn.liutils.cgui.gui.Widget;
import cn.liutils.core.LIUtils;
import cn.liutils.registry.AttachKeyHandlerRegistry.RegAttachKeyHandler;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValueFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeAthFolD
 */
@RegistrationClass
public class GuiEdit extends LIGuiScreen {
	
	static final String CONFIG_PATH = "config/cgui_layout.conf";
	
	public Configuration cfg;
	
	LIGui toEdit = new LIGuiPlayground(this); //The edit playground gui!
	
	Widget selectedEditor;
	
	public static double
		COLOR[] = { 0.2, 0.4, 0.65, .9 };
	
	public static double[] COLOR_STYLE = new double[] { .3, .56, 1 };
	
	public static void bindColor(int n) {
		double c = COLOR[n];
		GL11.glColor4d(c * COLOR_STYLE[0], c * COLOR_STYLE[1], c * COLOR_STYLE[2], 0.7);
	}
	
	public GuiEdit() {
		this.drawBack = false;
		
		//TODO: File IO in client thread.....
		File f = new File(CONFIG_PATH);
		if(!f.isFile()) {
			if(f.isDirectory()) {
				f.delete();
			}
			try {
				FileOutputStream fos = new FileOutputStream(f);
				fos.close();
			} catch(Exception e) {}
		}
		cfg = new Configuration(f);
		
		gui.addWidget("toolbar", new Toolbar(this));
		gui.addWidget("hierarchy", new Hierarchy(this));
	}
	
	public Vector2d getDefaultPosition(String name, double[] def) {
		double[] r = cfg.get("layout", name, def).getDoubleList();
		return new Vector2d(r);
	}
	
	public void updateDefaultPosition(String name, double x, double y) {
		cfg.get("layout", name, new double[] { x, y }).set(new double[] { x, y });
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
    		//toEdit.mouseClicked(mx, my, btn);
    	}
    }
    
    @Override
    protected void mouseClickMove(int mx, int my, int btn, long time) {
    	if(!gui.mouseClickMove(mx, my, btn, time)) {
    		toEdit.mouseClickMove(mx, my, btn, time);
    	}
    }
    
    @Override
    public void onGuiClosed() {
    	super.onGuiClosed();
    	
    	cfg.save();
    }
	
	@RegAttachKeyHandler(clazz = KeyHandler.class)
	public static final int OPEN = Keyboard.KEY_HOME;
	
	public static class KeyHandler implements IKeyHandler {
		@Override
		public void onKeyDown(int keyCode, boolean tickEnd) {
			if(LIUtils.DEBUG)
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
