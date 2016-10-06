package utility;

public class Clock {
	
	private static double fps = 60;
	private static long time;
	private static int waitingTime = (int) (1 / fps);
	
	public Clock() {
		// TODO Auto-generated constructor stub
		time = System.currentTimeMillis();
	}
	
	public static void tick(){
		long currentTime = System.currentTimeMillis();
		if(currentTime - time < waitingTime){
			try {
				Thread.sleep(waitingTime - currentTime + time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			time = currentTime;
		}
	}
	
	public static void setFps(double fps) {
		Clock.fps = fps;
	}

}
