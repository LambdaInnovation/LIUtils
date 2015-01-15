/**
 * 
 */
package cn.liutils.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import cn.annoreg.base.RegistrationInstance;
import cn.annoreg.core.LoadStage;
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
public class AuxGuiRegistry extends RegistrationInstance<AuxGuiRegistry.RegAuxGui, AuxGui> {
	
	@Target({ElementType.TYPE, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@SideOnly(Side.CLIENT)
	public @interface RegAuxGui {}

	public AuxGuiRegistry() {
		super(RegAuxGui.class, LIUtils.REGISTER_TYPE_AUXGUI);
		this.setLoadStage(LoadStage.INIT);
	}

	@Override
	protected void register(AuxGui obj, RegAuxGui anno) throws Exception {
		AuxGui.register(obj);
	}

}
