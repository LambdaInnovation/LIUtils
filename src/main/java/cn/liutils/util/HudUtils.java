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
package cn.liutils.util;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cn.liutils.api.draw.tess.RectMapping;

/**
 * Utilities about hud drawing.
 */
public class HudUtils {

	public static double SCALE_X = 1.0F, SCALE_Y = 1.0F;
	public static double zLevel = -90D;
	
	/**
	 * Called to set the scale before any drawing call.
	 * @param x
	 * @param y
	 */
	public static void setTextureResolution(double x, double y) {
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
    public static void drawRect(double x, double y, double width, double height)
    {
        double f = SCALE_X;
        double f1 = SCALE_Y;
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
    	double f = SCALE_X;
    	double f1 = SCALE_Y;
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
    public static void drawRect(double x, double y, double u, double v, double width, double height)
    {
    	double f = SCALE_X;
    	double f1 = SCALE_Y;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(x + 0, y + height, zLevel, (u + 0) * f, (v + height) * f1);
        t.addVertexWithUV(x + width, y + height, zLevel, (u + width) * f, (v + height) * f1);
        t.addVertexWithUV(x + width, y + 0, zLevel, (u + width) * f, (v + 0) * f1);
        t.addVertexWithUV(x + 0, y + 0, zLevel, (u + 0) * f, (v + 0) * f1);
        t.draw();
    }
    
    public static void drawRect(double x, double y, double width, double height, RectMapping rm) {
    	drawRect(x, y, rm.u0, rm.v0, width, height, rm.tw, rm.th);
    	//System.out.println(DebugUtils.formatArray(x, y, rm.u0, rm.v0, width, height, rm.tw, rm.th));
    }
    
    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height, texWidth, texHeight
     */
    public static void drawRect(double x, double y, double u, double v, double width, double height, double texWidth, double texHeight)
    {
    	double f = SCALE_X;
    	double f1 = SCALE_Y;
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(x + 0, y + height, zLevel, (u + 0) * f, (v + texHeight) * f1);
        t.addVertexWithUV(x + width, y + height, zLevel, (u + texWidth) * f, (v + texHeight) * f1);
        t.addVertexWithUV(x + width, y + 0, zLevel, (u + texWidth) * f, (v + 0) * f1);
        t.addVertexWithUV(x + 0, y + 0, zLevel, (u + 0) * f, (v + 0) * f1);
        t.draw();
    }
    
    public static void drawRectFromIcon(int x, int y, IIcon icon, int width, int height)
    {
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(x + 0, y + height, zLevel, icon.getMinU(), icon.getMaxV());
        t.addVertexWithUV(x + width, y + height, zLevel, icon.getMaxU(), icon.getMaxV());
        t.addVertexWithUV(x + width, y + 0, zLevel, icon.getMaxU(), icon.getMinV());
        t.addVertexWithUV(x + 0, y + 0, zLevel, icon.getMinU(), icon.getMinV());
        t.draw();
    }
    
    public static void drawHoveringText(List par1List, int par2, int par3, FontRenderer font, int width, int height)
    {
        if (!par1List.isEmpty())
        {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            float zLevel = -90.0F;
            Iterator iterator = par1List.iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                int l = font.getStringWidth(s);

                if (l > k)
                {
                    k = l;
                }
            }

            int i1 = par2 + 12;
            int j1 = par3 - 12;
            int k1 = 8;

            if (par1List.size() > 1)
            {
                k1 += 2 + (par1List.size() - 1) * 10;
            }

            if (i1 + k > width)
            {
                i1 -= 28 + k;
            }

            if (j1 + k1 + 6 > height)
            {
                j1 = height - k1 - 6;
            }

            zLevel = 300.0F;
            int l1 = -267386864;
            drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
            drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
            drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
            drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
            drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
            drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

            for (int k2 = 0; k2 < par1List.size(); ++k2)
            {
                String s1 = (String)par1List.get(k2);
                font.drawStringWithShadow(s1, i1, j1, -1);

                if (k2 == 0)
                {
                    j1 += 2;
                }

                j1 += 10;
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
    
    public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = (par5 >> 24 & 255) / 255.0F;
        float f1 = (par5 >> 16 & 255) / 255.0F;
        float f2 = (par5 >> 8 & 255) / 255.0F;
        float f3 = (par5 & 255) / 255.0F;
        float f4 = (par6 >> 24 & 255) / 255.0F;
        float f5 = (par6 >> 16 & 255) / 255.0F;
        float f6 = (par6 >> 8 & 255) / 255.0F;
        float f7 = (par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setColorRGBA_F(f1, f2, f3, f);
        t.addVertex(par3, par2, -90D);
        t.addVertex(par1, par2, -90D);
        t.setColorRGBA_F(f5, f6, f7, f4);
        t.addVertex(par1, par4, -90D);
        t.addVertex(par3, par4, -90D);
        t.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
