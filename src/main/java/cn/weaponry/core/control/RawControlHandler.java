/**
 * 
 */
package cn.weaponry.core.control;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cn.annoreg.mc.RegSubmoduleInit;
import cn.liutils.api.LIGeneralRegistry;
import cn.liutils.api.key.IKeyHandler;
import cn.liutils.api.key.LIKeyProcess;
import cn.liutils.api.register.Configurable;
import cn.liutils.registry.ConfigurableRegistry.RegConfigurable;
import cn.liutils.util.ClientUtils;
import cn.liutils.util.GenericUtils;
import cn.weaponry.core.Weaponry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeathFolD
 *
 */
@RegistrationClass
@RegSubmoduleInit(side = RegSubmoduleInit.Side.CLIENT_ONLY)
@RegEventHandler(Bus.FML)
@SideOnly(Side.CLIENT)
public class RawControlHandler {
	
	private static class Key implements IKeyHandler {
		
		final int id;
		long lastBeat;
		
		public Key(int _id) {
			id = _id;
		}

		@Override
		public void onKeyDown(int keyCode, boolean tickEnd) {
			if(accepts()) {
				//System.out.println("KeyDown");
				ControlManager.instance.onKeyState(Minecraft.getMinecraft().thePlayer, id, true);
				Weaponry.network.sendToServer(new MsgControl(id, 0));
				lastBeat = GenericUtils.getSystemTime();
			}
		}

		@Override
		public void onKeyUp(int keyCode, boolean tickEnd) {
			if(Minecraft.getMinecraft().thePlayer == null)
				return;
			ControlManager.instance.onKeyState(Minecraft.getMinecraft().thePlayer, id, false);
			Weaponry.network.sendToServer(new MsgControl(id, 1));
		}

		@Override
		public void onKeyTick(int keyCode, boolean tickEnd) {
			if(accepts()) {
				ControlManager.instance.onKeyTick(Minecraft.getMinecraft().thePlayer, id);
				long time = GenericUtils.getSystemTime();
				if(time - lastBeat > ControlManager.BEAT_RATE) {
					lastBeat = time;
					Weaponry.network.sendToServer(new MsgControl(id, 2));
				}
			} else {
				ControlManager.instance.onKeyState(Minecraft.getMinecraft().thePlayer, id, false);
			}
		}
		
		private boolean accepts() {
			return ClientUtils.isPlayerInGame();
		}
		
	}
	
	@Configurable(category = "Control", key = "KEY_S1", defValueInt = LIKeyProcess.MOUSE_LEFT)
	public static int KEY_S1 = LIKeyProcess.MOUSE_LEFT;
	
	@Configurable(category = "Control", key = "KEY_S2", defValueInt = LIKeyProcess.MOUSE_RIGHT)
	public static int KEY_S2 = LIKeyProcess.MOUSE_RIGHT;
	
	@Configurable(category = "Control", key = "KEY_S3", defValueInt = Keyboard.KEY_R)
	public static int KEY_S3 = Keyboard.KEY_R;

	public static void init() {
		LIGeneralRegistry.loadConfigurableClass(Weaponry.config, RawControlHandler.class);
		
		LIKeyProcess.instance.addKey("WM_0", KEY_S1, true, new Key(0));
		LIKeyProcess.instance.addKey("WM_1", KEY_S2, true, new Key(1));
		LIKeyProcess.instance.addKey("WM_2", KEY_S3, true, new Key(2));
	}

}
