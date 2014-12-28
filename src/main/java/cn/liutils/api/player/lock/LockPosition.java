package cn.liutils.api.player.lock;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

/**
 * 
 * @author Violet
 *
 */
public class LockPosition extends LockBase {

	public static final LockType TYPE = LockType.POSITION;
	
	private double posX, posY, posZ;
	
	public LockPosition(ByteBuf buf) {
		super(TYPE, buf);
	}

	public LockPosition(int ticks, EntityPlayer player) {
		super(TYPE, ticks);
		posX = player.posX;
		posY = player.posY;
		posZ = player.posZ;
	}
	
	@Override
	public void onTick(EntityPlayer player) {
		player.addChatComponentMessage(new ChatComponentText("lpos(" + posX + "," + posY + "," + posZ + ").ontick: " + (player.worldObj.isRemote ? "client" : "server")));
		player.setPosition(posX, posY + player.yOffset, posZ);
	}

	@Override
	protected void readBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
	}

	@Override
	protected void writeBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
	}

}
