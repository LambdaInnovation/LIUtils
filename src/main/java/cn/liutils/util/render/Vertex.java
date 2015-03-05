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
package cn.liutils.util.render;

import javax.vecmath.Vector2d;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;
import cn.liutils.util.RenderUtils;
import cn.liutils.util.misc.Pair;

/**
 * @author WeAthFolD
 *
 */
public class Vertex extends Pair<Vec3, Vector2d> {
	
	public Vertex(double x, double y, double z, double u, double v) {
		super(RenderUtils.newV3(x, y, z), new Vector2d(u, v));
	}

	public Vertex(Vec3 vec3, double u, double v) {
		super(vec3, new Vector2d(u, v));
	}

	public void addTo(Tessellator t) {
		t.addVertexWithUV(first.xCoord, first.yCoord, first.zCoord, second.x,
				second.y);
	}
}