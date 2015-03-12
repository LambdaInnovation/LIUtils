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
package cn.liutils.util;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Player experience manipulating
 * （我再次为MC的优秀代码而折服……）
 * @author WeathFolD
 */
public class ExpUtils {
	
	private static int dp[] = new int[666];
	static {
		dp[0] = xpBarCap(0);
		for(int i = 1; i < dp.length; ++i) {
			dp[i] += dp[i - 1] + xpBarCap(i);
		}
	}
	
	/**
	 * Returns the ceiling level value for the given exp.
	 * @param exp experience
	 * @return level that this exp corresponds to, counting start from 1
	 */
	public static int getLevel(int exp) {
		int l = 0, r = dp.length - 1;
		if(exp >= dp[dp.length - 1]) return dp.length;
		//Perform a binary search.
		while(l < r) {
			int m = (l + r) / 2;
			boolean a = dp[m] <= exp;
			if(a) {
				if(exp < dp[m + 1]) return m; //basic assumption: not over level 665
				l = m + 1;
			} else {
				r = m - 1;
			}
		}
		return l + 1;
	}
	
	public static int getTotalExp(EntityPlayer player) {
		return dp[player.experienceLevel] + (int) (player.experience * player.xpBarCap());
	}

    public static int xpBarCap(int experienceLevel) {
        return experienceLevel >= 30 ? 62 + (experienceLevel - 30) * 7 : (experienceLevel >= 15 ? 17 + (experienceLevel - 15) * 3 : 17);
    }
    
    public static boolean consumeExp(EntityPlayer player, int exp) {
    	//TODO: Currently, change not shown in lower bar, our mistake or MC's mistake?
    	int total = getTotalExp(player);
    	if(exp > total) {
    		return false;
    	}
    	
    	while(exp > 0) {
    		int csn = Math.min(exp, (int)(player.experience * player.xpBarCap()));
    		player.experience -= (float)csn / player.xpBarCap();
    		exp -= csn;
    		if(exp > 0) {
    			player.addExperienceLevel(-1);
    			player.experience = 1.0F;
    		}
    	}
    	return true;
    }

}
