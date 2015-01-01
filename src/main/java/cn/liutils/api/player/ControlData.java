package cn.liutils.api.player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.liutils.api.player.lock.LockBase;
import cn.liutils.api.player.lock.LockBase.LockType;
import cn.liutils.api.player.lock.LockControlJump;
import cn.liutils.api.player.lock.LockControlMove;
import cn.liutils.api.player.lock.LockControlSpin;
import cn.liutils.api.player.lock.LockPosition;
import cn.liutils.api.player.lock.LockRotation;
import cn.liutils.core.LIUtils;
import cn.liutils.core.player.MouseHelperX;
import cn.liutils.core.player.MsgControlSyncAll;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * 
 * @author Violet
 *
 */
public class ControlData implements IExtendedEntityProperties {

public static final String IDENTIFIER = "li_con";
	
	public static final LockType[] CONTROLS = new LockType[] {LockType.CONTROL_MOVE, LockType.CONTROL_JUMP, LockType.CONTROL_MOVE, LockType.CONTROL_SPIN};
	
	/**
	 * Get the data
	 */
	public static ControlData get(Entity entity) {
		return (ControlData) entity.getExtendedProperties(IDENTIFIER);
	}
	
	private final EntityPlayer player;
	
	private LockBase[] lock = new LockBase[LockType.values().length];
	
	private boolean shouldSync;
	
	public ControlData(EntityPlayer host) {
		player = host;
	}
	
	public ControlData(EntityPlayer host, ByteBuf buf) {
		player = host;
		fromBytes(buf);
	}
	
	/**
	 * Set lock state(s)
	 * @param ticks Ticks to unlock the state and negative for infinite
	 */
	public void lockSet(LockType type, int ticks) {
		switch(type) {
		case ALL:
			for (LockType lt : LockType.values())
				if (!lt.equals(LockType.ALL) && !lt.equals(LockType.CONTROL_ALL))
					doLockSet(lt, ticks);
			break;
		case CONTROL_ALL:
			for (LockType lt : CONTROLS)
				doLockSet(lt, ticks);
			break;
		default:
			doLockSet(type, ticks);
			break;
		}
		shouldSync = true;
	}

	/**
	 * Modify lock state(s)
	 * @param ticks Positive for increase while negative for decrease
	 */
	public void lockModify(LockType type, int ticks) {
		switch(type) {
		case ALL:
			for (LockBase lb : lock)
				doLockModify(lb, ticks);
			break;
		case CONTROL_ALL:
			for (LockType lt : CONTROLS)
				doLockModify(lock[lt.ordinal()], ticks);
			break;
		default:
			doLockModify(lock[type.ordinal()], ticks);
			break;
		}
		shouldSync = true;
	}
	
	/**
	 * Unlock something
	 */
	public void lockCancel(LockType type) {
		switch(type) {
		case ALL:
			for (int i = 0; i < lock.length; ++i)
				if (lock[i] != null) {
					lock[i].cancel();
					lock[i] = null;
				}
			break;
		case CONTROL_ALL:
			for (LockType lt : CONTROLS)
				if (lock[lt.ordinal()] != null) {
					lock[lt.ordinal()].cancel();
					lock[lt.ordinal()] = null;
				}
			break;
		default:
			lock[type.ordinal()].cancel();
			lock[type.ordinal()] = null;
			break;
		}
		shouldSync = true;
	}
	
	/**
	 * Called by ControlManager
	 */
	public void tickSync() {
		if (shouldSync)
			syncAll();
	}
	
	/**
	 * Never call it directly
	 */
	public void tick() {
		for (int i = 0; i < lock.length; ++i)
			if (lock[i] != null && lock[i].tick())
				lock[i] = null;
	}
	
	private void doLockSet(LockType lt, int ticks) {
		if (lock[lt.ordinal()] == null) {
			switch(lt) {
			case POSITION:
				lock[lt.ordinal()] = new LockPosition(player, ticks);
				break;
			case ROTATION:
				lock[lt.ordinal()] = new LockRotation(player, ticks);
				break;
			case CONTROL_MOVE:
				lock[lt.ordinal()] = new LockControlMove(player, ticks);
				break;
			case CONTROL_JUMP:
				lock[lt.ordinal()] = new LockControlJump(player, ticks);
				break;
			case CONTROL_SPIN:
				lock[lt.ordinal()] = new LockControlSpin(player, ticks);
				break;
			default:
				LIUtils.log.warn("Not supported yet: " + lt);
			}
		}
		else
			lock[lt.ordinal()].setTick(ticks);
	}
	
	private void doLockModify(LockBase lb, int ticks) {
		if (lb != null)
			lb.modifyTick(ticks);
	}
	
	private void clear() {
		lockCancel(LockType.ALL);
	}
	
	public void syncAll() {
		shouldSync = false;
		LIUtils.netHandler.sendTo(new MsgControlSyncAll(this), (EntityPlayerMP) player);
	}
	
	public void copyFrom(ControlData cd) {
		clear();
		lock = cd.lock.clone();
	}
	
	public void fromBytes(ByteBuf buf) {
		clear();
		while (buf.isReadable()) {
			byte tp = buf.readByte();
			switch(tp) {
			case 0:
				while (buf.isReadable()) {
				byte pre = buf.readByte();
					if (pre == -1)
						break;
					if (pre == LockType.POSITION.ordinal()) {
						lock[pre] = new LockPosition(player, buf);
					}
					if (pre == LockType.ROTATION.ordinal()) {
						lock[pre] = new LockRotation(player, buf);
					}
					if (pre == LockType.CONTROL_MOVE.ordinal()) {
						lock[pre] = new LockControlMove(player, buf);
					}
					if (pre == LockType.CONTROL_JUMP.ordinal()) {
						lock[pre] = new LockControlJump(player, buf);
					}
					if (pre == LockType.CONTROL_SPIN.ordinal()) {
						lock[pre] = new LockControlSpin(player, buf);
					}
				}
				break;
			case -1:
				return;
			default:
				LIUtils.log.warn("Unknown type: " + tp);
			}
		}
	}
	
	public void toBytes(ByteBuf buf) {
		buf.writeByte(0);
		for (LockBase lb : lock)
			if (lb != null) {
				buf.writeByte(lb.type.ordinal());
				lb.toBytes(buf);
			}
		buf.writeByte(-1);
		
		buf.writeByte(-1);
	}
	
	@Override
	public void saveNBTData(NBTTagCompound tag) {
		ByteBuf buf = Unpooled.buffer();
		toBytes(buf);
		tag.setByteArray(IDENTIFIER, buf.array());
	}

	@Override
	public void loadNBTData(NBTTagCompound tag) {
		byte[] bytes = tag.getByteArray(IDENTIFIER);
		if (bytes.length > 0)
			fromBytes(Unpooled.wrappedBuffer(bytes));
		else
			clear();
	}

	@Override
	public void init(Entity entity, World world) {
		if (!(entity instanceof EntityPlayer)) {
			LIUtils.log.warn("Registering ControlData for a(n)" + entity.getClass().getName());
			return;
		}
	}
}
