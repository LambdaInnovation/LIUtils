/**
 * 
 */
package cn.liutils.core.network;

import cn.liutils.api.block.IMetadataProvider;
import cn.liutils.api.block.TileDirectionedMulti;
import cn.liutils.core.LIUtilsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * @author WeathFolD
 *
 */
public class MsgTileDMulti implements IMessage {

	int x, y, z;
	int metadata;
	
	public MsgTileDMulti(TileEntity tile) {
		x = tile.xCoord;
		y = tile.yCoord;
		z = tile.zCoord;
		metadata = ((IMetadataProvider)tile).getMetadata();
	}
	
	public MsgTileDMulti() {}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		metadata = buf.readInt();
	}

	/* (non-Javadoc)
	 * @see cpw.mods.fml.common.network.simpleimpl.IMessage#toBytes(io.netty.buffer.ByteBuf)
	 */
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(metadata);
	}
	
	public static class Request implements IMessage {
		
		int x, y, z;
		
		public Request(TileEntity tile) {
			x = tile.xCoord;
			y = tile.yCoord;
			z = tile.zCoord;
		}
		
		public Request() {}

		@Override
		public void fromBytes(ByteBuf buf) {
			x = buf.readInt();
			y = buf.readInt();
			z = buf.readInt();
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(x);
			buf.writeInt(y);
			buf.writeInt(z);
		}
		
		public static class Handler implements IMessageHandler<Request, IMessage> {

			@Override
			public IMessage onMessage(Request msg, MessageContext ctx) {
				World wrld = ctx.getServerHandler().playerEntity.worldObj;
				TileEntity te = wrld.getTileEntity(msg.x, msg.y, msg.z);
				if(te == null || !(te instanceof IMetadataProvider)) {
					System.err.println("It seems we don't have the correspond tile entity for the client sync request");
					return null;
				}
				LIUtilsMod.netHandler.sendTo(new MsgTileDMulti(te), ctx.getServerHandler().playerEntity);
				return null;
			}
			
		}
		
	}
	
	public static class Handler implements IMessageHandler<MsgTileDMulti, IMessage> {

		@Override
		public IMessage onMessage(MsgTileDMulti msg, MessageContext ctx) {
			World world = Minecraft.getMinecraft().theWorld;
			TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
			if(te == null || !(te instanceof IMetadataProvider)) {
				return null;
			}
			
			((IMetadataProvider)te).setMetadata(msg.metadata);
			return null;
		}
		
	}

}
