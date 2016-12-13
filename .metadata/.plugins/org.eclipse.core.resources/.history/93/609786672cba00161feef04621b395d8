package graphic;

import java.util.ArrayList;

public class AnimationHolder {
	
	private static ArrayList<Animation> playingAnimations = new ArrayList<>();
	
	public static ArrayList<Animation> getPlayingAnimations() {
		return playingAnimations;
	}
	
	public static void addPlayingAnimations(Animation animation) {
		if (! playingAnimations.contains(animation)) {
			playingAnimations.add(animation);
		}
	}
	
	public static void removePlayingAnimations(Animation animation) {
		if (! playingAnimations.contains(animation)) {
			playingAnimations.remove(animation);
		}
	}

}
