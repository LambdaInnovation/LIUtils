package cn.liutils.api.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import org.lwjgl.opengl.GL11;

public class LIGuiContainer extends GuiContainer {
	
	protected LIGui gui;
	
	public LIGuiContainer(Container c) {
		super(c);
		gui = new LIGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		gui.resize(width, height);
		gui.draw(var2, var3);
	}
	
    public void drawScreen(int a, int b, float c) {
    	if(isSlotActive()) {
    		super.drawScreen(a, b, c);
    	} else {
    		this.drawDefaultBackground();
    		gui.draw(a, b);
    	}
    }

	@Override
    protected void mouseClicked(int par1, int par2, int par3) {
    	if(isSlotActive()) super.mouseClicked(par1, par2, par3);
    	gui.mouseClicked(par1, par2, par3);
    }
	
    protected void mouseClickMove(int mx, int my, int btn, long time) {
    	if(isSlotActive()) super.mouseClickMove(mx, my, btn, time);
    	gui.mouseClickMove(mx, my, btn, time);
    }
    
    public void onGuiClosed() {
    	gui.dispose();
    }
    
    protected void mouseMovedOrUp(int a, int b, int c) {
    	if(isSlotActive()) {
    		super.mouseMovedOrUp(a, b, c);
    	}
    }
    
    public boolean isSlotActive() {
    	return true;
    }
}
