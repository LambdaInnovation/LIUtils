/**
 * 
 */
package cn.liutils.api.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

/**
 * Generic interface for something that provides a texture.
 * @author WeAthFolD
 */
public interface ITextureProvider {
	@SideOnly(Side.CLIENT)
	ResourceLocation getTexture();
}
