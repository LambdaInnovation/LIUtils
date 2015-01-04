/** 
 * Copyright (c) LambdaCraft Modding Team, 2013
 * 版权许可：LambdaCraft 制作小组， 2013.
 * http://lambdacraft.half-life.cn/
 * 
 * LambdaCraft is open-source. It is distributed under the terms of the
 * LambdaCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 *
 * LambdaCraft是完全开源的。它的发布遵从《LambdaCraft开源协议》。你允许阅读，修改以及调试运行
 * 源代码， 然而你不允许将源代码以另外任何的方式发布，除非你得到了版权所有者的许可。
 */
package cn.liutils.api.key;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

/**
 * 统一处理按键的实用类。 请使用addKey(...)注册按键绑定。详见函数本身说明
 * @author WeAthFolD
 */
public class LIKeyProcess {
	
	public static final int MOUSE_LEFT = -100, MOUSE_MIDDLE = -98, MOUSE_RIGHT = -99;
	
	public static final LIKeyProcess instance = new LIKeyProcess();
	static {
		FMLCommonHandler.instance().bus().register(instance);
	}
	
	private Map<String, LIKeyBinding> bindingMap = new HashMap<String, LIKeyBinding>();
	private Map<LIKeyBinding, KeyBinding> associates = new HashMap();
	
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
	
    protected void keyTick(boolean tickEnd)
    {
        for (LIKeyBinding kb : bindingMap.values())
        {
            int keyCode = kb.keyCode;
            boolean state = (keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode));
            if (state != kb.keyDown || (state && kb.isRepeat))
            {
            	IKeyHandler proc = kb.process;
            	
                if (state)
                {
                    proc.onKeyDown(keyCode, tickEnd);
                }
                else
                {
                   proc.onKeyUp(keyCode, tickEnd);
                }
                if (tickEnd)
                {
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
    
}
