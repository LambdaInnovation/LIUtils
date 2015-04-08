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

	@Editable("texture")
	public ResourceLocation texture;
	
	@Editable(value = "tex_mapping", defDouble = 0.0)
	public double u, v, tw = 0, th = 0;
	
	public void init(ResourceLocation t, double _u, double _v, double _tw, double _th) {
		texture = t;
		u = _u;
		v = _v;
		tw = _tw;
		th = _th;
	}

	@Override
	public String getName() {
		return "texture";
	}
	
}
