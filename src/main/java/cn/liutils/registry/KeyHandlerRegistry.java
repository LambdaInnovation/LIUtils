/**
 * 
 */
package cn.liutils.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.annoreg.core.AnnotationData;
import cn.annoreg.core.RegistryType;
import cn.annoreg.core.RegistryTypeDecl;
import cn.liutils.api.key.IKeyHandler;
import cn.liutils.api.key.LIKeyProcess;
import cn.liutils.core.LIUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeathFolD
 *
 */
@RegistryTypeDecl
@SideOnly(Side.CLIENT)
public class KeyHandlerRegistry extends RegistryType {

	@Target({ElementType.TYPE, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@SideOnly(Side.CLIENT)
	public @interface RegKeyHandler {
		int key();
		boolean isRep() default false;
		String name();
	}
	
	public KeyHandlerRegistry() {
		super(RegKeyHandler.class, LIUtils.REGISTER_TYPE_KEYHANDLER2);
	}

	@Override
	public boolean registerClass(AnnotationData data) {
		Class<? extends IKeyHandler> clazz = data.getTheClass();
		try {
			IKeyHandler ikh = clazz.newInstance();
			doReg(data.<RegKeyHandler>getAnnotation(), ikh);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean registerField(AnnotationData data) {
		try {
			IKeyHandler ikh = (IKeyHandler) data.getTheField().get(null);
			doReg(data.<RegKeyHandler>getAnnotation(), ikh);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private void doReg(RegKeyHandler anno, IKeyHandler ikh) {
		LIKeyProcess.instance.addKey(anno.name(), anno.key(), anno.isRep(), ikh);
	}

}