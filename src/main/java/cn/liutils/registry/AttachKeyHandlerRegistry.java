/**
 * 
 */
package cn.liutils.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import cn.annoreg.core.AnnotationData;
import cn.annoreg.core.RegistryType;
import cn.annoreg.core.RegistryTypeDecl;
import cn.liutils.api.key.IKeyHandler;
import cn.liutils.api.key.LIKeyProcess;
import cn.liutils.core.LIUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * KeyHandler reg. use this on an integer field to apply id for it.
 * The fields must be initialized before postInit.
 * @author WeathFolD
 */
@RegistryTypeDecl
@SideOnly(Side.CLIENT)
public class AttachKeyHandlerRegistry extends RegistryType {
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@SideOnly(Side.CLIENT)
	public @interface AttachKeyHandler { //Use on Integer or int fields
		Class<? extends IKeyHandler> clazz();
		boolean isRep() default false;
		String name() default "";
	}
	
	public AttachKeyHandlerRegistry() {
		super(AttachKeyHandler.class, LIUtils.REGISTER_TYPE_KEYHANDLER);
	}

	@Override
	public boolean registerClass(AnnotationData data) {
		return false;
	}

	@Override
	public boolean registerField(AnnotationData data) {
		Field f = data.getTheField();
		Class cl = f.getType();
		if(cl == Integer.TYPE || cl == Integer.TYPE) {
			try {
				int kid = f.getInt(null);
				AttachKeyHandler anno = data.<AttachKeyHandler>getAnnotation();
				IKeyHandler ikh = anno.clazz().newInstance();
				String name = anno.name();
				if(name.equals("")) {
					name = anno.clazz().getName();
				}
				LIKeyProcess.instance.addKey(name, kid, anno.isRep(), ikh);
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("Invalid AttachKeyHandler type");
		}
		return true;
	}

}
