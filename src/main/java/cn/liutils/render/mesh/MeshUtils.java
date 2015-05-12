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
package cn.liutils.render.mesh;

/**
 * @author WeAthFolD
 *
 */
public class MeshUtils {

	/**
	 * Create a billboard mesh that maps to the whole texture in XOY plane. If mesh == null, create a new one.
	 */
	public static Mesh createBillboard(Mesh mesh, double x0, double y0, double x1, double y1) {
		if(mesh == null)
			mesh = new Mesh();
		mesh.setVertices(new double[][] {
				{ x0, y0, 0 },
				{ x1, y0, 0 },
				{ x1, y1, 0 },
				{ x0, y1, 0 }
		});
		mesh.setUVs(new double[][] {
			{ 0, 0 },
			{ 1, 0 },
			{ 1, 1 },
			{ 0, 1 }
		});
		mesh.setQuads(new int[] { 0, 1, 2, 3 });
		mesh.setAllNormals(new float[] { 0, 0, 1 });
		return mesh;
	}
	
}
