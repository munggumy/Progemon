package logic_world.player;

import java.io.File;

import audio.SFXUtility;
import graphic.Animation;
import graphic.DrawingUtility;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import logic_fight.character.pokemon.Pokemon;
import logic_fight.player.HumanPlayer;
import logic_world.terrain.WorldDirection;
import manager.WorldManager;
import utility.Pokedex;

public class PlayerCharacter extends Animation {
	
	public static final PlayerCharacter instance = new PlayerCharacter();

	private static final String DEFAULT_IMG_PATH = "load\\img\\player\\Boy.png";
	private static final int FAST_DELAY = 2, MEDIAM_DELAY = 5, SLOW_DELAY = 8, VERYSLOW_DELAY = 11;

	private float x, y;
	private int blockX, blockY;
	private WorldDirection direction;
	private int frameLimit = 2;
	private int legState = 0;
	private boolean moving = false, walking = false, turning = false, stucking = false;

	private HumanPlayer me = new HumanPlayer("Mhee", Color.BROWN);

	public PlayerCharacter() {
		super(DrawingUtility.resize(new Image(new File(DEFAULT_IMG_PATH).toURI().toString()), 2), 2);
		setFrameDelay(3);

		Pokemon charlizard = Pokedex.getPokemon("Charlizard");
		charlizard.setLevel(40);

		Pokemon caterpie = Pokedex.getPokemon("Caterpie");
		caterpie.setLevel(5);

		me.addPokemon(charlizard);
		me.addPokemon(caterpie);
		direction = WorldDirection.DOWN;
	}

	@Override
	public void play() {
		frameDelay = FAST_DELAY;
		frameLimit = 2;
		walking = false;
		stucking = false;
		super.play();
	}

	public void walk() {
		int x = blockX + (direction.ordinal() - 2) * (direction.ordinal() % 2);
		int y = blockY + (direction.ordinal() - 1) * (direction.ordinal() % 2 - 1);
		System.out.println("Player walk --> x : " + x + ", y : " + y);
		play();
		walking = true;
		moving = true;
		WorldManager.getWorldMap().getObjectAt(x, y).entered();
		WorldManager.getWorldMap().getObjectAt(blockX, blockY).exit();
	}

	public void turn(WorldDirection newDirection) {
		play();
		turning = true;
		direction = newDirection;
	}

	public void stuck() {
		SFXUtility.playSound("walk_obstructed");
		play();
		stucking = true;
		moving = true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
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
		case DOWN:
			y += 32f / (VERYSLOW_DELAY + 1);
			break;
		case LEFT:
			x -= 32f / (VERYSLOW_DELAY + 1);
			break;
		case UP:
			y -= 32f / (VERYSLOW_DELAY + 1);
			break;
		case RIGHT:
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
			switch (direction) {
			case DOWN:
				blockY += 1;
				break;
			case LEFT:
				blockX -= 1;
				break;
			case UP:
				blockY -= 1;
				break;
			case RIGHT:
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

	@Override
	public Image getCurrentImage() {
		WritableImage wimg = new WritableImage(animationImage.getPixelReader(), (legState * 2 + currentFrame) * 32,
				direction.ordinal() * 44, 32, 44);
		return wimg;
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

	@Override
	public int getDepth() {
		return (int) y;
	}

	@Override
	public void draw() {
		DrawingUtility.drawPlayer(this);
	}

	public final HumanPlayer getMe() {
		return me;
	}

}
