package cn.liutils.api.player.state;

import cn.liutils.api.player.state.StateBase.StateType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.eventhandler.Event;

public class StateFreeMove extends StateBase {

	public static final StateType TYPE = StateType.FREEMOVE;
	
	public StateFreeMove(EntityPlayer player, ByteBuf buf) {
		super(TYPE, player, buf);
		if (player.worldObj.isRemote)
			fmlDispatcher.registerKeyInput(this);
	}

	public StateFreeMove(EntityPlayer player, int ticks) {
		super(TYPE, player, ticks);
		if (player.worldObj.isRemote)
			fmlDispatcher.registerKeyInput(this);
	}
	
	@Override
	protected boolean onEvent(Event event) {
		// TODO Auto-generated method stub
		return false;
	}

}
