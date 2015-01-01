package cn.liutils.api.player.lock;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import cn.liutils.api.player.lock.LockBase.LockType;
import io.netty.buffer.ByteBuf;

/**
 * 
 * @author Violet
 *
 */
public class LockControlJump extends LockBase {

	public static final LockType TYPE = LockType.CONTROL_JUMP;
	
	public LockControlJump(EntityPlayer player, ByteBuf buf) {
		super(TYPE, player, buf);
		if (player.worldObj.isRemote)
			fmlDispatcher.registerKeyInput(this);
	}

	public LockControlJump(EntityPlayer player, int ticks) {
		super(TYPE, player, ticks);
		if (player.worldObj.isRemote)
			fmlDispatcher.registerKeyInput(this);
	}
	
	@Override
	protected boolean onEvent(Event event) {
		if (event instanceof KeyInputEvent) {
			GameSettings gs = Minecraft.getMinecraft().gameSettings;
			KeyBinding.setKeyBindState(gs.keyBindJump.getKeyCode(), false);
			return true;
		}
		return false;
	}

}
