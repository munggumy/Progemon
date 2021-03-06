package utility;

import graphic.PseudoAnimation;
import graphic.ScreenEffect;

public class AnimationUtility {

	private static ScreenEffect loadScreen00 = new ScreenEffect(20, 3, false, false);
	private static ScreenEffect loadScreen01 = new ScreenEffect(27, 3, false, false);
	private static PseudoAnimation changePokemonHP;
	private static PseudoAnimation increasePokemonEXP;
	private static PseudoAnimation pokemonGetAttack;

	public AnimationUtility() {
		loadScreen00.loadAnimationImage("load\\img\\animation\\loadscreen00.png");
		loadScreen01.loadAnimationImage("load\\img\\animation\\loadscreen01.png");
		System.out.println("AnimationUtility finished loading");
	}

	public static ScreenEffect getLoadScreen00() {
		return loadScreen00;
	}

	public static ScreenEffect getLoadScreen01() {
		return loadScreen01;
	}

}
