package cn.liutils.api.client.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

/**
 * Utilities about hud drawing.
 */
public class HudUtils {

	private static float SCALE_X = 1.0F, SCALE_Y = 1.0F;
	public static double zLevel = -90D;
	
	/**
	 * Called to set the scale before any drawing call.
	 * @param x
	 * @param y
	 */
	public static void setTextureResolution(int x, int y) {
		SCALE_X = 1.0F/x;
		SCALE_Y = 1.0F/y;
	}
	
	public static void setZLevel(double z) {
		zLevel = z;
	}
	
    /**
     * Draws a textured rectangle at the stored z-value. Mapping the full texture to the rect.
     * Args: x, y, width, height
     */
    public static void drawTexturedModalRect(double x, double y, double width, double height)
    {
        float f = SCALE_X;
        float f1 = SCALE_Y;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, zLevel, 0, 1);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
        tessellator.addVertexWithUV(x + width, y + 0, zLevel, 1, 0);
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
        tessellator.draw();
    }
    
    public static void drawRectOutline(double x, double y, double w, double h, float lineWidth) {
    	GL11.glLineWidth(lineWidth);
    	Tessellator t = Tessellator.instance;
    	GL11.glDisable(GL11.GL_TEXTURE_2D);
    	t.startDrawing(GL11.GL_LINE_LOOP);
    	t.addVertex(x, y, zLevel);
    	t.addVertex(x, y + h, zLevel);
    	t.addVertex(x + w, y + h, zLevel);
    	t.addVertex(x + w, y, zLevel);
    	t.draw();
    	GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    
    public static void drawModalRect(double x, double y, double width, double height) {
    	float f = SCALE_X;
        float f1 = SCALE_Y;
        Tessellator t = Tessellator.instance;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        t.startDrawingQuads(); {
	        t.addVertex(x + 0, y + height, zLevel);
	        t.addVertex(x + width, y + height, zLevel);
	        t.addVertex(x + width, y + 0, zLevel);
	        t.addVertex(x + 0, y + 0, zLevel);
        } t.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
	
    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(float par1, float par2, int par3, int par4, float par5, float par6)
    {
        float f = SCALE_X;
        float f1 = SCALE_Y;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par6, zLevel, (par3 + 0) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, zLevel, (par3 + par5) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + 0, zLevel, (par3 + par5) * f, (par4 + 0) * f1);
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, zLevel, (par3 + 0) * f, (par4 + 0) * f1);
        tessellator.draw();
    }
    
    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height, texWidth, texHeight
     */
    public static void drawTexturedModalRect(float par1, float par2, double par3, double par4, double par5, double par6, double par7, double par8)
    {
    	float f = SCALE_X;
        float f1 = SCALE_Y;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par6, zLevel, (par3 + 0) * f, (par4 + par8) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, zLevel, (par3 + par7) * f, (par4 + par8) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + 0, zLevel, (par3 + par7) * f, (par4 + 0) * f1);
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, zLevel, (par3 + 0) * f, (par4 + 0) * f1);
        tessellator.draw();
    }
    
    public static void drawTexturedModelRectFromIcon(int x, int y, IIcon icon, int width, int height)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, zLevel, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + width, y + height, zLevel, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + width, y + 0, zLevel, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(x + 0, y + 0, zLevel, icon.getMinU(), icon.getMinV());
        tessellator.draw();
    }
}
