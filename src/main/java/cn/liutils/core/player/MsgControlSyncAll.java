package cn.liutils.core.player;

import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegMessageHandler;
import cn.liutils.api.player.ControlData;
import net.minecraft.client.Minecraft;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Violet
 *
 */
@RegistrationClass
public class MsgControlSyncAll implements IMessage {

	private ControlData cd = null;
	private ByteBuf buf = null;
	
	public MsgControlSyncAll(ControlData data) {
		this.cd = data;
	}
	
	public MsgControlSyncAll() {
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void fromBytes(ByteBuf buf) {
		this.buf = buf;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		cd.toBytes(buf);
	}
	
	@RegMessageHandler(msg = MsgControlSyncAll.class, side = RegMessageHandler.Side.CLIENT)
	public static class Handler implements IMessageHandler<MsgControlSyncAll, IMessage> {

		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MsgControlSyncAll message, MessageContext ctx) {
			ControlData data = ControlData.get(Minecraft.getMinecraft().thePlayer);
			ControlData cd = new ControlData(Minecraft.getMinecraft().thePlayer, message.buf);
			if (data != null)
				data.copyFrom(cd);
			return null;
		}
		
	}
}
