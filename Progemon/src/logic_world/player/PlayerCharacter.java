package logic_world.player;

import java.io.File;

import audio.SFXUtility;
import graphic.Animation;
import graphic.DialogBox;
import graphic.DrawingUtility;
import graphic.PseudoAnimation;
import item.Bag;
import item.Items;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import logic_fight.character.pokemon.Pokemon;
import logic_fight.player.HumanPlayer;
import logic_world.terrain.WorldDirection;
import logic_world.terrain.WorldMapException;
import logic_world.terrain.WorldObject;
import manager.WorldManager;
import utility.InputUtility;
import utility.Pokedex;

public class PlayerCharacter extends Character {

	public static final PlayerCharacter instance = new PlayerCharacter();

	private static final String DEFAULT_IMG_PATH = "load\\img\\player\\Boy.png";

	private int repelTime = 0;

	private HumanPlayer me = new HumanPlayer("Mhee", Color.BROWN);
	private Bag bag = me.getBag();

	public PlayerCharacter() {
		super(DEFAULT_IMG_PATH, 2, 3);
		bag.addAll(Items.getItem("potion"), Items.getItem("potion"), Items.getItem("potion"), Items.getItem("soda_pop"),
				Items.getItem("rare_candy"), Items.getItem("antidote"), Items.getItem("lemonade"),
				Items.getItem("pokeball"), Items.getItem("pokeball"), Items.getItem("great_ball"));
	}

	@Override
	public void checkMove() {
		// TODO Auto-generated method stub
		try {
			if (InputUtility.getKeyPressed(KeyCode.DOWN)) {
				processMove(WorldDirection.SOUTH);
			} else if (InputUtility.getKeyPressed(KeyCode.LEFT)) {
				processMove(WorldDirection.WEST);
			} else if (InputUtility.getKeyPressed(KeyCode.UP)) {
				processMove(WorldDirection.NORTH);
			} else if (InputUtility.getKeyPressed(KeyCode.RIGHT)) {
				processMove(WorldDirection.EAST);
			} else if (!stucking && !walking) {
				moving = false;
			}
		} catch (WorldMapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processMove(WorldDirection wd) throws WorldMapException {
		if (direction == wd) {
			if (!playing) {
				if (WorldManager.getWorldMap().getTerrainAt(blockX, blockY, wd) <= 0) {
					stuck();
				} else {
					walk();
				}
			}
		} else if (moving && !walking) {
			if (WorldManager.getWorldMap().getTerrainAt(blockX, blockY, wd) <= 0) {
				direction = wd;
				stuck();
			} else {
				direction = wd;
				walk();
			}
		} else if (!playing) {
			turn(wd);
		}

	}

	public final HumanPlayer getMe() {
		return me;
	}

	@Override
	public void walk() {
		super.walk();
		if (repelTime == 1) {
			DialogBox.instance.show();
			DialogBox.instance.sentDialog("Repel Effects Wore out");
			DialogBox.instance.hide();
		}
		repelTime = repelTime > 0 ? repelTime - 1 : 0;
	}

	public void setRepelTime(int repelTime) {
		if (repelTime < 0) {
			throw new IllegalArgumentException("repelTime cannot be negative");
		}
		if (this.repelTime > repelTime) {
			return;
		}
		this.repelTime = repelTime;
	}

	public boolean hasRepel() {
		return repelTime > 0;
	}

	public void addPokemon(Pokemon pokemon) {
		me.addPokemon(pokemon);
	}

	public void addAllPokemons(Pokemon... pokemons) {
		for (Pokemon pokemon : pokemons) {
			me.addPokemon(pokemon);
		}
	}

	public void removePokemon(Pokemon pokemon) {
		me.removePokemon(pokemon);
	}

	public int getNumberOfPokemons() {
		return me.getPokemons().size();
	}

	public void setName(String name) {
		me.setName(name);
	}

	public void setColor(Color color) {
		me.setColor(color);
	}

	public void setBag(Bag bag) {
		me.setBag(bag);
		bag = me.getBag();
	}

	public Bag getBag() {
		return bag;
	}
}
