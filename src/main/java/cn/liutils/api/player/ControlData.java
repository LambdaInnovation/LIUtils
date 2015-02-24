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
package cn.liutils.api.player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import cn.liutils.api.player.state.StateBase;
import cn.liutils.api.player.state.StateBase.StateType;
import cn.liutils.api.player.state.StateLockControlJump;
import cn.liutils.api.player.state.StateLockControlMove;
import cn.liutils.api.player.state.StateLockControlSpin;
import cn.liutils.api.player.state.StateLockPosition;
import cn.liutils.api.player.state.StateLockRotation;
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

	/**
	 * Get the data
	 */
	public static ControlData get(Entity entity) {
		return (ControlData) entity.getExtendedProperties(IDENTIFIER);
	}
	
	private final EntityPlayer player;
	
	private StateBase[] state = new StateBase[StateType.values().length];
	
	private boolean shouldSync;
	
	public ControlData(EntityPlayer host) {
		player = host;
	}
	
	public ControlData(EntityPlayer host, ByteBuf data) {
		this(host);
		fromBytes(data);
	}
	
	/**
	 * Set state
	 * @param ticks Ticks to cancel the state and negative for infinite
	 */
	public void stateSet(StateType type, int ticks) {
		if (state[type.ordinal()] == null) {
			switch(type) {
			case LOCK_POSITION:
				state[type.ordinal()] = new StateLockPosition(player, ticks);
				break;
			case LOCK_ROTATION:
				state[type.ordinal()] = new StateLockRotation(player, ticks);
				break;
			case LOCK_CONTROL_MOVE:
				state[type.ordinal()] = new StateLockControlMove(player, ticks);
				break;
			case LOCK_CONTROL_JUMP:
				state[type.ordinal()] = new StateLockControlJump(player, ticks);
				break;
			case LOCK_CONTROL_SPIN:
				state[type.ordinal()] = new StateLockControlSpin(player, ticks);
				break;
			default:
				LIUtils.log.warn("Not supported yet: " + type);
			}
		}
		else
			state[type.ordinal()].setTick(ticks);
		shouldSync = true;
	}

	/**
	 * Modify state
	 * @param ticks Positive for increase while negative for decrease
	 */
	public void stateModify(StateType type, int ticks) {
		if (state[type.ordinal()] != null)
			state[type.ordinal()].modifyTick(ticks);
		shouldSync = true;
	}
	
	/**
	 * Cancel some state
	 */
	public void stateCancel(StateType type) {
		if (state[type.ordinal()] != null)
			state[type.ordinal()].cancel();
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
		for (int i = 0; i < state.length; ++i)
			if (state[i] != null && state[i].tick())
				state[i] = null;
	}
	
	private void stateCancelAll() {
		for (int i = 0; i < state.length; ++i)
			if (state[i] != null) {
				state[i].cancel();
				state[i] = null;
			}
	}
	
	private void clear() {
		stateCancelAll();
	}
	
	public void syncAll() {
		shouldSync = false;
		LIUtils.netHandler.sendTo(new MsgControlSyncAll(this), (EntityPlayerMP) player);
	}
	
	public void copyFrom(ControlData cd) {
		clear();
		state = cd.state.clone();
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
					if (pre == StateType.LOCK_POSITION.ordinal()) {
						state[pre] = new StateLockPosition(player, buf);
					}
					if (pre == StateType.LOCK_ROTATION.ordinal()) {
						state[pre] = new StateLockRotation(player, buf);
					}
					if (pre == StateType.LOCK_CONTROL_MOVE.ordinal()) {
						state[pre] = new StateLockControlMove(player, buf);
					}
					if (pre == StateType.LOCK_CONTROL_JUMP.ordinal()) {
						state[pre] = new StateLockControlJump(player, buf);
					}
					if (pre == StateType.LOCK_CONTROL_SPIN.ordinal()) {
						state[pre] = new StateLockControlSpin(player, buf);
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
		for (StateBase lb : state)
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
