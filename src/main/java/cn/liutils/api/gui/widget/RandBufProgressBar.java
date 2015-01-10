/**
 * 
 */
package cn.liutils.api.gui.widget;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import cn.liutils.api.gui.DrawArea;
import cn.liutils.api.gui.LIGui;
import cn.liutils.api.gui.Widget;
import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;

/**
 * A progress bar drawer. provide the progress value through abstract getProgress() method.</br>
 * The bar supports buffering, you can specify how much progress per s is allowed.</br>
 * Also it supports random fluctuating for display effect purpose. You can set the fluctuating speed
 * and the relative fluct region of the random(When set any of two to 0, there is no effect).</br>
 * Using of closure subClass is recommended.
 * @author WeathFolD
 */
public abstract class RandBufProgressBar extends Widget {
	
	public enum Direction { RIGHT, LEFT, UP, DOWN };
	
	/**
	 * To which direction the progress draws when increase.
	 */
	Direction dir = Direction.RIGHT;
	
	public double maxDelta = 0.1; //prog per sec
	public double 
		maxFluctSpeed = .8, //prog per sec
		fluctRegion = 0.15; //fluct in (progress - 0.5*fluctRegion, progress + 0.5 * fluctRegion)
	
	double curFluct;
	double curSpeed;
	double progressDisplay = -1; //cur display progress
	
	long lastDrawTime;
	
	private static final Random rand = new Random();
	
	public RandBufProgressBar(String id, Widget par, double x, double y,
			double w, double h) {
		super(id, par, x, y, w, h);
		setup();
	}

	public RandBufProgressBar(String id, LIGui scr, double x, double y,
			double w, double h) {
		super(id, scr, x, y, w, h);
		setup();
	}
	
	public void setDirection(Direction dir) {
		this.dir = dir;
	}
	
	private void setup() {
		lastDrawTime = Minecraft.getSystemTime();
	}
	
	public void draw(double mx, double my, boolean mh) {
		double prog = getProgress();
		long time = Minecraft.getSystemTime();
		double dt = Math.min((time - lastDrawTime) * 0.001, 10); //convert to seconds
		
		if(progressDisplay == -1) {
			progressDisplay = prog;
		} else {
			//Buffering
			double delta = prog - progressDisplay;
			double sgn = Math.signum(delta);
			delta = Math.min(Math.abs(delta), dt * maxFluctSpeed);
			progressDisplay += sgn * delta;
		}
		
		{ //Fluctuation
			double accel = (rand.nextDouble() - 0.5) * maxFluctSpeed;
			curSpeed += accel;
			curSpeed = Math.max(-maxFluctSpeed, Math.min(curSpeed, maxFluctSpeed));
			curFluct += curSpeed * dt;
			curFluct = Math.max(-0.5 * fluctRegion, Math.min(curFluct, 0.5 * fluctRegion));
		}
		
		{ //Area setting
			double disp = Math.max(0, Math.min(progressDisplay + curFluct, 1.0));
			double x, y, u, v, w, h, tw, th;
			
			switch(dir) {
			case RIGHT:
				w = area.width * disp;
				h = area.height;
				x = y = 0;
				u = area.u;
				v = area.v;
				tw = area.tWidth * disp;
				th = area.tHeight;
				break;
			case LEFT:
				w = area.width * disp;
				h = area.height;
				x = area.width - w;
				y = 0;
				u = area.u + area.tWidth * (1 - disp);
				v = area.v;
				tw = area.tWidth * disp;
				th = area.tHeight;
				break;
			case UP:
				w = area.width;
				h = area.height * disp;
				x = 0;
				y = area.height * (1 - disp);
				u = area.u;
				v = area.v + area.tHeight * (1 - disp);
				tw = area.tWidth;
				th = area.tHeight * disp;
				break;
			case DOWN:
				w = area.width;
				h = area.height * disp;
				x = y = 0;
				u = area.u;
				v = area.v;
				tw = area.tWidth;
				th = area.tHeight * disp;
				break;
			default:
				throw new RuntimeException("niconiconi, WTF??");
			}
			
			GL11.glEnable(GL11.GL_BLEND);
			if(texture != null) {
				RenderUtils.loadTexture(texture);
			}
			if(texWidth != 0 && texHeight != 0) {
				HudUtils.setTextureResolution(texWidth, texHeight);
			}
			GL11.glColor4d(1, 1, 1, 1);
			HudUtils.drawRect(x, y, u, v, w, h, tw, th);
		} 
		lastDrawTime = time;
	}
	
	/**
	 * Get the current progress of the bar.(0.0->1.0)
	 */
	public abstract double getProgress();

}
