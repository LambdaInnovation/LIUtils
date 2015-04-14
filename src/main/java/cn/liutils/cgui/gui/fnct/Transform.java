package cn.liutils.cgui.gui.fnct;

import cn.liutils.cgui.gui.Widget;

public class Transform extends Component {
	
	public enum AlignStyle { LEFT, CENTER };
	
	public double width = 50.0, height = 50.0;
	
	public double x = 0, y = 0;
	
	public double scale = 1.0;
	
	public boolean doesDraw = true, doesListenKey = true;
	
	public AlignStyle align = AlignStyle.LEFT;

	public Transform() {
		name = "Transform";
	}
	
	//Helper set methods
	public Transform setPos(double _x, double _y) {
		x = _x;
		y = _y;
		return this;
	}
	
	public Transform setSize(double _width, double _height) {
		width = _width;
		height = _height;
		return this;
	}
	
}
