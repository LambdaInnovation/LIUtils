/**
 * 
 */
package cn.liutils.cgui.gui.property;

import cn.liutils.cgui.loader.Editable;

/**
 * Basic widget geometry information
 * @author WeAthFolD
 */
public class PropWidget implements IProperty {
	
	public enum AlignStyle { LEFT, CENTER };
	
	@Editable("position")
	public double x, y;
	
	@Editable(value = "size", defDouble = 30.0)
	public double width, height;
	
	@Editable("scale")
	public double scale = 1.0;
	
	@Editable(value = "align")
	public AlignStyle align = AlignStyle.LEFT;
	
	@Editable(value = "doesDraw", defBoolean = true)
	public boolean doesDraw = true;
	
	@Editable(value = "doesListenKey", defBoolean = true)
	public boolean doesListenKey = true;
	
	public PropWidget setPos(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public PropWidget setSize(double w, double h) {
		this.width = w;
		this.height = h;
		return this;
	}

	@Override
	public String getName() {
		return "Widget";
	}
}
