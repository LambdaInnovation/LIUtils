/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.gui;

import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * @author WeathFolD
 *
 */
public class LIGuiScreen extends GuiScreen {

	protected LIGui gui;
	
	public LIGuiScreen() {
		gui = new LIGui();
	}
	
	@Override
    public void drawScreen(int mx, int my, float w) {
    	gui.resize(width, height);
    	this.drawDefaultBackground();
    	GL11.glPushMatrix(); {
    		GL11.glTranslated(0, 0, 100);
    		gui.draw(mx, my);
    	} GL11.glPopMatrix();
    }
    
    @Override
    protected void mouseClicked(int mx, int my, int btn) {
    	gui.mouseClicked(mx, my, btn);
    }
    
    @Override
    protected void mouseClickMove(int mx, int my, int btn, long time) {
    	gui.mouseClickMove(mx, my, btn, time);
    }
    
    @Override
    public void onGuiClosed() {
    	gui.dispose();
    }
    
    @Override
    protected void keyTyped(char par1, int par2) {
    	super.keyTyped(par1, par2);
    	gui.keyTyped(par1, par2);
    }
	
    public LIGui getGui() {
    	return gui;
    }
    
}
