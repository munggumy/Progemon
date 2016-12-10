package logic_fight.character.pokemon;

import javafx.scene.image.Image;
import logic_fight.character.Element;
import logic_fight.character.pokemon.Pokemon.MoveType;

/** Stored in Pokedex. Use to create pokemon. Make sure to set all fields */
public class PokemonTemplate extends AbstractPokemon {

	private int baseAttack, baseDefense, baseSpeed, baseHP;

	public PokemonTemplate() {

	}

	public final int getBaseAttack() {
		return baseAttack;
	}

	public final int getBaseDefense() {
		return baseDefense;
	}

	public final int getBaseSpeed() {
		return baseSpeed;
	}

	public final int getBaseHP() {
		return baseHP;
	}

	public final void setBaseAttack(int baseAttack) {
		this.baseAttack = baseAttack;
	}

	public final void setBaseDefense(int baseDefense) {
		this.baseDefense = baseDefense;
	}

	public final void setBaseSpeed(int baseSpeed) {
		this.baseSpeed = baseSpeed;
	}

	public final void setBaseHP(int baseHP) {
		this.baseHP = baseHP;
	}

	public final void setID(int id) {
		this.id = id;
	}

	public final void setLevelingRate(LevelingRate levelingRate) {
		this.levelingRate = levelingRate;
	}

	public final void setExpYield(int expYield) {
		this.expYield = expYield;
	}

	public final void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}

	public final void setPrimaryElement(Element primaryElement) {
		this.primaryElement = primaryElement;
	}

	public final void setSecondaryElement(Element secondaryElement) {
		this.secondaryElement = secondaryElement;
	}
	
	public final void setCatchRate(int catchRate){
		this.catchRate = catchRate;
	}

}
