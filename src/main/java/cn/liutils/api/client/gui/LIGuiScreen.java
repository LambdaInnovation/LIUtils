/**
 * 
 */
package cn.liutils.api.client.gui;

import net.minecraft.client.gui.GuiScreen;

/**
 * @author WeathFolD
 *
 */
public class LIGuiScreen extends GuiScreen {

	protected LIGui gui;
	
	public LIGuiScreen() {
		gui = new LIGui(this);
	}
	
	public LIGuiScreen(double w, double h) {
		gui = new LIGui(this, w, h);
	}
	
    public void drawScreen(int mx, int my, float w)
    {
    	gui.drawElements(mx, my);
    }
    
    public void addWidget(Widget w) {
    	gui.addWidget(w);
    }
    
    protected void mouseClicked(int mx, int my, int btn) {
    	gui.mouseClicked(mx, my, btn);
    }
    
    protected void mouseClickMove(int mx, int my, int btn, long time) {
    	gui.mouseClickMove(mx, my, btn, time);
    }
	
}
