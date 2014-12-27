package cn.liutils.api.player.lock;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.MouseEvent;

public abstract class LockBase {
	public final LockType type;
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
	}
	
	public LockBase(LockType pType, ByteBuf buf) {
		type = pType;
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
	
	public final boolean getEffective() {
		return tick != 0;
	}
	
	public final boolean tick() {
		if (tick < 0)
			return false;
		if (tick-- == 0)
			return true;
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

	public void onMouse(EntityPlayer player, MouseEvent event) {
	}
	public void onKeyboard(EntityPlayer player) {
	}
	public void onTick(EntityPlayer player) {
	}
	
	protected void readBytes(ByteBuf buf) {
	}
	protected void writeBytes(ByteBuf buf) {
	}
}
