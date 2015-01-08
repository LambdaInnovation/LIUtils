/**
 * 
 */
package cn.liutils.util.render;

import java.awt.Font;
import java.lang.reflect.Field;

import javax.vecmath.Vector2d;

import org.lwjgl.opengl.GL11;

import cn.liutils.util.HudUtils;

/**
 * PNG-Generated font drawer. Use a small C++-written tool as assist.
 * Currently don't support line-ends. Please seperate line-ends by yourself.
 * @author WeathFolD
 *
 */
public class TextUtils {
	
	public static final TrueTypeFont
		FONT_CONSOLAS_64 = new TrueTypeFont(new Font("微软雅黑", Font.PLAIN, 32), true),
		FONT_YAHEI_64 = new TrueTypeFont(new Font("微软雅黑", Font.PLAIN, 32), true);
	
	public static void init() {}
	
	public static void drawText(TrueTypeFont font, String text, double x, double y, float size) {
		drawText(font, text, x, y, size, TrueTypeFont.ALIGN_LEFT);
	}
	
	public static void drawText(TrueTypeFont font, String text, double x, double y, float size, int format) {
		GL11.glPushMatrix(); {
			GL11.glTranslated(x, y + getHeight(font, text, size), HudUtils.zLevel);
			float scale = size / font.getHeight();
			GL11.glScalef(scale, -scale, 1);
			//GL11.glBindTexture(GL11.GL_TEXTURE_2D, getTexture(font).getTextureID());
			font.drawString(0, 0, text, 1, 1, format);
		} GL11.glPopMatrix();
	}
	
	public static float getWidth(TrueTypeFont font, String str, float size) {
		return font.getWidth(str) * size / font.getLineHeight();
	}
	
	public static float getHeight(TrueTypeFont font, String str, float size) {
		return size;
	}

}
