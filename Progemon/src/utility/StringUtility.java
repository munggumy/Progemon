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
	
	public static String hpBar(double percent, int length){
		StringBuilder out = new StringBuilder("[");
		int shaded = (int) (percent * ((double) length));
		for(short i = 0; i < shaded; i++){
			out.append("=");
		}
		int unshaded = length - shaded;
		for(short i = 0; i < unshaded; i++){
			out.append(" ");
		}
		out.append("]");
		return out.toString();
	}
	
	public static String hpBar(double percent){
		return hpBar(percent, DEFAULT_HP_BAR_LENGTH);
	}
}
