/**
 * 
 */
package cn.liutils.api.debug;

import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.input.Keyboard;

import cn.liutils.api.client.register.IKeyProcess;
import cn.liutils.api.debug.command.Command_SetMode;

/**
 * 跟武器位置调试有关的键位处理。
 */
public class KeyMoving implements IKeyProcess {

	int keyTick = 0;
	private static HashSet<Debug_MovingProcessor> processors = new HashSet<Debug_MovingProcessor>();

	public enum EnumKey {
		UP(Keyboard.KEY_UP), DOWN(Keyboard.KEY_DOWN), LEFT(Keyboard.KEY_LEFT), 
		RIGHT(Keyboard.KEY_RIGHT), FORWARD(Keyboard.KEY_NUMPAD8), BACK(Keyboard.KEY_NUMPAD2);
		public int keyCode;
		EnumKey(int key) {
			keyCode = key;
		}
		public static EnumKey getByKeyCode(int keyCode) {
			for(EnumKey e : EnumKey.values()) {
				if(e.keyCode == keyCode)
					return e;
			}
			return null;
		}
	}

	public KeyMoving() {
	}

	public static void addProcess(Debug_MovingProcessor proc) {
		processors.add(proc);
	}

	@Override
	public void onKeyDown(int keyCode, boolean tickEnd) {
		++keyTick;
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.thePlayer;
		if (player != null && mc.currentScreen == null) {
			ItemStack is = player.getCurrentEquippedItem();
			if (is != null) {
				IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(is, ItemRenderType.EQUIPPED_FIRST_PERSON);
				EnumKey e = EnumKey.getByKeyCode(keyCode);
				Debug_MovingProcessor proc = getProcessor(renderer);
				if(proc != null)
					proc.doProcess(renderer, e, Command_SetMode.getMode(), keyTick);
			}
		}
	}
	
	public static Debug_MovingProcessor getProcessor(ItemStack is) {
		IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(is, ItemRenderType.EQUIPPED_FIRST_PERSON);
		if(renderer != null)
			return getProcessor(renderer);
		return null;
	}
	
	public static Debug_MovingProcessor getProcessor(IItemRenderer renderer) {
		if(renderer != null) {
			Iterator<Debug_MovingProcessor> i = processors.iterator();
			while (i.hasNext()) {
				Debug_MovingProcessor proc = i.next();
				if (proc.isProcessAvailable(renderer, Command_SetMode.getMode())) 
					return proc;
			}
		}
		return null;
	}

	@Override
	public void onKeyUp(int keyCode, boolean tickEnd) {
		keyTick = 0;
	}

}
