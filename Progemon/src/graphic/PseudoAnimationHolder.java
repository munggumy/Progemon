package graphic;

import java.util.concurrent.CopyOnWriteArrayList;

public class PseudoAnimationHolder {

	private static CopyOnWriteArrayList<PseudoAnimation<?>> playingPseudoAnimations = new CopyOnWriteArrayList<>();

	public static CopyOnWriteArrayList<PseudoAnimation<?>> getPlayingPsuedoAnimations() {
		return playingPseudoAnimations;
	}

	public static void addPlayingPseudoAnimations(PseudoAnimation<?> animation) {
		if (!playingPseudoAnimations.contains(animation)) {
			playingPseudoAnimations.add(animation);
		}
	}

	public static void removePlayingPseudoAnimations(PseudoAnimation<?> animation) {
		if (playingPseudoAnimations.contains(animation)) {
			playingPseudoAnimations.remove(animation);
		}
	}

}
