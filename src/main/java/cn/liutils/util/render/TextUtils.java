/**
 * 
 */
package cn.liutils.util.render;

import java.awt.Font;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.liutils.util.HudUtils;

/**
 * Font drawing assistance build upon TrueTypeFont class.
 * @author WeathFolD
 */
public class TextUtils {
	
	public static TrueTypeFont
		FONT_YAHEI_32;
	
	public static void init() {
		FONT_YAHEI_32 = new TrueTypeFont(new Font("Microsoft YaHei", Font.PLAIN, 32), true);
	}
	
	public static void drawText(TrueTypeFont font, String text, double x, double y, float size) {
		drawText(font, text, x, y, size, TrueTypeFont.ALIGN_LEFT);
	}
	
	public static void drawText(TrueTypeFont font, String text, double x, double y, float size, int format) {
		GL11.glPushMatrix(); {
			GL11.glTranslated(x, y + getHeight(font, text, size), HudUtils.zLevel);
			float scale = size / font.getHeight();
			GL11.glScalef(scale, -scale, 1);
			font.drawString(0, 0, text, 1, 1, format);
		} GL11.glPopMatrix();
	}
	
	public static float getWidth(TrueTypeFont font, String str, float size) {
		return font.getWidth(str) * size / font.getHeight(str);
	}
	
	public static float getHeight(TrueTypeFont font, String str, float size) {
		return size;
	}

}
