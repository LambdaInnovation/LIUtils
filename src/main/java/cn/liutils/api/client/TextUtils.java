/**
 * 
 */
package cn.liutils.api.client;

import java.awt.Font;
import java.lang.reflect.Field;

import javax.vecmath.Vector2d;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import cn.liutils.api.client.util.HudUtils;

/**
 * PNG-Generated font drawer. Use a small C++-written tool as assist.
 * Currently don't support line-ends. Please seperate line-ends by yourself.
 * @author WeathFolD
 *
 */
public class TextUtils {
	
	public static final TrueTypeFont
		FONT_CONSOLAS_64 = new TrueTypeFont(new Font("微软雅黑", Font.PLAIN, 32), false),
		FONT_YAHEI_64 = new TrueTypeFont(new Font("微软雅黑", Font.PLAIN, 32), false);
	
	private static Field fieldTexture;
	static {
		try {
			fieldTexture = TrueTypeFont.class.getDeclaredField("fontTexture");
		} catch (Exception e) {
			e.printStackTrace();
		}
		fieldTexture.setAccessible(true);
	}
	
	public static void drawText(TrueTypeFont font, String text, double x, double y, float size) {
		GL11.glPushMatrix(); {
			GL11.glTranslated(x, y, HudUtils.zLevel);
			float scale = size / font.getHeight();
			GL11.glScalef(scale, scale, 1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, getTexture(font).getTextureID());
			font.drawString(0, 0, text);
		} GL11.glPopMatrix();
	}
	
	public static float getWidth(TrueTypeFont font, String str, float size) {
		return font.getWidth(str) * size / font.getHeight();
	}
	
	public static float getHeight(TrueTypeFont font, String str, float size) {
		return size;
	}
	
	private static Texture getTexture(TrueTypeFont ttf) {
		try {
			return (Texture) fieldTexture.get(ttf);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
