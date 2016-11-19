package utility;

import graphic.Animation;
import graphic.AnimationHolder;
import javafx.scene.input.KeyEvent;

public class Clock {

	/** Ticks per second */
	private static final double DEFAULT_TPS = 60;
	private static double tps = DEFAULT_TPS;
	/** Previous time tick (last round) */
	private static long time;
	private static long periodTime = (long) ((1000 / tps) * 1000 * 1000);
	private static boolean pause = false;

	public Clock() {
		// TODO Auto-generated constructor stub
		time = System.nanoTime();
	}
	
	public static void tick(){
		tick(1);
	}

	public static void tick(int n) {

		tps = DEFAULT_TPS;

		for (KeyEvent kEvent : InputUtility.getHoldingKeys()) {
			if (kEvent.getText().equals(" ")) {
				tps *= 4;
			}
		}

		for (KeyEvent kEvent : InputUtility.getTypeKeys()) {
			if (kEvent.getText().equals("p")) {
				pause();
			}
		}
		
		if (! pause) {
			for (Animation animation : AnimationHolder.getPlayingAnimations()) {
				animation.update();
			}
		}

		InputUtility.clear();

		periodTime = (long) ((1000 / tps) * 1000 * 1000 * n);
		long currentTime = System.nanoTime();
		if (currentTime - time < periodTime) {
			int waitingTime = (int) (periodTime - currentTime + time);

			// time check
			/*
			 * System.out.println("currentTime" + currentTime);
			 * System.out.println("Time" + time);
			 * System.out.println("periodTime" + periodTime);
			 * System.out.println("waitingTime" + waitingTime);
			 */

			try {
				Thread.sleep((long) (Math.floor(waitingTime / (1000 * 1000))), waitingTime % (1000 * 1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		time += periodTime;
	}

	private static void pause() {
		pause = true;
		while (pause) {
			for (KeyEvent kEvent : InputUtility.getTypeKeys()) {
				if (kEvent.getText().equals("p")) {
					pause = false;
					break;
				}
			}

			Clock.tick();
		}
	}

	public static double getTps() {
		return tps;
	}

	public static void setTps(double tps) {
		Clock.tps = tps;
	}

	public static long getTime() {
		return time;
	}

}
