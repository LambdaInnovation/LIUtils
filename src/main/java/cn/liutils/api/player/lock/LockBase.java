package cn.liutils.api.player.lock;

import cpw.mods.fml.common.eventhandler.Event;
import cn.liutils.core.event.eventhandler.LIEventDispatcher;
import cn.liutils.core.event.eventhandler.LIIHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.MouseEvent;

/**
 * 
 * @author Violet
 *
 */
public abstract class LockBase implements LIIHandler {
	public final LockType type;
	protected static final LIEventDispatcher lied = LIEventDispatcher.instance();
	protected int tick;
	
	public static enum LockType {
		ALL,
		POSITION,
		ROTATION,
		CONTROL_ALL,
		CONTROL_MOVE,
		CONTROL_JUMP,
		CONTROL_SPIN;
	}
	
	protected LockBase(LockType pType, int ticks) {
		type = pType;
		tick = ticks;
		register();
	}
	
	public LockBase(LockType pType, ByteBuf buf) {
		type = pType;
		fromBytes(buf);
		register();
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
	
	public final boolean getEffective() {
		return tick != 0;
	}
	
	public final boolean tick() {
		if (tick < 0)
			return false;
		if (tick-- == 0) {
			unregister();
		}
		return false;
	}

	public final void fromBytes(ByteBuf buf) {
		tick = buf.readInt();
		readBytes(buf);
	}
	
	public final void toBytes(ByteBuf buf) {
		buf.writeInt(tick);
		writeBytes(buf);
	}
	
	protected final void incorrect(Event event) {
		throw new RuntimeException("Incorrect event(" + event.getClass().getName() + ") for " + this.getClass().getName());
	}
	
	protected abstract void register();
	protected abstract void unregister();
	protected void readBytes(ByteBuf buf) {
	}
	protected void writeBytes(ByteBuf buf) {
	}
}
