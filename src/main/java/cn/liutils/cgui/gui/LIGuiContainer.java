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
package cn.liutils.cgui.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class LIGuiContainer extends GuiContainer {
	
	protected LIGui gui;
	
	public LIGuiContainer(Container c) {
		super(c);
		gui = new LIGui();
	}
	
	public LIGuiContainer(Container c, LIGui _gui) {
		super(c);
		gui = _gui;
	}
	
	public LIGui getGui() {
		return gui;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		gui.resize(width, height);
		gui.draw(var2, var3);
	}
	
	@Override
    public void drawScreen(int a, int b, float c) {
    	if(isSlotActive()) {
    		super.drawScreen(a, b, c);
    	} else {
    	    gui.resize(width, height);
    	    this.drawDefaultBackground();
            gui.draw(a, b);
    	}
    }

	@Override
    protected void mouseClicked(int par1, int par2, int par3) {
    	if(isSlotActive()) super.mouseClicked(par1, par2, par3);
    	gui.mouseClicked(par1, par2, par3);
    }
	
	@Override
    protected void mouseClickMove(int mx, int my, int btn, long time) {
    	if(isSlotActive()) super.mouseClickMove(mx, my, btn, time);
    	gui.mouseClickMove(mx, my, btn, time);
    }
    
    @Override
    public void onGuiClosed() {
    	gui.dispose();
    }
    
    @Override
    protected void mouseMovedOrUp(int a, int b, int c) {
    	if(isSlotActive()) {
    		super.mouseMovedOrUp(a, b, c);
    	}
    }
    
    @Override
    public void keyTyped(char ch, int key) {
    	gui.keyTyped(ch, key);
    	if(containerAcceptsKey(key) || key == Keyboard.KEY_ESCAPE)
    		super.keyTyped(ch, key);
    }
    
    protected boolean containerAcceptsKey(int key) {
    	return true;
    }
    
    public boolean isSlotActive() {
    	return true;
    }
}
