package logic.player;

import graphic.Frame;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import logic.character.Pokemon;
import logic.terrain.FightTerrain;
import utility.InputUtility;

public class HumanPlayer extends Player {

	public HumanPlayer(String name) {
		super(name);
	}

	public HumanPlayer(String name, Pokemon starter_pokemon, javafx.scene.paint.Color blue) {
		super(name, starter_pokemon, blue);
	}

	public HumanPlayer(String name, Color color) {
		super(name, color);
	}

	@Override
	protected boolean inputNextPath(Pokemon pokemon) {
		MouseEvent mEvent = InputUtility.getLastMouseClickEvent();
		if (mEvent == null) {
			super.nextPath = null;
			return false;
		} else {
			FightTerrain destination = pokemon.getCurrentFightMap().getFightTerrainAt(
					(mEvent.getX() - Frame.OFFSET_X) / FightTerrain.IMG_SIZE_X,
					(mEvent.getY() - Frame.OFFSET_Y) / FightTerrain.IMG_SIZE_Y);
			System.out.println(destination);
			if (pokemon.getAvaliableFightTerrains().contains(destination)) {
				super.nextPath = pokemon.findPathTo(destination);
				return true;
			} else {
				super.nextPath = null;
				return false;
			}
		}
	}

	@Override
	protected boolean inputAttackPokemon(Pokemon pokemon) {
		if (super.nextAttackedPokemon != null) {
			return true;
		}
		MouseEvent mEvent = InputUtility.getLastMouseClickEvent();
		if (mEvent == null) {
			super.nextAttackedPokemon = null;
			return false;
		} else {
			@Deprecated
			FightTerrain destination = pokemon.getCurrentFightMap().getFightTerrainAt(
					(mEvent.getX()) / FightTerrain.IMG_SIZE_X, (mEvent.getY()) / FightTerrain.IMG_SIZE_Y);
			Pokemon otherPokemon = pokemon.getCurrentFightMap().getPokemonAt(destination);
			if (otherPokemon != null && otherPokemon.getOwner() != pokemon.getOwner()) {
				super.nextAttackedPokemon = otherPokemon;
				return true;
			} else {
				super.nextAttackedPokemon = null;
				return false;
			}
		}
	}

	@Override
	protected boolean inputAttackActiveSkill(Pokemon attackingPokemon) {

		System.out.println(super.nextAttackSkill);
		if (super.nextAttackSkill != null) {
			return true;
		}
		KeyEvent kEvent = InputUtility.getLastKeyEvent();
		if (kEvent.getID() != KeyEvent.KEY_RELEASED) {
			return false;
		}
		int keyCode = kEvent.getKeyCode();
		System.out.println("KeyCode = " + keyCode);
		switch (keyCode) {
		case KeyEvent.VK_1:
		case KeyEvent.VK_2:
		case KeyEvent.VK_3:
		case KeyEvent.VK_4:
			if (keyCode - KeyEvent.VK_0 > attackingPokemon.getActiveSkills().size()) {
				System.err.println("keyCode " + (keyCode - KeyEvent.VK_0) + " is invalid.");
				super.nextAttackSkill = null;
			} else {
				super.nextAttackSkill = attackingPokemon.getActiveSkills().get(keyCode - KeyEvent.VK_0 - 1);
				System.out.println("Attacking with skill " + (keyCode - KeyEvent.VK_0));
			}

			break;
		default:
			InputUtility.setLastKeyEvent(kEvent); // pass event back
		}
		return super.nextAttackSkill != null;
	}
}
