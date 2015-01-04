package cn.liutils.api.player.state;

import java.util.Set;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cn.liutils.api.player.state.StateBase.StateType;
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
public class StateLockControlMove extends StateBase {

	public static final StateType TYPE = StateType.LOCK_CONTROL_MOVE;
	
	public StateLockControlMove(EntityPlayer player, ByteBuf buf) {
		super(TYPE, player, buf);
		if (player.worldObj.isRemote)
			fmlDispatcher.registerKeyInput(this);
	}

	public StateLockControlMove(EntityPlayer player, int ticks) {
		super(TYPE, player, ticks);
		if (player.worldObj.isRemote)
			fmlDispatcher.registerKeyInput(this);
	}
	
	@Override
	protected boolean onEvent(Event event) {
		if (event instanceof KeyInputEvent) {
			GameSettings gs = Minecraft.getMinecraft().gameSettings;
			KeyBinding.setKeyBindState(gs.keyBindForward.getKeyCode(), false);
			KeyBinding.setKeyBindState(gs.keyBindLeft.getKeyCode(), false);
			KeyBinding.setKeyBindState(gs.keyBindBack.getKeyCode(), false);
			KeyBinding.setKeyBindState(gs.keyBindRight.getKeyCode(), false);
			return true;
		}
		return false;
	}

}
