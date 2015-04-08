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
	
	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize(double w, double h) {
		this.width = w;
		this.height = h;
	}

	@Override
	public String getName() {
		return "Widget";
	}
}
