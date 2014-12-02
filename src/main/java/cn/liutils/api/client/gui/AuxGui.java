/**
 * 
 */
package cn.liutils.api.client.gui;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import cn.liutils.api.client.key.IKeyHandler;
import cn.liutils.core.client.register.LIKeyProcess;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

/**
 * Auxillary GUI interface class. This is a kind of GUI that doesn't make mouse gain focus. </br>
 * GUIs such as health indication, information indications are suitable of using this interface to define.
 * The class also provided a set of key-listening fucntions, based on LIKeyProcess. you can use event-based key listening
 * methods to setup key listening.
 * @author WeathFolD
 */
public abstract class AuxGui {
	private LIKeyProcess keyListener;
	
	public AuxGui() {
		if(needKeyListening()) {
			keyListener = new LIKeyProcess() {
				@SubscribeEvent
				@Override
			    public void onClickTick(ClientTickEvent e) {
					if(!doesEnable()) return;
			    	if(e.phase == Phase.START) {
			    		keyTick(false);
			    	} else {
			    		keyTick(true);
			    	}
			    }
			};
			FMLCommonHandler.instance().bus().register(keyListener);
		}
	}
	
	public abstract boolean doesEnable();
	public abstract void draw(ScaledResolution sr);
	
	protected boolean needKeyListening() {
		return false;
	}
	
	protected void addKeyHandler(KeyBinding kb, boolean rep, IKeyHandler handler) {
		keyListener.addKey(kb, rep, handler);
	}
	
	protected void addKeyHandler(String name, int keyCode, boolean rep, IKeyHandler handler) {
		keyListener.addKey(name, keyCode, rep, handler);
	}
}
