package logic_fight.character.pokemon;

import javafx.scene.image.Image;
import logic_fight.character.Element;
import logic_fight.character.pokemon.Pokemon.MoveType;

/** Abstract class for Pokemon and PokemonTemplate */
public abstract class AbstractPokemon {

	protected MoveType moveType;
	protected Element primaryElement, secondaryElement;
	protected int moveRange, attackRange, id;
	protected LevelingRate levelingRate;
	protected int expYield;
	protected int catchRate;

	protected String imageFileName;
	protected Image image;

	public final MoveType getMoveType() {
		return moveType;
	}

	public final Element getPrimaryElement() {
		return primaryElement;
	}

	public final Element getSecondaryElement() {
		return secondaryElement;
	}

	public final int getMoveRange() {
		return moveRange;
	}

	public final int getAttackRange() {
		return attackRange;
	}

	public final int getId() {
		return id;
	}

	public final LevelingRate getLevelingRate() {
		return levelingRate;
	}

	public final int getExpYield() {
		return expYield;
	}

	public final void setMoveRange(int moveRange) {
		this.moveRange = moveRange;
	}

	public final void setAttackRange(int attackRange) {
		this.attackRange = attackRange;
	}

	public final String getImageFileName() {
		return imageFileName;
	}

	public final Image getImage() {
		return image;
	}

	public final void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public final void setImage(Image image) {
		this.image = image;
	}

	public final int getCatchRate() {
		return catchRate;
	}
	
	

}
