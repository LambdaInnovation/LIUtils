/**
 * 
 */
package cn.liutils.util.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.vecmath.Vector2d;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;
import cn.liutils.util.misc.Pair;

/**
 * Java+LIUtils implementation of LambdaFont.
 * @author WeathFolD
 */
public class LambdaFont {
	
	public enum Align { LEFT, CENTER, RIGHT };
	
	static final int PER_LINE = 16;
	final ResourceLocation png;
	final Map<Character, CharExtent> table = new HashMap();
	int fontSize, spacing, texWidth, texHeight;
	
	double ratio;

	public LambdaFont(ResourceLocation _png, String file) {
		png = _png;
		Properties prop = new Properties();
		init(LambdaFont.class.getResourceAsStream(file));
	}
	
	private int udiv(int a, int b) {
		int ret = a / b;
		return ret * b == a ? ret : ret + 1;
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
					if(key.length() == 0) continue;
					if(key.length() != 1 && key.charAt(0) == '_') {
						props.put(key, Integer.valueOf(value));
					} else {
						if(key.length() == 1) {
							String[] toparse = value.split(",");
							table.put(key.charAt(0), 
								new CharExtent(Integer.valueOf(toparse[0]), Double.valueOf(toparse[1])));
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		fontSize = props.get("_size");
		spacing = props.get("_spacing");
		int csSize = props.get("_charset_size");
		
		texWidth = fontSize * PER_LINE;
		texHeight = (spacing + fontSize) * udiv(csSize, PER_LINE);
		ratio = (double)(spacing + fontSize) / fontSize;
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
					double dy = i * (fontSize + spacing);
					GL11.glTranslated(0, dy, 0);
					drawSingleLine(ss[i], align);
				} GL11.glPopMatrix();
			}
		} GL11.glPopMatrix();
		HudUtils.SCALE_X = psx;
		HudUtils.SCALE_Y = psy;
	}
	
	public void drawAdjusted(String str, double x, double y, double size, double cst) {
		drawAdjusted(str, x, y, size, Align.LEFT, cst);
	}
	
	public void drawAdjusted(String str, double x, double y, double size, Align align, double cst) {
		size = adjustFontSize(this, str, size, cst);
		draw(str, x, y, size, align);
	}
	
	/**
	 * It must be guaranteed that the string contains no line-break characters
	 * @returns area of drawing
	 */
	public Vector2d drawLinebreak(String str, double x, double y, double size, double cst) {
		GL11.glEnable(GL11.GL_BLEND);
		RenderUtils.loadTexture(png);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		double psx = HudUtils.SCALE_X, psy = HudUtils.SCALE_Y;
		GL11.glPushMatrix();
			GL11.glTranslated(x, y, 0);
			GL11.glScaled(size, size, 1);
			
			HudUtils.setTextureResolution(texWidth, texHeight);
			
			List<Pair<String, Boolean>> arr = dlbPreProc(str, size, cst);
			
			double spcstp = getExtent(' ').getStep();
			
			double y0 = 0;
			double curLen = 0;
			double maxLen = 0;
			for(int i = 0; i < arr.size(); ++i) {
				Pair<String, Boolean> pair = arr.get(i);
				double len = widthSingleLine(pair.first);
				if(size * len < cst && size * (curLen + len) > cst) {
					--i;
					maxLen = Math.max(curLen, maxLen);
					curLen = 0;
					y0 += 1.0;
					continue;
				}
				
				GL11.glPushMatrix(); {
					GL11.glTranslated(curLen, y0, 0);
					drawSingleLine(pair.first, Align.LEFT);
				} GL11.glPopMatrix();
				curLen += len + (pair.second ? spcstp : 0);
				
				if(size * len > cst) {
					maxLen = Math.max(curLen, maxLen);
					curLen = 0;
					y0 += 1.0;
				}
			}
			maxLen = Math.max(curLen, maxLen);
		GL11.glPopMatrix();
		HudUtils.SCALE_X = psx;
		HudUtils.SCALE_Y = psy;
		
		return new Vector2d(maxLen * size, y0 * size + size + spacing * size / fontSize);
	}
	
	/**
	 * Chop up over-length words.
	 */
	private List<Pair<String, Boolean>> dlbPreProc(String str, double size, double cst) {
		String[] raw = str.split(" ");
		List<Pair<String, Boolean>> res = new ArrayList();
		for(int i = 0; i < raw.length; ++i) {
			String to = raw[i];
			double len = widthSingleLine(to);
			if(len * size < cst) {
				res.add(new Pair(to, true));
			} else {
				double cur = 0.0;
				int li = 0;
				//System.out.println("st: " + to);
				//System.out.println("---");
				for(int j = 1; j < to.length(); ++j) {
					double l = getExtent(to.charAt(j)).getStep() * size;
					//System.out.println(cur);
					if(cur + l > cst) {
						cur = 0.0;
						res.add(new Pair(to.substring(li, j), false));
						li = j;
						continue;
					}
					cur += l;
				}
				//System.out.println("---");
				if(li != to.length() - 1) {
					res.add(new Pair(to.substring(li, to.length()), true));
				} else {
					res.get(res.size() - 1).second = true;
				}
			}
		}
		
		return res;
	}
	
	public double getWidth(String str, double size) {
		double ret = 0.0;
		String[] strs = str.split("\n");
		for(String s : strs) {
			ret = Math.max(ret, widthSingleLine(s));
		}
		return ret * size;
	}
	
	public static double adjustFontSize(LambdaFont font, String str, double expSize, double lenCst) {
		double len = font.getWidth(str, expSize);
		return len > lenCst ? (expSize * (lenCst / len)) : expSize;
	}
	
	private double widthSingleLine(String str) {
		double ret = 0.0;
		for(int i = 0; i < str.length(); ++i) {
			ret += getExtent(str.charAt(i)).getStep();
		}
		return ret;
	}
	
	private void drawSingleLine(String line, Align align) {
		double x0 = 0, y0 = 0;
		switch(align) {
		case CENTER:
			x0 = -widthSingleLine(line) / 2;
			break;
		case LEFT:
			x0 = 0;
			break;
		case RIGHT:
			x0 = -widthSingleLine(line);
			break;
		default:
			break;
		}
		HudUtils.setTextureResolution(texWidth, texHeight);
		for(int i = 0; i < line.length(); ++i) {
			CharExtent ext = getExtent(line.charAt(i));
			int ind = ext.id;
			double u = fontSize * (ind % PER_LINE),
					v = (fontSize + spacing) * (ind / PER_LINE);
			HudUtils.drawRect(x0, y0, u, v, 1, ratio, fontSize, fontSize + spacing);
			x0 += ext.getStep();
		}
	}
	
	private void drawSingleChar(char ch) {
		CharExtent ext = getExtent(ch);
		int ind = ext.id;
		HudUtils.drawRect(0, 0, fontSize * (ind % PER_LINE), (fontSize + spacing) * (ind / PER_LINE), 1, ratio, fontSize, fontSize + spacing);
	}
	
	private CharExtent getExtent(char ch) {
		//here we assert that '?' already have a mapping.
		CharExtent ret = table.get(ch);
		return ret == null ? table.get(' ') : ret;
	}

	private class CharExtent {
		public final int id;
		private final double step;
		public CharExtent(int _id, double _step) {
			id = _id;
			step = _step;
		}
		
		public double getStep() {
			return step / fontSize;
		}
	}
	
}
