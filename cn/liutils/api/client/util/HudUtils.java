package cn.liutils.api.client.util;

import net.minecraft.client.renderer.Tessellator;

public class HudUtils {

	private static float SCALE_X = 1.0F, SCALE_Y = 1.0F;
	public static void setTextureResolution(int x, int y) {
		SCALE_X = 1.0F/x;
		SCALE_Y = 1.0F/y;
	}
	
    /**
     * Draws a textured rectangle at the stored z-value. Mapping the full texture to the rect.
     * Args: x, y, width, height
     */
    public static void drawTexturedModalRect(int x, int y, int width, int height)
    {
        float f = SCALE_X;
        float f1 = SCALE_Y;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, -90, 0, 1);
        tessellator.addVertexWithUV(x + width, y + height, -90, 1, 1);
        tessellator.addVertexWithUV(x + width, y + 0, -90, 1, 0);
        tessellator.addVertexWithUV(x + 0, y + 0, -90, 0, 0);
        tessellator.draw();
    }
    
	
    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public static void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = SCALE_X;
        float f1 = SCALE_Y;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par6, -90, (par3 + 0) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, -90, (par3 + par5) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + 0, -90, (par3 + par5) * f, (par4 + 0) * f1);
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, -90, (par3 + 0) * f, (par4 + 0) * f1);
        tessellator.draw();
    }
    
    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height, texWidth, texHeight
     */
    public static void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6, int par7, int par8)
    {
    	float f = SCALE_X;
        float f1 = SCALE_Y;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par6, -90, (par3 + 0) * f, (par4 + par8) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, -90, (par3 + par7) * f, (par4 + par8) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + 0, -90, (par3 + par7) * f, (par4 + 0) * f1);
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, -90, (par3 + 0) * f, (par4 + 0) * f1);
        tessellator.draw();
    }
    /*
    public static void drawTexturedModelRectFromIcon(int par1, int par2, Icon par3Icon, int par4, int par5)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par5, -90, par3Icon.getMinU(), par3Icon.getMaxV());
        tessellator.addVertexWithUV(par1 + par4, par2 + par5, -90, par3Icon.getMaxU(), par3Icon.getMaxV());
        tessellator.addVertexWithUV(par1 + par4, par2 + 0, -90, par3Icon.getMaxU(), par3Icon.getMinV());
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, -90, par3Icon.getMinU(), par3Icon.getMinV());
        tessellator.draw();
    }*/
}
