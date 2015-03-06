/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.energy.event;

import ic2.api.energy.event.EnergyTileEvent;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * TileEntity energy-omitting event.
 * TODO adapt to new IC2API, remove this!
 * @author HopeAsd
 */
@Deprecated
public class EnergyTileSourceEvent extends EnergyTileEvent {
	public int amount;
	public World world;

	public EnergyTileSourceEvent(IEnergyTile energyTile,
			int amount) {
		super(energyTile);
		this.amount = amount;
		this.world = ((TileEntity)energyTile).getWorldObj();
	}

}