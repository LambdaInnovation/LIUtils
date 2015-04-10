/**
 * 
 */
package cn.liutils.cgui.gui.fnct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.lwjgl.opengl.GL11;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.property.IProperty;
import cn.liutils.cgui.gui.property.PropColor;
import cn.liutils.cgui.gui.property.PropTexture;
import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;

/**
 * 
 * @author WeAthFolD
 *
 */
public class SimpleDrawer extends Function<DrawEvent> {
	
	public SimpleDrawer() {
		super(DrawEvent.class);
	}
	
	@Override
	public void onAdded(Widget w) {
		w.checkProperty("texture", PropTexture.class);
	}

	@Override
	public void handleEvent(Widget widget, DrawEvent event) {
		PropTexture texture = (PropTexture) widget.getProperty("texture");
		if(texture.texture != null)
			RenderUtils.loadTexture(texture.texture);
		
		//Set the color if color prop exists.
		PropColor color = (PropColor) widget.getProperty("color");
		if(color != null) {
			GL11.glColor4d(color.r, color.g, color.b, color.a);
		} else {
			GL11.glColor4d(1, 1, 1, 1);
		}
		
		//Draw the rectangle.
		if(texture.tw != 0 && texture.th != 0) {
			HudUtils.drawRect(0, 0, texture.u, texture.v, widget.propBasic().width, 
				widget.propBasic().height, texture.tw, texture.th);
		} else {
			HudUtils.drawRect(0, 0, widget.propBasic().width, widget.propBasic().height);
		}
	}

	@Override
	public Collection<Class> getRequiredProperties() {
		return Arrays.asList(new Class[] { PropTexture.class });
	}

	@Override
	public String getName() {
		return "simpleDrawer";
	}

}
