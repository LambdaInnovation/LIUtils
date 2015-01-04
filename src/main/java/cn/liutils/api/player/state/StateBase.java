package cn.liutils.api.player.state;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cn.liutils.core.event.eventhandler.LIFMLGameEventDispatcher;
import cn.liutils.core.event.eventhandler.LIHandler;


/**
 * 
 * @author Violet
 *
 */
public abstract class StateBase extends LIHandler {
	public static final LIFMLGameEventDispatcher fmlDispatcher = LIFMLGameEventDispatcher.INSTANCE;
	public final StateType type;
	public final EntityPlayer player;
	protected int tick;
	
	public static enum StateType {
		FREEMOVE,
		LOCK_POSITION,
		LOCK_ROTATION,
		LOCK_CONTROL_MOVE,
		LOCK_CONTROL_JUMP,
		LOCK_CONTROL_SPIN;
	}
	
	protected StateBase(StateType pType, EntityPlayer pPlayer, int ticks) {
		type = pType;
		player = pPlayer;
		tick = ticks;
	}
	
	public StateBase(StateType pType, EntityPlayer pPlayer, ByteBuf buf) {
		type = pType;
		player = pPlayer;
		fromBytes(buf);
	}
	
	public final int getTick() {
		return tick;
	}
	
	public final void setTick(int ticks) {
		tick = ticks;
	}
	
	public final void modifyTick(int ticks) {
		if (tick < 0)
			return;
		tick += ticks;
		if (tick < 0)
			tick = 0;
	}
	
	public final boolean tick() {
		if (tick < 0)
			return false;
		if (tick-- == 0) {
			cancel();
			return true;
		}
		return false;
	}
	
	public final void cancel() {
		this.tick = 0;
		setDead();
		onDead();
	}

	public final void fromBytes(ByteBuf buf) {
		tick = buf.readInt();
		readBytes(buf);
	}
	
	public final void toBytes(ByteBuf buf) {
		buf.writeInt(tick);
		writeBytes(buf);
	}

	protected void readBytes(ByteBuf buf) {
	}
	protected void writeBytes(ByteBuf buf) {
	}
	protected void onDead() {
	}
}
