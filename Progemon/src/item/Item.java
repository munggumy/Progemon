package item;

import javafx.scene.image.Image;
import logic_fight.character.pokemon.Pokemon;
import logic_world.player.PlayerCharacter;

public class Item {

	private String name;
	private Image icon;
	private ItemAction<Pokemon> onPokemonUse;
	private ItemAction<Pokemon> onPokemonHold;
	private ItemAction<PlayerCharacter> onTrainerUse;

	public Item(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Item() : Invalid Item Name");
		}
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	public final ItemAction<Pokemon> getOnPokemonUse() {
		return onPokemonUse;
	}

	public final ItemAction<Pokemon> getOnPokemonHold() {
		return onPokemonHold;
	}

	public final ItemAction<PlayerCharacter> getOnTrainerUse() {
		return onTrainerUse;
	}

	public Image getIcon() {
		return icon;
	}

	public final void setName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Item() : Invalid Item Name");
		}
		this.name = name;
	}

	public final void setOnPokemonUse(ItemAction<Pokemon> onPokemonUse) {
		this.onPokemonUse = onPokemonUse;
	}

	public final void setOnPokemonHold(ItemAction<Pokemon> onPokemonHold) {
		this.onPokemonHold = onPokemonHold;
	}

	public final void setOnTrainerUse(ItemAction<PlayerCharacter> onTrainerUse) {
		this.onTrainerUse = onTrainerUse;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}

}
