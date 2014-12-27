package cn.liutils.api.player;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * @author EAirPeter
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
		cd = new ControlData(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		cd.toBytes(buf);
	}
	
	public static class Handler implements IMessageHandler<MsgControlSyncAll, IMessage> {

		@Override
		public IMessage onMessage(MsgControlSyncAll message, MessageContext ctx) {
			Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("onMessage"));
			ControlData data = ControlData.get(Minecraft.getMinecraft().thePlayer);
			if (data != null)
				data.copyFrom(message.cd);
			return null;
		}
		
	}
}
