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
package cn.liutils.api.player.state;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import cn.liutils.api.player.state.StateBase.StateType;
import io.netty.buffer.ByteBuf;

/**
 * 
 * @author Violet
 *
 */
public class StateLockControlJump extends StateBase {

	public static final StateType TYPE = StateType.LOCK_CONTROL_JUMP;
	
	public StateLockControlJump(EntityPlayer player, ByteBuf buf) {
		super(TYPE, player, buf);
		if (player.worldObj.isRemote)
			fmlDispatcher.registerKeyInput(this);
	}

	public StateLockControlJump(EntityPlayer player, int ticks) {
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
