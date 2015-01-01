package cn.liutils.core.player;

import cn.liutils.api.player.ControlData;
import net.minecraft.client.Minecraft;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * 
 * @author Violet
 *
 */
public class MsgControlSyncAll implements IMessage {

	private ControlData cd = null;
	
	public MsgControlSyncAll(ControlData data) {
		this.cd = data;
	}
	
	public MsgControlSyncAll() {
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		cd = new ControlData(Minecraft.getMinecraft().thePlayer, buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		cd.toBytes(buf);
	}
	
	public static class Handler implements IMessageHandler<MsgControlSyncAll, IMessage> {

		@Override
		public IMessage onMessage(MsgControlSyncAll message, MessageContext ctx) {
			ControlData data = ControlData.get(Minecraft.getMinecraft().thePlayer);
			if (data != null)
				data.copyFrom(message.cd);
			return null;
		}
		
	}
}
