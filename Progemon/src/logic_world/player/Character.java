package logic_world.player;

import java.io.File;

import audio.SFXUtility;
import graphic.Animation;
import graphic.DrawingUtility;
import graphic.PseudoAnimation;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import logic_world.terrain.WorldDirection;
import logic_world.terrain.WorldMapException;
import logic_world.terrain.WorldObject;
import manager.WorldManager;

public abstract class Character extends Animation {
	
	private static final int FAST_DELAY = 2, MEDIAM_DELAY = 5, SLOW_DELAY = 8, VERYSLOW_DELAY = 11;
	
	protected double x, y;
	protected int blockX, blockY;
	protected WorldDirection direction;
	protected double yOffset = 0;
	protected int frameLimit = 2;
	protected int legState = 0;
	protected boolean moving = false, walking = false, turning = false, stucking = false, jumping = false;
	protected WorldObject jumpAnimation;
	protected PseudoAnimation<Character> jump = new PseudoAnimation<Character>(12, 1) {

		@Override
		public void update() {
			if (delayCounter == frameDelay) {
				currentFrame++;
				yOffset = -(6.0 - Math.abs((6.0 - currentFrame))) * 32 / 6.0;
				if (currentFrame == amountOfFrame) {
					jumping = false;
					jumpAnimation.setBlockX(blockX);
					jumpAnimation.setBlockY(blockY);
					jumpAnimation.show();
					jumpAnimation.play();
					System.out.println("reach");
					stop();
				}
				delayCounter = 0;
			} else {
				delayCounter++;
			}
		}
	};
	
	public Character(String imagepath, int frameNumber, int frameDelay) {
		super(DrawingUtility.resize(new Image(new File(imagepath).toURI().toString()), 2), frameNumber, frameDelay);

		jumpAnimation = WorldObject.createWorldObject("100", 0, 0, null, null);
		jumpAnimation.setHideOnStop(true);
		direction = WorldDirection.SOUTH;
		show();
	}
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub
		DrawingUtility.drawCharacter(this);
	}
	
	@Override
	public Image getCurrentImage() {
		WritableImage wimg = new WritableImage(animationImage.getPixelReader(), (legState * 2 + currentFrame) * 32,
				direction.ordinal() * 44, 32, 44);
		return wimg;
	}
	
	@Override
	public void play() {
		// TODO Auto-generated method stub
		frameDelay = FAST_DELAY;
		frameLimit = 2;
		walking = false;
		stucking = false;
		super.play();
	}
	
	public void walk() {
		int x = blockX + (direction.ordinal() - 2) * (direction.ordinal() % 2);
		int y = blockY + (direction.ordinal() - 1) * (direction.ordinal() % 2 - 1);
		System.out.println("Character walk --> walk to : " + x + " , " + y);
		play();
		walking = true;
		moving = true;
		WorldManager.getWorldMap().getObjectAt(x, y).entered();
		if (walking) {
			try {
				WorldManager.getWorldMap().setTerrainAt(blockX, blockY, -WorldManager.getWorldMap().getTerrainAt(blockX, blockY));
			} catch (WorldMapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WorldManager.getWorldMap().getObjectAt(blockX, blockY).exit();
		}
	}

	public void turn(WorldDirection newDirection) {
		play();
		turning = true;
		direction = newDirection;
	}

	public void stuck() {
		play();
		stucking = true;
		moving = true;
	}

	public void jump() {
		SFXUtility.playSound("ledge_jump");
		jumping = true;
		jump.play();
	}
	
	@Override
	public void update() {
		if (!playing) {
			return;
		}
		if (walking) {
			walkUpdate();
		} else if (turning) {
			turnUpdate();
		} else if (stucking) {
			stuckUpdate();
		}
	}
	
	public void walkUpdate() {
		switch (direction) {
		case SOUTH:
			y += 32f / (VERYSLOW_DELAY + 1);
			break;
		case WEST:
			x -= 32f / (VERYSLOW_DELAY + 1);
			break;
		case NORTH:
			y -= 32f / (VERYSLOW_DELAY + 1);
			break;
		case EAST:
			x += 32f / (VERYSLOW_DELAY + 1);
			break;
		}
		if (frameDelay > delayCounter) {
			delayCounter++;
			return;
		} else if (frameLimit != 0) {
			currentFrame++;
			frameLimit--;
			currentFrame %= amountOfFrame;
			if (frameDelay == FAST_DELAY) {
				frameDelay = MEDIAM_DELAY;
			} else {
				frameDelay = FAST_DELAY;
			}
		} else {
			frameLimit = 2;
			legState++;
			legState %= 2;
			try {
				WorldManager.getWorldMap().setTerrainAt(blockX, blockY, -WorldManager.getWorldMap().getTerrainAt(blockX, blockY));
			} catch (WorldMapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (direction) {
			case SOUTH:
				blockY += 1;
				break;
			case WEST:
				blockX -= 1;
				break;
			case NORTH:
				blockY -= 1;
				break;
			case EAST:
				blockX += 1;
				break;
			}
			x = blockX * 32;
			y = blockY * 32;
			walking = false;
			pause();
			WorldManager.getWorldMap().getObjectAt(blockX, blockY).step();
		}
		delayCounter = 0;
	}
	
	public void stuckUpdate() {
		if (frameDelay > delayCounter) {
			delayCounter++;
			return;
		} else if (frameLimit != 0) {
			currentFrame++;
			currentFrame %= amountOfFrame;
			frameLimit--;
			if (frameDelay == FAST_DELAY) {
				frameDelay = VERYSLOW_DELAY;
			} else if (frameDelay == VERYSLOW_DELAY) {
				frameDelay = SLOW_DELAY;
			}
		} else {
			frameLimit = 2;
			legState++;
			legState %= 2;
			stucking = false;
			pause();
		}
		delayCounter = 0;
	}

	public void turnUpdate() {
		if (FAST_DELAY > delayCounter) {
			delayCounter++;
			return;
		} else if (frameLimit != 0) {
			currentFrame++;
			currentFrame %= amountOfFrame;
			frameLimit--;
		} else {
			frameLimit = 2;
			legState++;
			legState %= 2;
			turning = false;
			pause();
			WorldManager.getWorldMap().getObjectAt(blockX, blockY).step();
		}
		delayCounter = 0;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getBlockX() {
		return blockX;
	}

	public int getBlockY() {
		return blockY;
	}

	public void setBlockX(int blockX) {
		this.blockX = blockX;
	}

	public void setBlockY(int blockY) {
		this.blockY = blockY;
	}

	public WorldDirection getDirection() {
		return direction;
	}

	public void setDirection(WorldDirection direction) {
		this.direction = direction;
	}
	
	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public boolean isWalking() {
		return walking;
	}

	public boolean isStucking() {
		return stucking;
	}
	
	public boolean isJumping() {
		return jumping;
	}

	public double getyOffset() {
		return yOffset;
	}

	@Override
	public int getDepth() {
		return (int) y;
	}
	
	public abstract void checkMove();

}
