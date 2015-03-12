/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import cn.annoreg.base.RegistrationFieldSimple;
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
public class AttachKeyHandlerRegistry 
		extends RegistrationFieldSimple<AttachKeyHandlerRegistry.RegAttachKeyHandler, Integer> {
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@SideOnly(Side.CLIENT)
	public @interface RegAttachKeyHandler { //Use on Integer or int fields
		Class<? extends IKeyHandler> clazz();
		boolean isRep() default false;
		String name() default "";
	}
	
	public AttachKeyHandlerRegistry() {
		super(RegAttachKeyHandler.class, LIUtils.REGISTER_TYPE_KEYHANDLER);
	}

	@Override
	protected void register(Integer value, RegAttachKeyHandler anno, String field) throws Exception {
		IKeyHandler ikh = anno.clazz().newInstance();
		LIKeyProcess.instance.addKey(this.getSuggestedName(), value, anno.isRep(), ikh);
	}

}
