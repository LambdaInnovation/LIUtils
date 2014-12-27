package cn.liutils.api.player.lock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import cn.liutils.api.player.lock.LockBase.LockType;
import io.netty.buffer.ByteBuf;

public class LockControlJump extends LockBase {

	public static final LockType TYPE = LockType.CONTROL_JUMP;
	
	public LockControlJump(ByteBuf buf) {
		super(TYPE, buf);
	}

	public LockControlJump(int ticks, EntityPlayer player) {
		super(TYPE, ticks);
	}
	
	@Override
	public void onKeyboard(EntityPlayer player) {
		GameSettings gs = Minecraft.getMinecraft().gameSettings;
		KeyBinding.setKeyBindState(gs.keyBindJump.getKeyCode(), false);
	}

}
