package item;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Bag {
	private Map<Item, Integer> items = new LinkedHashMap<Item, Integer>();

	public void addAll(Item... items) {
		for (Item i : items) {
			if (this.items.containsKey(i)) {
				this.items.put(i, this.items.get(i) + 1);
			} else {
				this.items.put(i, 1);
			}
		}
	}

	public Item getAndRemove(Item item) {
		if (items.containsKey(item) && items.get(item) > 0) {
			items.put(item, items.get(item) - 1);
			return item;
		} else {
			throw new IllegalArgumentException("Cannot find item=" + item.getName() + "in bag.");
		}
	}

	public void sortByAlphabet() {
		Map<Item, Integer> temp = new HashMap<Item, Integer>(items);
		items.clear();

		temp.entrySet().stream().sorted(Map.Entry.<Item, Integer>comparingByKey((i1, i2) -> {
			return i1.getName().compareTo(i2.getName());
		})).forEachOrdered(entry -> items.put(entry.getKey(), entry.getValue()));
	}

	public Map<Item, Integer> getItems() {
		return items;
	}

	public Map<Item, Integer> getPokeballs() {
		return items.entrySet().stream().filter(en -> en.getKey() instanceof Pokeball)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<Item, Integer> getNonPokeballs() {
		return items.entrySet().stream().filter(en -> !(en.getKey() instanceof Pokeball))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}
