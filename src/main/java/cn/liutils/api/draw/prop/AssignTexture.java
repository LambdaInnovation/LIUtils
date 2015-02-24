/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.draw.prop;

import java.util.EnumSet;

import net.minecraft.util.ResourceLocation;
import cn.liutils.api.draw.DrawHandler;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;
import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;

/**
 * @author WeathFolD
 *
 */
public class AssignTexture extends DrawHandler {

	public ResourceLocation texture;
	
	public double texWidth, texHeight;
	
	public AssignTexture() {}
	
	public AssignTexture(ResourceLocation res, double tw, double th) {
		set(res);
		texWidth = tw;
		texHeight = th;
	}
	
	public AssignTexture(ResourceLocation res) {
		set(res);
	}
	
	public void set(ResourceLocation tex) {
		texture = tex;
	}
	
	public void setResolution(double w, double h) {
		texWidth = w;
		texHeight = h;
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
		if(texWidth != 0 && texHeight != 0) {
			HudUtils.setTextureResolution(texWidth, texHeight);
		}
	}

}
