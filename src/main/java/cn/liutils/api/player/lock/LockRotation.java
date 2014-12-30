package cn.liutils.api.player.lock;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cn.liutils.api.player.lock.LockBase.LockType;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * 
 * @author Violet
 *
 */
public class LockRotation extends LockBase {

	public static final LockType TYPE = LockType.ROTATION;
	
	private float rotationPitch;
	private float rotationYaw;
	private float rotationYawHead;
	private float cameraPitch;
	private float cameraYaw;
	
	
	public LockRotation(ByteBuf buf) {
		super(TYPE, buf);
	}

	public LockRotation(int ticks, EntityPlayer player) {
		super(TYPE, ticks);
		rotationPitch = player.rotationPitch;
		rotationYaw = player.rotationYaw;
		rotationYawHead = player.rotationYawHead;
		cameraPitch = player.cameraPitch;
		cameraYaw = player.cameraYaw;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof PlayerTickEvent) {
			EntityPlayer player = ((PlayerTickEvent) event).player;
			player.prevRotationPitch = player.rotationPitch = rotationPitch;
			player.prevRotationYaw = player.rotationYaw = rotationYaw;
			player.prevRotationYawHead = player.rotationYawHead = rotationYawHead;
			player.prevCameraPitch = player.cameraPitch = cameraPitch;
			player.prevCameraYaw = player.cameraYaw = cameraYaw;
		}
		incorrect(event);
	}
	
	@Override
	public void register() {
		lied.setPlayerTick.add(this);
	}
	
	@Override
	public void unregister() {
		lied.setPlayerTick.remove(this);
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
