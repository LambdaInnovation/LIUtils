package cn.weaponmod.core.proxy;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import cn.liutils.api.util.RegUtils;
import cn.liutils.core.LIUtils;
import cn.liutils.core.client.register.LIKeyProcess;
import cn.liutils.core.debug.FieldModifierHandler;
import cn.weaponmod.api.client.render.ModelBipedHack;
import cn.weaponmod.api.weapon.WeaponDualWield;
import cn.weaponmod.core.client.UpliftHandler;
import cn.weaponmod.core.client.keys.WMKeyHandler;
import cn.weaponmod.core.debug.ModifierProviderModelWeapon;
import cpw.mods.fml.common.FMLCommonHandler;

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
	
	@Override
	public void postInit() {
		try {
			Field field;
			RenderPlayer r = (RenderPlayer) RenderManager.instance.entityRenderMap.get(EntityPlayer.class);
			ModelBipedHack mbh = new ModelBipedHack(1F);
			RegUtils.getObfField(RendererLivingEntity.class, "mainModel", "field_77405_g").set(r, mbh);
			RegUtils.getObfField(RenderPlayer.class, "modelBipedMain", "field_77071_a").set(r, mbh);
			RegUtils.getObfField(RenderPlayer.class, "modelArmorChestplate", "field_77108_b").set(r, new ModelBipedHack(1F));
			RegUtils.getObfField(RenderPlayer.class, "modelArmor", "field_77111_i").set(r, new ModelBipedHack(0.5F));
			System.out.println("Attempted hack");
		} catch(Exception e) {
			e.printStackTrace();
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
