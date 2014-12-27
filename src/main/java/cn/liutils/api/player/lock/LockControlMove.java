package cn.liutils.api.player.lock;

import java.util.Set;

import org.lwjgl.input.Keyboard;

import cn.liutils.api.player.lock.LockBase.LockType;
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
	public void onKeyboard(EntityPlayer player) {
		GameSettings gs = Minecraft.getMinecraft().gameSettings;
		KeyBinding.setKeyBindState(gs.keyBindForward.getKeyCode(), false);
		KeyBinding.setKeyBindState(gs.keyBindLeft.getKeyCode(), false);
		KeyBinding.setKeyBindState(gs.keyBindBack.getKeyCode(), false);
		KeyBinding.setKeyBindState(gs.keyBindRight.getKeyCode(), false);
	}

}
