package utility.exception;

public class InvalidStatStage extends Exception {
	public static final int HIGH = 1, LOW = 0;
	public int type;
	
	public InvalidStatStage(int type){
		this.type = type;
	}
}
