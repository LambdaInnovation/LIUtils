/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.core.energy;

import ic2.api.energy.tile.IEnergyConductor;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class EnergyPath {
	TileEntity target = null;
	ForgeDirection targetDirection;
	Set<IEnergyConductor> conductors = new HashSet();

	int minX = 2147483647;
	int minY = 2147483647;
	int minZ = 2147483647;
	int maxX = -2147483648;
	int maxY = -2147483648;
	int maxZ = -2147483648;

	double loss = 0.0D;
	int minInsulationEnergyAbsorption = 2147483647;
	int minInsulationBreakdownEnergy = 2147483647;
	int minConductorBreakdownEnergy = 2147483647;
	long totalEnergyConducted = 0L;

}
