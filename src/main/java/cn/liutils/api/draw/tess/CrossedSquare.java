/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.draw.tess;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;

/**
 * @author WeathFolD
 *
 */
public class CrossedSquare extends Rect {

	public CrossedSquare() {}

	public CrossedSquare(double w, double h) {
		super(w, h);
	}
	
	@Override
	public void onEvent(EventType event, DrawObject obj) {
		Tessellator t = Tessellator.instance;
		GL11.glTranslated(tx, ty, tz); //built in offset, usually in order to move it to center
		t.startDrawingQuads(); {
			obj.post(EventType.IN_TESS);
			t.addVertexWithUV(0, 0, 	 0, 	map.u0, map.getMaxV());
			t.addVertexWithUV(0, 0,  	 width, map.getMaxU(), map.getMaxV());
			t.addVertexWithUV(0, height, width, map.getMaxU(), map.v0);
			t.addVertexWithUV(0, height, 0, 	map.u0, map.v0);
			
			double hh = height / 2;
			t.addVertexWithUV(-hh, hh, 0, 	  map.u0, map.getMaxV());
			t.addVertexWithUV(-hh, hh, width, map.getMaxU(), map.getMaxV());
			t.addVertexWithUV(hh,  hh, width, map.getMaxU(), map.v0);
			t.addVertexWithUV(hh,  hh, 0, 	  map.u0, map.v0);
		} t.draw();
	}

}
