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
package cn.liutils.util.generic;

import net.minecraft.util.Vec3;

/**
 * @author WeAthFolD
 *
 */
public class MathUtils {

	public static final float PI_F = (float) Math.PI;
	
	public static float wrapYawAngle(float a) {
		float ret = a % 360f;
		return ret < 0 ? ret + 360f : ret;
	}
	
	public static double toRadians(double angle) {
		return angle * Math.PI / 180;
	}
	
	public static float toRadians(float angle) {
		return angle * PI_F / 180;
	}
	
	public static double toAngle(double rad) {
		return rad * 180 / Math.PI;
	}
	
	public static float toAngle(float rad) {
		return rad * 180 / PI_F;
	}
	
	/**
	 * Return whether a -180~180 wrapped yaw angle is in the range denoted by [start, end].
	 * Note that this is not a simple range comparison. If the arc of the angle is in the
	 * range sweeped from start to end clockwisely, then the result is true.
	 */
	public static boolean angleYawinRange(float start, float end, float angle) {
		if(end < start)
			return false;
		if(end - start >= 360f)
			return true;
		
		float ss = wrapYawAngle(start), se = wrapYawAngle(end), sa = wrapYawAngle(angle);
		
		if(ss > se) {
			return ss <= sa || sa <= se;
		}
		
		//System.out.println(ss + " " + se + " " + sa);
		return ss <= sa && sa <= se;
	}
	
	/**
	 * Perform a simple linear lerp between a and b
	 * @param a
	 * @param b
	 * @param lambda The weight of b
	 * @return The lerp value
	 */
	public static double lerp(double a, double b, double lambda) {
		return a * (1 - lambda) + b * lambda;
	}
	
	public static float lerpf(float a, float b, float lambda) {
		return a * (1 - lambda) + b * lambda;
	}
	
	public static int wrapi(int min, int max, int val) {
		return Math.max(min, Math.min(max, val));
	}
	
	public static double wrapd(double min, double max, double val) {
		if(val > max)
			return max;
		if(val < min)
			return min;
		return val;
	}
	
	public static float wrapf(float min, float max, float val) {
		if(val > max)
			return max;
		if(val < min)
			return min;
		return val;
	}
	
	public static double distanceSq(double[] vec1, double[] vec2) {
		if(vec1.length != vec2.length) {
			throw new RuntimeException("Inconsistent length");
		}
		
		double ret = 0.0;
		for(int i = 0; i < vec1.length; ++i) {
			double d = vec2[i] - vec1[i];
			ret += d * d;
		}
		
		return ret;
	}
	
	public static double distance(double x0, double y0, double z0, double x1, double y1, double z1) {
		return Math.sqrt(distanceSq(x0, y0, z0, x1, y1, z1));
	}
	
	public static double distanceSq(double x0, double y0, double z0, double x1, double y1, double z1) {
		return distanceSq(new double[] { x0, y0, z0 }, new double[] { x1, y1, z1 });
	}
	
	public static double distance(double[] vec1, double[] vec2) {
		return Math.sqrt(distanceSq(vec1, vec2));
	}
	
	public static Vec3 multiply(Vec3 vec, double factor) {
		return Vec3.createVectorHelper(vec.xCoord * factor, vec.yCoord * factor, vec.zCoord * factor);
	}
	
	public static double length(double dx, double dy, double dz) {
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public static double lengthSq(double dx, double dy, double dz) {
		return dx * dx + dy * dy + dz * dz;
	}
	
}
