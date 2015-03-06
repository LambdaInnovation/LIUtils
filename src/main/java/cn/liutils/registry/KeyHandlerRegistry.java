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
	@Deprecated
	/**
	 * key is not configurable. Use AttachKeyHandler instead.
	 */
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
		Class<? extends IKeyHandler> clazz = (Class<? extends IKeyHandler>) data.getTheClass();
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
