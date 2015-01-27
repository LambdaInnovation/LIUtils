/**
 * 
 */
package cn.liutils.api.draw.prop;

import java.util.EnumSet;

import net.minecraft.util.ResourceLocation;
import cn.liutils.api.draw.DrawHandler;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;
import cn.liutils.util.RenderUtils;

/**
 * @author WeathFolD
 *
 */
public class AssignTexture extends DrawHandler {

	public ResourceLocation texture;
	
	public AssignTexture() {}
	
	public AssignTexture(ResourceLocation res) {
		set(res);
	}
	
	public void set(ResourceLocation tex) {
		texture = tex;
	}

	@Override
	public EnumSet<EventType> getEvents() {
		return EnumSet.of(EventType.PRE_TESS);
	}

	@Override
	public String getID() {
		return "texture";
	}

	@Override
	public void onEvent(EventType event, DrawObject obj) {
		if(texture != null)
			RenderUtils.loadTexture(texture);
	}

}
