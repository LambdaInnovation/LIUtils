package cn.liutils.api.player.lock;

import java.util.List;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
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
	
	private void onTick(EntityPlayer player) {
		player.setPosition(posX, posY + player.yOffset, posZ);
	}

	
	
	@Override
	public void onEvent(Event event) {
		if (event instanceof ServerTickEvent) {
			List<EntityPlayerMP> list = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
			for (EntityPlayerMP player : list)
				onTick(player);
			return;
		}
		if (event instanceof ClientTickEvent && Minecraft.getMinecraft().thePlayer != null && !Minecraft.getMinecraft().isGamePaused()) {
			onTick(Minecraft.getMinecraft().thePlayer);
			return;
		}
		incorrect(event);
	}

	@Override
	protected void register() {
		lied.setClientTick.add(this);
		lied.setServerTick.add(this);
	}

	@Override
	protected void unregister() {
		lied.setClientTick.remove(this);
		lied.setServerTick.remove(this);
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
