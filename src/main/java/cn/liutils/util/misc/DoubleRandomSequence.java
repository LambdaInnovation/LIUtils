/**
 * 
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
