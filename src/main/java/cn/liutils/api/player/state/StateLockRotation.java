package cn.liutils.api.player.state;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cn.liutils.api.player.state.StateBase.StateType;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * 
 * @author Violet
 *
 */
public class StateLockRotation extends StateBase {

	public static final StateType TYPE = StateType.LOCK_ROTATION;
	
	private float rotationPitch;
	private float rotationYaw;
	private float rotationYawHead;
	private float cameraPitch;
	private float cameraYaw;
	
	public StateLockRotation(EntityPlayer player, ByteBuf buf) {
		super(TYPE, player, buf);
		fmlDispatcher.registerPlayerTick(this);
	}

	public StateLockRotation(EntityPlayer player, int ticks) {
		super(TYPE, player, ticks);
		rotationPitch = player.rotationPitch;
		rotationYaw = player.rotationYaw;
		rotationYawHead = player.rotationYawHead;
		cameraPitch = player.cameraPitch;
		cameraYaw = player.cameraYaw;
		fmlDispatcher.registerPlayerTick(this);
	}

	@Override
	protected boolean onEvent(Event event) {
		if (event instanceof PlayerTickEvent) {
			if (player == ((PlayerTickEvent) event).player) {
				player.prevRotationPitch = player.rotationPitch = rotationPitch;
				player.prevRotationYaw = player.rotationYaw = rotationYaw;
				player.prevRotationYawHead = player.rotationYawHead = rotationYawHead;
				player.prevCameraPitch = player.cameraPitch = cameraPitch;
				player.prevCameraYaw = player.cameraYaw = cameraYaw;
			}
			return true;
		}
		return false;
	}
	
	@Override
	protected void readBytes(ByteBuf buf) {
		rotationPitch = buf.readFloat();
		rotationYaw = buf.readFloat();
		rotationYawHead = buf.readFloat();
		cameraPitch = buf.readFloat();
		cameraYaw = buf.readFloat();
	}

	@Override
	protected void writeBytes(ByteBuf buf) {
		buf.writeFloat(rotationPitch);
		buf.writeFloat(rotationYaw);
		buf.writeFloat(rotationYawHead);
		buf.writeFloat(cameraPitch);
		buf.writeFloat(cameraYaw);
	}

}
