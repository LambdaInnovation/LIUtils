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

import cn.annoreg.base.RegistrationInstance;
import cn.annoreg.core.LoadStage;
import cn.annoreg.core.RegistryTypeDecl;
import cn.liutils.api.render.IPlayerRenderHook;
import cn.liutils.core.LIUtils;
import cn.liutils.core.entity.EntityPlayerHook;
import cn.liutils.registry.PlayerRenderHookRegistry.RegPlayerRenderHook.Pass;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Register of PlayerRenderHook
 * @author WeathFolD
 */
@RegistryTypeDecl
@SideOnly(Side.CLIENT)
public class PlayerRenderHookRegistry 
		extends RegistrationInstance<PlayerRenderHookRegistry.RegPlayerRenderHook, IPlayerRenderHook> {
	
	@Target({ElementType.TYPE, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	@SideOnly(Side.CLIENT)
	public @interface RegPlayerRenderHook {
		enum Pass { OPAQUE, ALPHA };
		Pass value() default Pass.OPAQUE;
	}

	public PlayerRenderHookRegistry() {
		super(RegPlayerRenderHook.class, LIUtils.REGISTER_TYPE_RENDER_HOOK);
		setLoadStage(LoadStage.INIT);
	}

	@Override
	protected void register(IPlayerRenderHook obj, RegPlayerRenderHook anno) throws Exception {
		EntityPlayerHook.regPlayerRenderHook(obj, anno.value() == Pass.ALPHA);
	}

}
