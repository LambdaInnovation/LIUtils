/**
 * 
 */
package cn.liutils.registry;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cn.annoreg.core.AnnotationData;
import cn.annoreg.core.RegistryType;
import cn.annoreg.core.RegistryTypeDecl;
import cn.liutils.api.render.IPlayerRenderHook;
import cn.liutils.core.LIUtils;
import cn.liutils.template.LIClientRegistry;

/**
 * Register of PlayerRenderHook
 * @author WeathFolD
 */
@RegistryTypeDecl
@SideOnly(Side.CLIENT)
public class PlayerRenderHookRegistry extends RegistryType {
	
	@Target({ElementType.TYPE, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@SideOnly(Side.CLIENT)
	public @interface RegPlayerRenderHook {}

	public PlayerRenderHookRegistry() {
		super(RegPlayerRenderHook.class, LIUtils.REGISTER_TYPE_RENDER_HOOK);
	}

	@Override
	public boolean registerClass(AnnotationData data) {
		try {
			IPlayerRenderHook hook = data.<IPlayerRenderHook>getTheClass().newInstance();
			LIClientRegistry.addPlayerRenderingHook(hook);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean registerField(AnnotationData data) {
		try {
			LIClientRegistry.addPlayerRenderingHook(
				(IPlayerRenderHook) data.getTheField().get(null));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
