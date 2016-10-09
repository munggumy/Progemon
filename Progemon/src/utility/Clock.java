package utility;

public class Clock {
	
	private static double tps = 60;
	private static long time;
	private static long periodTime = (long) ((1000 / tps) * 1000 * 1000);
	
	public Clock() {
		// TODO Auto-generated constructor stub
		time = System.nanoTime();
	}
	
	public static void tick(){
		periodTime = (long) ((1000 / tps) * 1000 * 1000);
		long currentTime = System.nanoTime();
		if(currentTime - time < periodTime){
			int waitingTime = (int) (periodTime - currentTime + time);
			
			//time check
			/*System.out.println("currentTime" + currentTime);
			System.out.println("Time" + time);
			System.out.println("periodTime" + periodTime);
			System.out.println("waitingTime" + waitingTime);*/
			
			try {
				Thread.sleep((long) (Math.floor(waitingTime / (1000 * 1000))), waitingTime % (1000 * 1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		time += periodTime;
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
