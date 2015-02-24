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
