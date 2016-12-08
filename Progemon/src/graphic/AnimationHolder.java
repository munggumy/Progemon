package graphic;

import java.util.concurrent.CopyOnWriteArrayList;

public class AnimationHolder {
	
	private static CopyOnWriteArrayList<Animation> playingAnimations = new CopyOnWriteArrayList<>();
	
	public static CopyOnWriteArrayList<Animation> getPlayingAnimations() {
		return playingAnimations;
	}
	
	public static void addPlayingAnimations(Animation animation) {
		if (!playingAnimations.contains(animation)) {
			playingAnimations.add(animation);
		}
	}
	
	public static void removePlayingAnimations(Animation animation) {
		if (!playingAnimations.contains(animation)) {
			playingAnimations.remove(animation);
		}
	}

}
