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
package cn.liutils.api.key;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cn.liutils.core.event.eventhandler.LIHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

/**
 * 统一处理按键的实用类。 请使用addKey(...)注册按键绑定。详见函数本身说明
 * @author WeAthFolD
 */
public class LIKeyProcess {
	
	public static final int MOUSE_LEFT = -100, MOUSE_MIDDLE = -98, MOUSE_RIGHT = -99,
			MWHEELDOWN = -50, MWHEELUP = -49;;
	
	public static final LIKeyProcess instance = new LIKeyProcess();
	static {
		FMLCommonHandler.instance().bus().register(instance);
	}
	
	private Map<String, LIKeyBinding> bindingMap = new HashMap<String, LIKeyBinding>();
	private Map<LIKeyBinding, KeyBinding> associates = new HashMap();
	
	private long lastMouseTime = 0;
	
	public boolean mouseOverride = false;
	
	public static class LIKeyBinding {
		public int keyCode;
		public boolean isRepeat;
		public IKeyHandler process;
		public boolean keyDown;
		public String name;
		
		public LIKeyBinding(String n, int code, boolean repeat, IKeyHandler proc) {
			name = n;
			keyCode = code;
			isRepeat = repeat;
			process = proc;
		}
	}

	/**
	 * adding a key to the process list.
	 * @param key the key
	 * @param isRep repeatly call keyDown when pressing
	 * @param handler handler
	 */
	public LIKeyBinding addKey(String name, int keyCode, boolean isRep, IKeyHandler handler) {
		LIKeyBinding binding = new LIKeyBinding(name, keyCode, isRep, handler);
		bindingMap.put(name, binding);
		return binding;
	}
	
	public LIKeyBinding addKey(KeyBinding b, boolean isRep, IKeyHandler process) {
		LIKeyBinding bd = addKey(b.getKeyDescription(), b.getKeyCode(), isRep, process);
		associates.put(bd, b);
		return bd;
	}
	
	public LIKeyBinding getBindingByName(String s) {
		return bindingMap.get(s);
	}
	
	/**
	 * Get the display name for a specific key code.
	 * @param code
	 * @return A human readable name of the key
	 */
	public static final String getKeyName(int code) {
		if(code > 0) {
			String res = Keyboard.getKeyName(code);
			return res == null ? "" : res;
		}
		if(code == -100) { return "ML"; }
		if(code == -99) { return "MR"; }
		return "M" + (code + 100);
	}
	
	//----------------INTERNAL IMPLEMENTATIONS---------------------
	private final void tickStart()
    {
        keyTick(false);
    }

    private final void tickEnd()
    {
        keyTick(true);
    }
	
    public void keyTick(boolean tickEnd)
    {
    	int dwheel = 0;
    	long time = Mouse.getEventNanoseconds();
    	if(mouseOverride) {
    		while(Mouse.next());
    	}
    	
    	if(this != instance) {
    		int dt = Mouse.getEventDWheel();
    		if(lastMouseTime != time && dt != 0) {
    			dwheel = dt > 0 ? 1 : -1;
    		}
	    	
	    	lastMouseTime = time;
    	}
    	
        for (LIKeyBinding kb : bindingMap.values())
        {
            int keyCode = kb.keyCode;
            boolean state = false;
            if(MWHEELDOWN == keyCode) {
            	state = dwheel < 0;
            } else if(MWHEELUP == keyCode) {
            	state = dwheel > 0;
            } else if(keyCode < -60) {
            	state = Mouse.isButtonDown(keyCode + 100);
            } else if(keyCode > 0){
            	state = Keyboard.isKeyDown(keyCode);
            }
            
            if (state != kb.keyDown || (state && kb.isRepeat))
            {
            	IKeyHandler proc = kb.process;
            	
                if (state) {
                    proc.onKeyDown(keyCode, tickEnd);
                } else {
                	proc.onKeyUp(keyCode, tickEnd);
                }
                if (!tickEnd) {
                    kb.keyDown = state;
                }
            } else if(state) {
            	IKeyHandler handler = kb.process;
            	handler.onKeyTick(keyCode, tickEnd);
            }
            KeyBinding binding = associates.get(kb);
            if(binding != null) kb.keyCode = binding.getKeyCode();
        } 
    }

    @SubscribeEvent
    public void onClickTick(ClientTickEvent e) {
    	if(e.phase == Phase.START) {
    		keyTick(false);
    	} else {
    		keyTick(true);
    	}
    }
    
    /**
     * For fast KeyHandler deploying. register this to LIFMLGameEventDispatcher.
     * @author WeathFolD
     */
    public static class Trigger extends LIHandler<ClientTickEvent> {
		
		LIKeyProcess process;
		
		public Trigger(LIKeyProcess lkp) {
			process = lkp;
		}

		@Override
		protected boolean onEvent(ClientTickEvent event) {
			process.keyTick(event.phase == Phase.END);
			return true;
		}
	}
    
}
