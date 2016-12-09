package item;

public class Item {
	private String name;
	private ItemAction onPokemonUse;
	private ItemAction onPokemonHold;
	private ItemAction onTrainerUse;
	
	public Item(String name){
		if (name == null || name.isEmpty()){
			throw new IllegalArgumentException("Item() : Invalid Item Name");
		}
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}
	public final ItemAction getOnPokemonUse() {
		return onPokemonUse;
	}
	public final ItemAction getOnPokemonHold() {
		return onPokemonHold;
	}
	public final ItemAction getOnTrainerUse() {
		return onTrainerUse;
	}
	public final void setName(String name) {
		if (name == null || name.isEmpty()){
			throw new IllegalArgumentException("Item() : Invalid Item Name");
		}
		this.name = name;
	}
	public final void setOnPokemonUse(ItemAction onPokemonUse) {
		this.onPokemonUse = onPokemonUse;
	}
	public final void setOnPokemonHold(ItemAction onPokemonHold) {
		this.onPokemonHold = onPokemonHold;
	}
	public final void setOnTrainerUse(ItemAction onTrainerUse) {
		this.onTrainerUse = onTrainerUse;
	}
	
}
