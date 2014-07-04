package cn.liutils.api.register;

import java.io.DataInputStream;

import cpw.mods.fml.common.network.Player;

public interface IChannelProcess {
	public void onPacketData(DataInputStream stream, Player player);
}
