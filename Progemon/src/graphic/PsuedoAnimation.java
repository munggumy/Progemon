package graphic;

public abstract class PsuedoAnimation<T> {
	
	protected int amountOfFrame, frameDelay, currentFrame, delayCounter;
	protected boolean playing = false;
	protected T target;
	
	public PsuedoAnimation(int amountOfFrame, int frameDelay) {
		// TODO Auto-generated constructor stub
		this.amountOfFrame = amountOfFrame;
		this.frameDelay = frameDelay;
		currentFrame = 0;
		delayCounter = 0;
	}
	
	public void play() {
		PsuedoAnimationHolder.addPlayingPsuedoAnimations(this);
		currentFrame = 0;
		delayCounter = 0;
		playing = true;
	}
	
	public abstract void update();
	
	public void stop() {
		PsuedoAnimationHolder.removePlayingPsuedoAnimations(this);
		currentFrame = 0;
		delayCounter = 0;
		playing = false;
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public void setTarget(T target) {
		this.target = target;
	}
	
}
