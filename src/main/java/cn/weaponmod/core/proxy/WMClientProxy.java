package cn.weaponmod.core.proxy;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cn.liutils.core.LIUtils;
import cn.liutils.core.client.register.LIKeyProcess;
import cn.liutils.core.debug.FieldModifierHandler;
import cn.weaponmod.api.weapon.WeaponDualWield;
import cn.weaponmod.core.client.UpliftHandler;
import cn.weaponmod.core.client.keys.WMKeyHandler;
import cn.weaponmod.core.debug.ModifierProviderModelWeapon;

public class WMClientProxy extends WMCommonProxy{

	Minecraft mc = Minecraft.getMinecraft();
	
	public static UpliftHandler upliftHandler = new UpliftHandler();
	private static UpliftHandler ulOrigin = upliftHandler;
	
	// This enable one to access LIKeyProcess directly and set the key binding to another.
	// Although, not quite recommended for multi-mod support hasn't been added now.
	public static final String
		KEY_ID_RELOAD = "wm_reload";
	
	@Override
	public void preInit() { 
		super.preInit();
		FMLCommonHandler.instance().bus().register(upliftHandler);
		FMLCommonHandler.instance().bus().register(WeaponDualWield.fakeUpl);
		LIKeyProcess.addKey(mc.gameSettings.keyBindAttack, false, new WMKeyHandler(0));
		LIKeyProcess.addKey(mc.gameSettings.keyBindUseItem, false, new WMKeyHandler(1));
		LIKeyProcess.addKey(KEY_ID_RELOAD, Keyboard.KEY_R, false, new WMKeyHandler(2));
		
		if(LIUtils.DEBUG) {
			FieldModifierHandler.all.add(new ModifierProviderModelWeapon());
		}
	}
	
	public synchronized static void switchUpl(UpliftHandler dup) {
		//FMLCommonHandler.instance().bus().unregister(ulOrigin);
		//FMLCommonHandler.instance().bus().register(dup);
		upliftHandler = dup;
	}
	
	public synchronized static void restoreUpl() {
		//FMLCommonHandler.instance().bus().register(ulOrigin);
		//FMLCommonHandler.instance().bus().unregister(upliftHandler);
		upliftHandler = ulOrigin;
	}
	
	@Override
	public void init() {
		super.init();
	}
	
}
