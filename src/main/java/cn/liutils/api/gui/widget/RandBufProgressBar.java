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
package cn.liutils.api.gui.widget;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import cn.liutils.api.draw.DrawHandler;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;
import cn.liutils.api.draw.tess.GUIRect;
import cn.liutils.api.draw.tess.RectMapping;
import cn.liutils.api.gui.Widget;
import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;

/**
 * A progress bar drawer. provide the progress value through abstract getProgress() method.</br>
 * The bar supports buffering, you can specify how much progress per s is allowed.</br>
 * Also it supports random fluctuating for display effect purpose. You can set the fluctuating speed
 * and the relative fluct region of the random(When set any of two to 0, there is no effect).</br>
 * The drawer is pre-initialized. You should directly add <code>DrawHandler</code> into it.
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
	
	final ResourceLocation tex;
	
	private GUIRect orig;
	
	public RandBufProgressBar(double x, double y, double w, double h,
			ResourceLocation _tex, double u, double v, double tw, double th) {
		super(x, y, w, h);
		tex = _tex;
		lastDrawTime = Minecraft.getSystemTime();
		
		this.drawer = new DrawObject();
		orig = new GUIRect(w, h, u, v, tw, th);
		
		drawer.addHandler(new DrawHandler() { //setup the size of the object
			@Override
			public EnumSet<EventType> getEvents() {
				return EnumSet.of(EventType.DO_TESS);
			}
			@Override
			public String getID() {
				return "setup";
			}
			@Override
			public void onEvent(EventType event, DrawObject obj) {
				double disp = Math.max(0, Math.min(progressDisplay + curFluct, 1.0));
				//System.out.println(progressDisplay + " " + curFluct + " " + disp);
				double x, y, u, v, w, h, tw, th;
				RectMapping mapping = orig.getMap();
				switch(dir) {
				case RIGHT:
					w = width * disp;
					h = height;
					x = y = 0;
					u = mapping.u0;
					v = mapping.v0;
					tw = mapping.tw * disp;
					th = mapping.th;
					break;
				case LEFT:
					w = width * disp;
					h = height;
					x = width - w;
					y = 0;
					u = mapping.u0 + mapping.tw * (1 - disp);
					v = mapping.v0;
					tw = mapping.tw * disp;
					th = mapping.th;
					break;
				case UP:
					w = width;
					h = height * disp;
					x = 0;
					y = height * (1 - disp);
					u = mapping.u0;
					v = mapping.v0 + mapping.th * (1 - disp);
					tw = mapping.tw;
					th = mapping.th * disp;
					break;
				case DOWN:
					w = width;
					h = height * disp;
					x = y = 0;
					u = mapping.u0;
					v = mapping.v0;
					tw = mapping.tw;
					th = mapping.th * disp;
					break;
				default:
					throw new RuntimeException("niconiconi, WTF??");
				}
				RenderUtils.loadTexture(tex);;
				HudUtils.drawRect(x, y, u, v, w, h, tw, th);
			}
		});
	}
	
	public RandBufProgressBar setDirection(Direction dir) {
		this.dir = dir;
		return this;
	}
	
	public RandBufProgressBar setFluctRegion(double r) {
        fluctRegion = r;
        return this;
    }
	
	@Override
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
		
		drawer.draw();
		lastDrawTime = time;
	}
	
	/**
	 * Get the current progress of the bar.(0.0->1.0)
	 */
	public abstract double getProgress();

}
