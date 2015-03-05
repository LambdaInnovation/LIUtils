/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.draw.tess;

import java.util.EnumSet;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;

/**
 * A most simple rectangle drawer that takes rect's width and height, then draw it in
 * (0, 0, 0) -> (0, height, width) with a offset (tx, ty, tz)
 * @author WeathFolD
 */
public class Rect extends TessBase {

	public double width, height;
	
	public double tx, ty, tz;
	
	public RectMapping map = new RectMapping(); //Texture mapping data.

	public Rect() {
		super("simple_rect");
	}
	
	public Rect(double w, double h) {
		super("simple_rect");
		setSize(w, h);
	}
	
	public void setCentered() {
		tz = -width / 2;
		ty = -height / 2;
	}
	
	public void setSize(double w, double h) {
		width = w;
		height = h;
	}
	
	public void setOffset(double tx, double ty, double tz) {
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
	}

	@Override
	public EnumSet<EventType> getEvents() {
		return EnumSet.of(EventType.DO_TESS);
	}

	@Override
	public void onEvent(EventType event, DrawObject obj) {
		Tessellator t = Tessellator.instance;
		GL11.glTranslated(tx, ty, tz); //built in offset, usually in order to move it to center
		t.startDrawingQuads(); {
			obj.post(EventType.IN_TESS);
			t.setNormal(1, 0, 0);
			t.addVertexWithUV(0, 0, 	 0, 	map.getMinU(), map.getMaxV());
			t.addVertexWithUV(0, 0,  	 width, map.getMaxU(), map.getMaxV());
			t.addVertexWithUV(0, height, width, map.getMaxU(), map.getMinV());
			t.addVertexWithUV(0, height, 0, 	map.getMinU(), map.getMinV());
		} t.draw();
	}

}
