package cn.liutils.api.register;

import java.util.HashMap;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * 根据TileEntity的类型自动获取对应GUI的统一处理类。请配合IChannelProcess使用。 TODO:加入对按键等其他情况打开Gui的支持。
 * 
 * @see IChannelProcess
 * @author WeAThFolD
 */
public class LIGuiHandler implements IGuiHandler {

	public HashMap<Integer, IGuiElement> guis = new HashMap();

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (guis.containsKey(ID)) {
			Object gui = guis.get(ID)
					.getServerContainer(player, world, x, y, z);
			return gui;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if (guis.containsKey(ID)) {
			Object gui = guis.get(ID).getClientGui(player, world, x, y, z);
			return gui;
		}
		return null;
	}

	public void addGuiElement(int id, IGuiElement process) {
		guis.put(id, process);
	}

}
