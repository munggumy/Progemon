package graphic;

public abstract class PseudoAnimation<T> {
	
	protected int amountOfFrame, frameDelay, currentFrame, delayCounter;
	protected boolean playing = false;
	protected T target;
	
	public PseudoAnimation(int amountOfFrame, int frameDelay) {
		this.amountOfFrame = amountOfFrame;
		this.frameDelay = frameDelay;
		currentFrame = 0;
		delayCounter = 0;
	}
	
	public void play() {
		PseudoAnimationHolder.addPlayingPseudoAnimations(this);
		currentFrame = 0;
		delayCounter = 0;
		playing = true;
	}
	
	public abstract void update();
	
	public void stop() {
		PseudoAnimationHolder.removePlayingPseudoAnimations(this);
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
