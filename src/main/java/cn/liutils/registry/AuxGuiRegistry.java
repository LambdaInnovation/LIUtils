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
import cn.liutils.api.gui.AuxGui;
import cn.liutils.core.LIUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * AuxGui register annotation.
 * @author WeathFolD
 */
@RegistryTypeDecl
@SideOnly(Side.CLIENT)
public class AuxGuiRegistry extends RegistryType {
	
	@Target({ElementType.TYPE, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@SideOnly(Side.CLIENT)
	public @interface RegAuxGui {}

	public AuxGuiRegistry() {
		super(RegAuxGui.class, LIUtils.REGISTER_TYPE_AUXGUI);
	}

	@Override
	public boolean registerClass(AnnotationData data) {
		Class<? extends AuxGui> clazz = (Class<? extends AuxGui>) data.reflect;
		try {
			AuxGui.register(clazz.newInstance());
			return true;
		} catch(Exception e) {
			LIUtils.log.error("Exception regging AuxGui class" + clazz);
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean registerField(AnnotationData data) {
		Field f = data.getTheField();
		System.out.println("registerField " + f);
		try {
			AuxGui.register((AuxGui) f.get(null));
			return true;
		} catch(Exception e) {
			LIUtils.log.error("Exception regging AuxGui field" + f);
			e.printStackTrace();
		}
		return false;
	}

}
