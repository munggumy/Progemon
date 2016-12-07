package logic.player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import logic.character.ActiveSkill;
import logic.character.Pokemon;
import logic.terrain.FightTerrain;
import manager.GUIFightGameManager;
import utility.InputUtility;

public class HumanPlayer extends Player {

	Set<KeyCode> attackingKeys = new HashSet<KeyCode>(
			Arrays.asList(KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3, KeyCode.DIGIT4));

	public HumanPlayer(String name) {
		super(name);
	}

	public HumanPlayer(String name, Pokemon starter_pokemon, Color blue) {
		super(name, starter_pokemon, blue);
	}

	public HumanPlayer(String name, Color color) {
		super(name, color);
	}

	@Override
	protected boolean inputNextPath(Pokemon pokemon) {
		if (!InputUtility.isMouseLeftClicked()) {
			return false;
		} else {
			FightTerrain destination = pokemon.getCurrentFightMap().getFightTerrainAt(
					(InputUtility.getMouseX()) / FightTerrain.IMG_SIZE_X,
					(InputUtility.getMouseY()) / FightTerrain.IMG_SIZE_Y);
			System.out.println("Destination : " + destination);
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

		if (super.nextAttackedPokemon.isPresent()) {
			return true;
		}
		if (!InputUtility.isMouseLeftClicked()) {
			return false;
		} else {
			FightTerrain destination = pokemon.getCurrentFightMap().getFightTerrainAt(
					InputUtility.getMouseX() / FightTerrain.IMG_SIZE_X,
					InputUtility.getMouseY() / FightTerrain.IMG_SIZE_Y);
			Optional<Pokemon> otherPokemon = pokemon.getCurrentFightMap().getPokemonAt(destination);
			if (otherPokemon.isPresent() && otherPokemon.get().getOwner() != pokemon.getOwner()) {
				super.nextAttackedPokemon = otherPokemon;
				return true;
			} else {
				super.nextAttackedPokemon = Optional.empty();
				return false;
			}
		}
	}

	@Override
	protected boolean inputAttackActiveSkill(Pokemon attackingPokemon) {
		KeyCode endTurn = KeyCode.E;
		if (InputUtility.getKeyTriggered(endTurn)) {
			GUIFightGameManager.nextPhase();
		}
		if (super.nextAttackSkill.isPresent()) {
			return true;
		}
		List<ActiveSkill> attackSkills = attackingPokemon.getActiveSkills();
		for (KeyCode kc : attackingKeys) {
			int index = kc.ordinal() - KeyCode.DIGIT1.ordinal();
			if (InputUtility.getKeyTriggered(kc) && index >= 0 && index < attackSkills.size()) {
				System.out.println("Attack Skill setted");
				super.nextAttackSkill = Optional.of(attackSkills.get(index));
				return true;
			}
		}
		return false;
	}
}
