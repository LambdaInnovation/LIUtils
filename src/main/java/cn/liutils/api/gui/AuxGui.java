/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.gui;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import cn.liutils.api.key.IKeyHandler;
import cn.liutils.api.key.LIKeyProcess;
import cn.liutils.core.event.LIClientEvents;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Auxillary GUI interface class. This is a kind of GUI that doesn't make mouse gain focus. </br>
 * GUIs such as health indication, information indications are suitable of using this interface to define.
 * The class also provided a set of key-listening functions, based on LIKeyProcess. you can use event-based
 * methods to setup key listening.
 * @author WeathFolD
 */
@SideOnly(Side.CLIENT)
public abstract class AuxGui {
	private LIKeyProcess keyListener;
	public class AuxProcess extends LIKeyProcess {
		final AuxGui gui;
		public AuxProcess(AuxGui _gui) {
			gui = _gui;
			this.mouseOverride = overrideMouse();
		}
		@SubscribeEvent
		@Override
	    public void onClickTick(ClientTickEvent e) {
			if(!gui.isOpen()) return;
	    	if(e.phase == Phase.START) {
	    		keyTick(false);
	    	} else {
	    		keyTick(true);
	    	}
	    }
	}
	
	public AuxGui() {
		if(needKeyListening()) {
			keyListener = new AuxProcess(this);
			FMLCommonHandler.instance().bus().register(keyListener);
		}
	}
	
	public abstract boolean isOpen();
	public abstract void draw(ScaledResolution sr);
	
	protected boolean needKeyListening() {
		return true;
	}
	
	protected void addKeyHandler(KeyBinding kb, boolean rep, IKeyHandler handler) {
		keyListener.addKey(kb, rep, handler);
	}
	
	protected void addKeyHandler(String name, int keyCode, boolean rep, IKeyHandler handler) {
		keyListener.addKey(name, keyCode, rep, handler);
	}
	
	protected boolean overrideMouse() {
		return false;
	}
	
	public static void register(AuxGui gui) {
		LIClientEvents.registerAuxGui(gui);
	}
}
