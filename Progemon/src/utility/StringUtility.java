package utility;

public class StringUtility {

	private static final int DEFAULT_HP_BAR_LENGTH = 24;

	public static String toTitleCase(String inputString) {
		StringBuilder sb = new StringBuilder("");
		String out;
		if (inputString.contains(" ")) {
			for (String substring : inputString.split(" ")) {
				sb.append(toTitleCase(substring));
				sb.append(" ");
			}
			out = sb.substring(0, sb.length() - 1);
		} else {
			sb.append(Character.toUpperCase(inputString.charAt(0)));
			sb.append(inputString.substring(1, inputString.length()).toLowerCase());
			out = sb.toString();
		}
		return out;
	}

	/** Percent in range [0,1] */
	public static String hpBar(double percent, int length) {
		StringBuilder out = new StringBuilder("[");
		length = length < 0 ? DEFAULT_HP_BAR_LENGTH : length; // length boundary [> 0]
		percent = percent > 1 ? 1 : (percent < 0 ? 0 : percent); // percent boundary [0,1]
		int shaded = (int) (percent * ((double) length)); // rounds down
		if (shaded > 0) {
			for (short i = 0; i < shaded; i++) {
				out.append("=");
			}
		}
		int unshaded = length - shaded;
		if (unshaded > 0) {
			for (short i = 0; i < unshaded; i++) {
				out.append(" ");
			}
		}
		out.append("]");
		return out.toString();
	}

	public static String hpBar(double percent) {
		return hpBar(percent, DEFAULT_HP_BAR_LENGTH);
	}
}
