package cn.liutils.api.player.lock;

import java.util.Set;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cn.liutils.api.player.lock.LockBase.LockType;
import cn.liutils.core.event.eventhandler.LIEventDispatcher;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

/**
 * 
 * @author Violet
 *
 */
public class LockControlMove extends LockBase {

	public static final LockType TYPE = LockType.CONTROL_MOVE;
	
	public LockControlMove(ByteBuf buf) {
		super(TYPE, buf);
	}

	public LockControlMove(int ticks, EntityPlayer player) {
		super(TYPE, ticks);
	}
	
	@Override
	public void onEvent(Event event) {
		if (event instanceof KeyInputEvent) {
			GameSettings gs = Minecraft.getMinecraft().gameSettings;
			KeyBinding.setKeyBindState(gs.keyBindForward.getKeyCode(), false);
			KeyBinding.setKeyBindState(gs.keyBindLeft.getKeyCode(), false);
			KeyBinding.setKeyBindState(gs.keyBindBack.getKeyCode(), false);
			KeyBinding.setKeyBindState(gs.keyBindRight.getKeyCode(), false);
			return;
		}
		incorrect(event);
	}

	@Override
	protected void register() {
		lied.setKeyInput.add(this);
	}

	@Override
	protected void unregister() {
		lied.setKeyInput.remove(this);
	}

}
