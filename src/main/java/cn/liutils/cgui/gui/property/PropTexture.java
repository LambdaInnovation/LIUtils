/**
 * 
 */
package cn.liutils.cgui.gui.property;

import net.minecraft.util.ResourceLocation;
import cn.liutils.cgui.loader.Editable;

/**
 * @author WeAthFolD
 */
public class PropTexture implements IProperty {
	
	static final ResourceLocation MISSING = new ResourceLocation("liutils:textures/cgui/missing.png");

	@Editable("texture")
	public ResourceLocation texture = MISSING;
	
	@Editable(value = "tex_mapping")
	public double u, v, tw = 0, th = 0;
	
	public PropTexture init(ResourceLocation t, double _u, double _v, double _tw, double _th) {
		texture = t;
		u = _u;
		v = _v;
		tw = _tw;
		th = _th;
		return this;
	}
	
	public PropTexture init(ResourceLocation t) {
		texture = t;
		return this;
	}

	@Override
	public String getName() {
		return "texture";
	}

	@Override
	public IProperty copy() {
		return new PropTexture().init(new ResourceLocation(texture.getResourceDomain(), texture.getResourcePath()), u, v, tw, th);
	}
	
}
