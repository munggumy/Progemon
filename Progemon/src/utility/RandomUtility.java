package utility;

import java.util.Random;

public class RandomUtility {
	
	private static Random random = new Random();
	/** Inclusive Random Integer.*/
	public static int randomInt(int start, int end){
		return random.nextInt(end - start + 1) + start;
	}
	
	/** Inclusive Random Integer Starting from 0 */
	public static int randomInt(int end){
		return random.nextInt(end + 1);
	}

}
