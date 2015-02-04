/**
 * 
 */
package cn.weaponry.core.control;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegMessageHandler;
import cn.annoreg.mc.RegMessageHandler.Side;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * @author WeathFolD
 *
 */
@RegistrationClass
public class MsgControl implements IMessage {
	
	int kid;
	int state;

	public MsgControl(int _kid, int _state) {
		kid = _kid;
		state = _state;
	}
	
	public MsgControl() {}

	@Override
	public void fromBytes(ByteBuf buf) {
		byte data = buf.readByte();
		kid = data & 3;
		state = data >> 2;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(kid | state << 2);
	}
	
	@RegMessageHandler(msg = MsgControl.class, side = Side.SERVER)
	public static class Handler implements IMessageHandler<MsgControl, IMessage> {

		@Override
		public IMessage onMessage(MsgControl msg, MessageContext ctx) {
			EntityPlayer player = ctx.getServerHandler().playerEntity;
			switch(msg.state) {
			case 0: //down
				ControlManager.instance.onKeyState(player, msg.kid, true);
				break;
			case 1: //up
				ControlManager.instance.onKeyState(player, msg.kid, false);
				break;
			case 2: //beat
				ControlManager.instance.onKeyTick(player, msg.kid);
				break;
			}
			return null;
		}
		
	}

}
