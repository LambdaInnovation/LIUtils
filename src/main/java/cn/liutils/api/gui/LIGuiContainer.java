package cn.liutils.api.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import org.lwjgl.opengl.GL11;

public class LIGuiContainer extends GuiContainer {
	
	protected LIGui gui;
	protected boolean clickItem = true;
	
	public LIGuiContainer(Container c) {
		super(c);
		gui = new LIGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.drawDefaultBackground();
		gui.resize(width, height);
		gui.draw(var2, var3);
	}

	@Override
    protected void mouseClicked(int par1, int par2, int par3) {
    	if(clickItem) super.mouseClicked(par1, par2, par3);
    	gui.mouseClicked(par1, par2, par3);
    }
	
    protected void mouseClickMove(int mx, int my, int btn, long time) {
    	if(clickItem) super.mouseClickMove(mx, my, btn, time);
    	gui.mouseClickMove(mx, my, btn, time);
    }
    
    public void onGuiClosed() {
    	gui.dispose();
    }
}
