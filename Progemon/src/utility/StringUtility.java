package utility;

public class StringUtility {

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
}
