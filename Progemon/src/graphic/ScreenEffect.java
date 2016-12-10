package graphic;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class ScreenEffect extends Animation {

	public ScreenEffect(int frameNumber, int frameDelay, boolean loop, boolean autostop) {
		super(frameNumber, frameDelay, loop, autostop);
	}

	@Override
	public void draw() {
		DrawingUtility.drawScreenEffect(this);
	}

	@Override
	public int getDepth() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void hide() {
		visible = false;
		IRenderableHolder.removeScreenTransitionObject(this);
	}

	@Override
	public void show() {
		visible = true;
		IRenderableHolder.addScreenTransitionObject(this);
	}

	@Override
	public void loadAnimationImage(String filePath) {
		File file = new File(filePath);
		animationImage = new Image(file.toURI().toString());
		int width;
		if (amountOfFrame < 5) {
			width = (int) (animationImage.getWidth() / amountOfFrame);
		} else {
			width = (int) (animationImage.getWidth() / 5);
		}
		int height = (int) (animationImage.getHeight() / Math.ceil(amountOfFrame / 5.0));
		for (int i = 0; i < amountOfFrame; i++) {
			Image img = new WritableImage(animationImage.getPixelReader(), i % 5 * width,
					(int) (Math.floor(i / 5) * height), width, height);
			animationImages.add(DrawingUtility.resize(img, 2));
		}
	}

}
