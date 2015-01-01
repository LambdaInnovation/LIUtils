package cn.liutils.api.player.lock;

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
public class LockControlSpin extends LockBase {

	public static final LockType TYPE = LockType.CONTROL_SPIN;
	
	private int x, y;
	
	public LockControlSpin(EntityPlayer player, ByteBuf buf) {
		super(TYPE, player, buf);
		x = Mouse.getX();
		y = Mouse.getY();
		if (player.worldObj.isRemote)
			fmlDispatcher.registerRenderTick(this);
	}

	public LockControlSpin(EntityPlayer player, int ticks) {
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
