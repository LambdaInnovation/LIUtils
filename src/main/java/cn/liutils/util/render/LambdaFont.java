/**
 * 
 */
package cn.liutils.util.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;

/**
 * Java+LIUtils implementation of LambdaFont.
 * @author WeathFolD
 */
public class LambdaFont {
	
	public enum Align { LEFT, CENTER, RIGHT };
	
	static final int PER_LINE = 16;
	final ResourceLocation png;
	final Map<Character, Integer> table = new HashMap();
	int fontSize, texWidth, texHeight;
	double narrowStep = 0.5, wideStep = 1.0;

	public LambdaFont(ResourceLocation _png, String file) {
		png = _png;
		Properties prop = new Properties();
		init(LambdaFont.class.getResourceAsStream(file));
	}
	
	private int udiv(int a, int b) {
		int ret = a / b;
		return ret * b == a ? ret : ret + 1;
	}
	
	public void setStep(double narrow, double wide) {
		narrowStep = narrow;
		wideStep = wide;
	}
	
	private void init(InputStream stm) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stm));
		Map<String, Integer> props = new HashMap();
		
		String str;
		try{
			while((str = reader.readLine()) != null) {
				int ind;
				if((ind = str.indexOf('=')) != -1) {
					String key = str.substring(0, ind),
						value = ind == str.length() - 1 ? "" : str.substring(ind + 1, str.length());
					
					//Start proc
					int val = Integer.valueOf(value);
					if(key.charAt(0) == '_') {
						props.put(key, val);
					} else {
						if(key.length() == 1) {
							table.put(Character.valueOf(key.charAt(0)), val);
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		fontSize = props.get("_size");
		int csSize = props.get("_charset_size");
		
		for(Map.Entry<Character, Integer> entry : table.entrySet()) {
			System.out.println(entry.getKey() + " -> " + entry.getValue());
		}
		
		texWidth = fontSize * PER_LINE;
		texHeight = fontSize * udiv(csSize, PER_LINE);
	}
	
	public void draw(String str, double x, double y, double size) {
		draw(str, x, y, size, Align.LEFT);
	}
	
	public void draw(String str, double x, double y, double size, Align align) {
		GL11.glEnable(GL11.GL_BLEND);
		RenderUtils.loadTexture(png);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		double psx = HudUtils.SCALE_X, psy = HudUtils.SCALE_Y;
		GL11.glPushMatrix(); {
			GL11.glTranslated(x, y, 0);
			GL11.glScaled(size, size, 1);
			String[] ss = str.split("\n");
			for(int i = 0; i < ss.length; ++i) {
				GL11.glPushMatrix(); {
					double dy = i * fontSize;
					GL11.glTranslated(0, dy, 0);
					drawSingleLine(ss[i], align);
				} GL11.glPopMatrix();
			}
		} GL11.glPopMatrix();
		HudUtils.SCALE_X = psx;
		HudUtils.SCALE_Y = psy;
	}
	
	public double getWidth(String str, double size) {
		double ret = 0.0;
		String[] strs = str.split("\n");
		for(String s : strs) {
			ret = Math.max(ret, widthSingleLine(s));
		}
		return ret;
	}
	
	private double widthSingleLine(String str) {
		double ret = 0.0;
		for(int i = 0; i < str.length(); ++i) {
			ret += getStep(str.codePointAt(i));
		}
		return ret;
	}
	
	private void drawSingleLine(String line, Align align) {
		double x0 = 0, y0 = 0;
		switch(align) {
		case CENTER:
			y0 = 0;
			x0 = -widthSingleLine(line) / 2;
			break;
		case LEFT:
			y0 = 0;
			x0 = 0;
			break;
		case RIGHT:
			y0 = 0;
			x0 = -widthSingleLine(line);
			break;
		default:
			break;
		}
		HudUtils.setTextureResolution(texWidth, texHeight);
		for(int i = 0; i < line.length(); ++i) {
			int ind = getMapping(line.charAt(i));
			//System.out.println(ind + " <- " + line.charAt(i));
			double u = fontSize * (ind % PER_LINE),
					v = fontSize * (ind / PER_LINE);
			HudUtils.drawRect(x0, y0, u, v, 1, 1, fontSize, fontSize);
			x0 += getStep(line.codePointAt(i));
		}
	}
	
	private double getStep(int cp) {
		return Character.isIdeographic(cp) ? wideStep : narrowStep;
	}
	
	private int getMapping(char ch) {
		return table.containsKey(ch) ? table.get(ch) : 0;
	}

}
