/**
 * 
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
