package item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bag {
	private List<Item> items = new ArrayList<Item>();
	
	public void addAll(Item... items){
		for(Item i : items){
			this.items.add(i);
		}
	}
	
	public void remove(Item item){
		items.remove(item);
	}

	public void sort() {
		items.sort((i1, i2) -> i1.getName().compareToIgnoreCase(i2.getName()));
	}
}
