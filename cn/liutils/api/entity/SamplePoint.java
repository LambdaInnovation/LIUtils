package cn.liutils.api.entity;

/**
 * 采样点类，在尾迹渲染中使用。
 * 
 * @author WeAthFolD
 * 
 */
public class SamplePoint {

	public double x, y, z;
	public int tick;

	public SamplePoint(double p1, double p2, double p3, int t) {
		x = p1;
		y = p2;
		z = p3;
		tick = t;
	}

}
