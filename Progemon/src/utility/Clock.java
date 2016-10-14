package utility;

import java.awt.event.KeyEvent;

public class Clock {

	/** Ticks per second */
	private static final double DEFAULT_TPS = 60;
	private static double tps = DEFAULT_TPS;
	/** Previous time tick (last round) */
	private static long time;
	private static long periodTime = (long) ((1000 / tps) * 1000 * 1000);

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
			if (kEvent.getKeyChar() == ' ') {
				tps *= 4;
			}
		}

		for (KeyEvent kEvent : InputUtility.getTypeKeys()) {
			if (kEvent.getKeyChar() == 'p') {
				pause();
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
		boolean isPause = true;
		while (isPause) {
			for (KeyEvent kEvent : InputUtility.getTypeKeys()) {
				if (kEvent.getKeyChar() == 'p') {
					isPause = false;
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
