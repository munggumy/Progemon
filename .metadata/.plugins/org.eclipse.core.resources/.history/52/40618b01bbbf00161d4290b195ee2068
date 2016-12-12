package graphic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public abstract class Animation implements IRenderable{
	
	/** Image used for animation. Must be consisted of 5 frames per row */
	protected Image animationImage;
	protected List<Image> animationImages = new ArrayList<>();
	protected int amountOfFrame, frameDelay, currentFrame, delayCounter;
	protected boolean visible = true, playing = false, pause = false, loop = false, autostop = true, playback = false;
	
	public Animation() {
		frameDelay = 1;
	}
	
	public Animation(Image image, int frameNumber) {
		this.animationImage = image;
		this.amountOfFrame = frameNumber;
		frameDelay = 1;
	}
	
	public Animation(int frameNumber, int frameDelay, boolean loop, boolean autostop) {
		this.amountOfFrame = frameNumber;
		this.frameDelay = frameDelay;
		this.loop = loop;
		this.autostop = autostop;
	}
	
	public void play() {
		pause = false;
		delayCounter = 0;
		currentFrame = 0;
		AnimationHolder.addPlayingAnimations(this);
		playing = true;
		if (playback) {
			currentFrame = amountOfFrame - 1;
		}
	}
	
	public void pause() {
		if (!pause) {
			playing = false;
			AnimationHolder.removePlayingAnimations(this);
			pause = true;
		}
	}
	
	public void unpause() {
		if (pause) {
			AnimationHolder.removePlayingAnimations(this);
			playing = true;
			pause = false;
		}
	}
	
	public void update() {
		if (playback) {
			updatePlayback();
			return;
		}
		if(!playing) {
			return;
		}
		if(frameDelay > delayCounter) {
			delayCounter++;
		}
		else if(amountOfFrame - 1 > currentFrame) {
			currentFrame++;
			delayCounter = 0;
		}
		else if(loop) {
			currentFrame = 0;
			delayCounter = 0;
		}
		else if(autostop) {
			stop();
		}
		else{
			pause();
		}
	}
	
	public void updatePlayback() {
		if(!playing) {
			return;
		}
		if(frameDelay > delayCounter) {
			delayCounter++;
		}
		else if(0 < currentFrame) {
			currentFrame--;
			delayCounter = 0;
		}
		else if(loop) {
			currentFrame = amountOfFrame - 1;
			delayCounter = 0;
		}
		else if(autostop) {
			stop();
		}
		else{
			pause();
		}
	}
	
	public void stop() {
		playing = false;
		delayCounter = 0;
		currentFrame = 0;
		AnimationHolder.removePlayingAnimations(this);
	}
	
	public Image getCurrentImage() {
		return animationImages.get(currentFrame);
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	
	public boolean isPause() {
		return pause;
	}
	
	public boolean isLoop() {
		return loop;
	}
	
	public void setLoop(boolean loop) {
		this.loop = loop;
	}
	
	public void setAutostop(boolean autostop) {
		this.autostop = autostop;
	}
	
	public void setPlayback(boolean playback) {
		this.playback = playback;
	}
	
	public void loadAnimationImage(String filePath) {
		File file = new File(filePath);
		animationImage = new Image(file.toURI().toString());
		int width;
		if (amountOfFrame < 5) {
			width = (int) (animationImage.getWidth() / amountOfFrame);
		}
		else{
			width = (int) (animationImage.getWidth() / 5);
		}
		int height = (int) Math.floor(animationImage.getHeight() / amountOfFrame);
		for (int i = 0; i < amountOfFrame; i++) {
			animationImages.add(new WritableImage(animationImage.getPixelReader(), i % 5 * width, (int) (Math.floor(i / 5) * height), width, height));
		}
		/*if(name.equals("Flamethrower")) {
		File file = new File("load/img/skill/" + name + "/all.png");
		Image image = new Image(file.toURI().toString());
		animation = new Animation(image, 16);
		}*/
		/*File file = new File(filePath);
		this.image = new Image(file.toURI().toString());
		frameNumber = 16;*/
	}
	
	public void setFrameDelay(int frameDelay) {
		this.frameDelay = frameDelay;
	}
	
	public void setFrameNumber(int frameNumber) {
		this.amountOfFrame = frameNumber;
	}
	
	@Override
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	@Override
	public void hide() {
		visible = false;
		IRenderableHolder.removeWorldObject(this);
	}
	
	@Override
	public void show() {
		IRenderableHolder.addWorldObject(this);
		visible = true;
	}

}
