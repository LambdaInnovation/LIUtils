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
import cn.liutils.cgui.gui.property.PropTexture;
import cn.liutils.util.DebugUtils;
import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;

/**
 * 
 * @author WEAthFolD
 *
 */
public class SimpleDrawer extends GuiEventHandler<DrawEvent> {
	
	{
		checkProperty("texture", PropTexture.class);
	}
	
	public SimpleDrawer(Widget w) {
		super(w);
	}
	
	public SimpleDrawer(Widget w, ResourceLocation rl, double _u, double _v, double _tw, double _th) {
		super(w);
		PropTexture texture = (PropTexture) widget.getProperty("texture");
		texture.init(rl, _u, _v, _tw, _th);
	}

	@Override
	public void handleEvent(DrawEvent event) {
		PropTexture texture = (PropTexture) widget.getProperty("texture");
		if(texture.texture != null)
			RenderUtils.loadTexture(texture.texture);
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glColor4d(1, 1, 1, 1);
		HudUtils.drawRect(0, 0, texture.u, texture.v, widget.propWidget().width, 
			widget.propWidget().height, texture.tw, texture.th);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	@Override
	public Class<? extends GuiEvent> getEventClass() {
		return DrawEvent.class;
	}

}
