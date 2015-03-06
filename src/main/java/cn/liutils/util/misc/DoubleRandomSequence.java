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
package cn.liutils.util.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author WeathFolD
 *
 */
public class DoubleRandomSequence {

	List<Double> seq = new ArrayList<Double>();
	
	double from, to;
	
	static Random RNG = new Random();

	public DoubleRandomSequence(int n, int fr, int t) {
		from = fr;
		to = t;
		rebuild(n);
	}
	
	public int size() {
		return seq.size();
	}
	
	public double get(int i) {
		return seq.get(i);
	}
	
	public void rebuild() {
		rebuild(seq.size());
	}
	
	public void rebuild(int size) {
		seq.clear();
		for(int i = 0; i < size; ++i) {
			seq.add(intvRand());
		}
	}
	
	private double intvRand() {
		return RNG.nextDouble() * (to - from) + from;
	}

}
