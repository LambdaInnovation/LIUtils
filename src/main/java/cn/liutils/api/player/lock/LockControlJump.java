package cn.liutils.api.player.lock;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import cn.liutils.api.player.lock.LockBase.LockType;
import cn.liutils.core.event.eventhandler.LIEventDispatcher;
import io.netty.buffer.ByteBuf;

/**
 * 
 * @author Violet
 *
 */
public class LockControlJump extends LockBase {

	public static final LockType TYPE = LockType.CONTROL_JUMP;
	
	public LockControlJump(ByteBuf buf) {
		super(TYPE, buf);
	}

	public LockControlJump(int ticks, EntityPlayer player) {
		super(TYPE, ticks);
	}
	
	@Override
	public void onEvent(Event event) {
		if (event instanceof KeyInputEvent) {
			GameSettings gs = Minecraft.getMinecraft().gameSettings;
			KeyBinding.setKeyBindState(gs.keyBindJump.getKeyCode(), false);
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
