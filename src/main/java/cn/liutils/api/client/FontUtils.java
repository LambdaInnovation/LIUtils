/**
 * 
 */
package cn.liutils.api.client;

import javax.vecmath.Vector2d;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.client.util.HudUtils;
import cn.liutils.api.client.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

/**
 * PNG-Generated font drawer. Use a small C++-written tool as assist.
 * Currently don't support line-ends. Please seperate line-ends by yourself.
 * @author WeathFolD
 *
 */
public class FontUtils {
	
	public static final ResourceLocation
		FONT_CONSOLAS_64 = new ResourceLocation("liutils:fonts/consolas_64.png");
	
	static final int LINES = 5, COLUMNS = 10;
	static final double 
		VERT_STEP = 1.0 / LINES,
		HORZ_STEP = 1.0 / COLUMNS;
	
	//Mapping between char index to line and column.
	static final int revmap[] = 
	{
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 0, 48, 45, 46, 0, 0, 37, 
		0, 38, 47, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 39, 0, 49, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		43, 0, 44, 0, 0, 0, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 
		24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 41, 40, 42, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0
	};
	
	public static void drawText(ResourceLocation font, String text, double x, double y, float size) {
		GL11.glPushMatrix(); {
			GL11.glTranslated(x, y, HudUtils.zLevel);
			GL11.glScalef(size, size, 1);
			HudUtils.setTextureResolution(1, 1);
			float x0 = 0;
			RenderUtils.loadTexture(font);
			for(int i = 0; i < text.length(); ++i) {
				int map = revmap[(int)text.charAt(i)];
				if(map == 0) continue;
				map -= 1;
				double u = HORZ_STEP * (map % COLUMNS);
				double v = VERT_STEP * (map / COLUMNS);
				HudUtils.drawTexturedModalRect(x0, 0, u, v, 1, 1, HORZ_STEP, VERT_STEP);
				x0 += 0.5;
			}
		} GL11.glPopMatrix();
	}
	
	public static Vector2d fontLength(String text, float size) {
		Vector2d res = new Vector2d(0, 0);
		res.y = size;
		for(int i = 0; i < text.length(); ++i) {
			if(acceptChar(text.charAt(i))) res.x += size / 2;
		}
		return res;
	}
	
	private static boolean acceptChar(char ch) {
		return revmap[(int)ch] != 0;
	}

}
