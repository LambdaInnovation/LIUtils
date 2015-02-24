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
package cn.liutils.util.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Builds a sequence of integer of size n whose elements are all in range[0, max).
 * @author WeathFolD
 *
 */
public class IntRandomSequence {
	
	List<Integer> seq = new ArrayList<Integer>();
	
	int max;
	
	static Random RNG = new Random();

	public IntRandomSequence(int n, int max) {
		this.max = max;
		for(int i = 0; i < n; ++i) {
			seq.add(RNG.nextInt(max));
		}
	}
	
	public int size() {
		return seq.size();
	}
	
	public int get(int i) {
		return seq.get(i);
	}
	
	public void rebuild() {
		for(int i = 0; i < size(); ++i) {
			seq.set(i, RNG.nextInt(max));
		}
	}

}
