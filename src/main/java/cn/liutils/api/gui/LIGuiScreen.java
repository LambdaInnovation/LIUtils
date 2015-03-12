/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
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
	protected boolean drawBack = true;
	
	public LIGuiScreen(LIGui _gui) {
		gui = _gui;
	}
	
	public LIGuiScreen() {
		this(new LIGui());
	}
	
	public LIGuiScreen setDrawBack(boolean b) {
		drawBack = b;
		return this;
	}
	
	@Override
    public void drawScreen(int mx, int my, float w) {
    	gui.resize(width, height);
    	if(drawBack)
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
