/**
 * 
 */
package cn.liutils.api.gui;

import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;

/**
 * @author WeathFolD
 *
 */
public class LIGuiScreen extends GuiScreen {

	protected LIGui gui;
	
	public LIGuiScreen() {
		gui = new LIGui(this);
	}
	
    public void drawScreen(int mx, int my, float w)
    {
    	this.drawDefaultBackground();
    	GL11.glPushMatrix(); {
    		GL11.glTranslated(0, 0, 100);
    		gui.drawElements(mx, my);
    	} GL11.glPopMatrix();
    }
    
    protected void mouseClicked(int mx, int my, int btn) {
    	gui.mouseClicked(mx, my, btn);
    }
    
    protected void mouseClickMove(int mx, int my, int btn, long time) {
    	gui.mouseClickMove(mx, my, btn, time);
    }
	
    public LIGui getGui() {
    	return gui;
    }
    
}
