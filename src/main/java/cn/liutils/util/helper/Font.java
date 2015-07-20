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
package cn.liutils.util.helper;

import java.util.List;

import javax.vecmath.Vector2d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * A wrapper for better font drawing of mc fonts.
 * @author WeathFolD
 */
public class Font {
    
    public static Font font = new Font();

    private static FontRenderer theFont;
    
    FontRenderer mcFont() {
    	if(theFont == null) {
    		Minecraft mc = Minecraft.getMinecraft();
    		theFont = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, true);
    	}
    	return theFont;
    }
    
    public enum Align { LEFT, CENTER, RIGHT };
    
    private Font() {}
    
    /**
     * Draw a string at given position with given color.
     * @param str
     * @param x
     * @param y
     * @param size
     * @param color
     */
    public void draw(String str, double x, double y, double size, int color) {
        draw(str, x, y, size, color, Align.LEFT);
    }
    
    /**
     * Draw a string with line wrapping. Every line's length should not exceed [limit].
     * @param str
     * @param x
     * @param y
     * @param size
     * @param color
     * @param limit
     */
    public void drawWrapped(String str, double x, double y, double size, int color, double limit) {
        double scale = size / mcFont().FONT_HEIGHT;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(scale, scale, 1);
        mcFont().drawSplitString(str, 0, 0, (int) (limit * scale), color);
        GL11.glPopMatrix();
    }
    
    /**
     * The drawing simulation of drawWrapped, you can get the drawing area of the string through this.
     * @param str
     * @param size
     * @param limit
     * @return
     */
    public Vector2d simDrawWrapped(String str, double size, double limit) {
    	double scale = size / mcFont().FONT_HEIGHT;
        List<String> lst = mcFont().listFormattedStringToWidth(str, (int) (limit * scale));
        return new Vector2d(lst.size() == 1 ? strLen(str, size) : limit, size * lst.size());
    }
    
    /**
     * Draw a string with alignment.
     * @param str
     * @param x
     * @param y
     * @param size
     * @param color
     * @param align
     */
    public void draw(String str, double x, double y, double size, int color, Align align) {
        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glDepthMask(false);
        double scale = size / mcFont().FONT_HEIGHT;
        GL11.glPushMatrix(); {
            GL11.glTranslated(x, y, 0);
            GL11.glScaled(scale, scale, 1);
            String[] ss = str.split("\n");
            for(int i = 0; i < ss.length; ++i) {
                GL11.glPushMatrix(); {
                    double dy = i * mcFont().FONT_HEIGHT;
                    GL11.glTranslated(0, dy, 0);
                    drawSingleLine(ss[i], color, align);
                } GL11.glPopMatrix();
            }
        } GL11.glPopMatrix();
    }
    
    /**
     * Draw a string with trimming. Every character when length longer than limit will be ignored, 
     * and the postfix string will be appended to the drawing string if trimming actually happened.
     */
    public void drawTrimmed(String str, double x, double y, double size, int color, Align align, double limit, String postfix) {
        double cur = 0.0, scale = size / mcFont().FONT_HEIGHT;
        for(int i = 0; i < str.length(); ++i) {
            cur += mcFont().getCharWidth(str.charAt(i)) * scale;
            if(cur > limit) {
                str = str.substring(0, i).concat(postfix);
                break;
            }
        }
        draw(str, x, y, size, color, align);
    }
    
    private void drawSingleLine(String line, int color, Align align) {
        double x0 = 0, y0 = 0;
        switch(align) {
        case CENTER:
            x0 = -mcFont().getStringWidth(line) / 2;
            break;
        case LEFT:
            x0 = 0;
            break;
        case RIGHT:
            x0 = -mcFont().getStringWidth(line);
            break;
        default:
            break;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x0, y0, 0);
        mcFont().drawString(line, 0, 0, color);
        GL11.glPopMatrix();
    }
    
    /**
     * Get the length of the string on the screen when ordinarily drawn.
     */
    public double strLen(String str, double size) {
    	return mcFont().getStringWidth(str) * size / mcFont().FONT_HEIGHT;
    }

}
