/**
 * 
 */
package cn.liutils.cgui.gui.fnct;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;
import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.GuiEvent;
import cn.liutils.cgui.gui.event.GuiEventHandler;
import cn.liutils.cgui.gui.property.PropColor;
import cn.liutils.cgui.gui.property.PropTexture;
import cn.liutils.util.DebugUtils;
import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;

/**
 * 
 * @author WeAthFolD
 *
 */
public class SimpleDrawer extends GuiEventHandler<DrawEvent> {
	
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
			HudUtils.drawRect(0, 0, texture.u, texture.v, widget.propWidget().width, 
				widget.propWidget().height, texture.tw, texture.th);
		} else {
			HudUtils.drawRect(0, 0, widget.propWidget().width, widget.propWidget().height);
		}
	}

}
