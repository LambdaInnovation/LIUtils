/**
 * 
 */
package cn.liutils.cgui.gui.property;

import cn.liutils.cgui.loader.Editable;

/**
 * Basic widget geometry information
 * @author WeAthFolD
 */
public class PropBasic implements IProperty {
	
	public enum AlignStyle { LEFT, CENTER };
	
	@Editable("position")
	public double x = 50, y = 50;
	
	@Editable(value = "size")
	public double width = 100.0, height = 100.0;
	
	@Editable("scale")
	public double scale = 1.0;
	
	@Editable(value = "align")
	public AlignStyle align = AlignStyle.LEFT;
	
	@Editable(value = "doesDraw")
	public boolean doesDraw = true;
	
	@Editable(value = "doesListenKey")
	public boolean doesListenKey = true;
	
	public PropBasic setPos(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public PropBasic setSize(double w, double h) {
		this.width = w;
		this.height = h;
		return this;
	}

	@Override
	public String getName() {
		return "basic";
	}

	@Override
	public IProperty copy() {
		PropBasic ret = new PropBasic();
		ret.x = x;
		ret.y = y;
		ret.width = width;
		ret.height = height;
		ret.scale = scale;
		ret.align = align;
		ret.doesDraw = doesDraw;
		ret.doesListenKey = doesListenKey;
		return ret;
	}
}
