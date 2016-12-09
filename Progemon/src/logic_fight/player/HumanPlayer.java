package logic_fight.player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import logic_fight.FightPhase;
import logic_fight.character.activeSkill.ActiveSkill;
import logic_fight.character.pokemon.Pokemon;
import logic_fight.terrain.FightTerrain;
import manager.GUIFightGameManager;
import utility.InputUtility;
import utility.exception.AbnormalPhaseOrderException;

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
	protected boolean inputNextPath(Pokemon pokemon) throws AbnormalPhaseOrderException {
		KeyCode captureKey = KeyCode.C;
		if (InputUtility.getKeyTriggered(captureKey) && getCurrentFightManager().canCapturePokemon()) {
			throw new AbnormalPhaseOrderException(FightPhase.preCapturePhase);
		}
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
	protected boolean inputAttackActiveSkill(Pokemon attackingPokemon) throws AbnormalPhaseOrderException {
		KeyCode endTurn = KeyCode.E;
		if (InputUtility.getKeyTriggered(endTurn)) {
			throw new AbnormalPhaseOrderException(FightPhase.postAttackPhase);
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

	@Override
	protected boolean inputCapturePokemon(Pokemon pokemon) throws AbnormalPhaseOrderException {
		KeyCode goBackKey = KeyCode.C;
		if (InputUtility.getKeyTriggered(goBackKey)) {
			throw new AbnormalPhaseOrderException(FightPhase.preMovePhase);
		}
		if (!InputUtility.isMouseLeftClicked()) {
			return false;
		}
		FightTerrain destination = pokemon.getCurrentFightMap().getFightTerrainAt(
				InputUtility.getMouseX() / FightTerrain.IMG_SIZE_X, InputUtility.getMouseY() / FightTerrain.IMG_SIZE_Y);
		Optional<Pokemon> otherPokemon = pokemon.getCurrentFightMap().getPokemonAt(destination);
		otherPokemon.ifPresent(other -> {
			if(!other.getOwner().equals(pokemon.getOwner())){
				captureTarget = other;
			}
		});
		return captureTarget != null;
	}

}
