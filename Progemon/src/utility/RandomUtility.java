package utility;

import java.util.List;
import java.util.Random;

public class RandomUtility {

	private static Random random = new Random();

	/** Inclusive Random Integer. */
	public static int randomInt(int start, int end) {
		return random.nextInt(end - start + 1) + start;
	}

	/** Inclusive Random Integer Starting from 0 */
	public static int randomInt(int end) {
		return random.nextInt(end + 1);
	}

	/** Gets random element from array */
	public static <T extends Object> T randomElement(List<T> array) {
		try {
			return array.get(random.nextInt(array.size()));
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("array is empty", ex);
		}
	}

	/**
	 * 
	 * @param end
	 *            End range percentage in % unit.
	 * @return
	 */
	public static double randomPercent(int end) {
		return random.nextDouble() * end * 0.01;
	}

	/**
	 * 
	 * @param start
	 *            Start range percentage in % unit.
	 * @param end
	 *            End range percentage in % unit.
	 * @return
	 */
	public static double randomPercent(int start, int end) {
		return ((random.nextDouble() * (end - start)) + start) * 0.01;
	}

}
