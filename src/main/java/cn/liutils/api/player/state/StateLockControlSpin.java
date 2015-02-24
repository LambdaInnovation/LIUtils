/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.player.state;

import org.lwjgl.input.Mouse;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cn.liutils.core.player.MouseHelperX;

/**
 * 
 * @author Violet
 *
 */
public class StateLockControlSpin extends StateBase {

	public static final StateType TYPE = StateType.LOCK_CONTROL_SPIN;
	
	private int x, y;
	
	public StateLockControlSpin(EntityPlayer player, ByteBuf buf) {
		super(TYPE, player, buf);
		x = Mouse.getX();
		y = Mouse.getY();
		if (player.worldObj.isRemote)
			fmlDispatcher.registerRenderTick(this);
	}

	public StateLockControlSpin(EntityPlayer player, int ticks) {
		super(TYPE, player, ticks);
		x = Mouse.getX();
		y = Mouse.getY();
		if (player.worldObj.isRemote)
			fmlDispatcher.registerRenderTick(this);
	}
	
	@Override
	protected boolean onEvent(Event event) {
		if (event instanceof RenderTickEvent) {
			if (((RenderTickEvent) event).phase == Phase.START) {
				MouseHelperX.lock();
				if (Minecraft.getMinecraft().currentScreen == null)
					Mouse.setCursorPosition(x, y);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void onDead() {
		MouseHelperX.unlock();
	}
}
