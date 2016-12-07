package logic.character;

import java.util.Comparator;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import logic.character.Element.SW;

/** Singleton Class of Strength and Weakness Table. */
public class SWTable extends HashMap<Element, HashMap<Element, SW>> {

	public static SWTable instance = new SWTable();

	private SWTable() {

	}

	public double getFactor(Element attacker, Element attacked) {
		return this.get(attacker).get(attacked).getFactor();
	}

	public SW getSW(Element attacker, Element attacked) {
		return this.get(attacker).get(attacked);
	}

	public void setSW(Element attacker, Element attacked, SW sw) {
		if (this.containsKey(attacker)) {
			this.get(attacker).put(attacked, sw);
		} else {
			HashMap<Element, Element.SW> temp = new HashMap<>();
			temp.put(attacked, sw);
			super.put(attacker, temp);
		}
	}

	@Override
	public HashMap<Element, SW> put(Element key, HashMap<Element, SW> value) {
		throw new UnsupportedOperationException("Unsupported put() in SWTable");
	}

	@Override
	public String toString() {
		final Comparator<? super Element> byElement = (e1, e2) -> e1.ordinal() - e2.ordinal();
		return this.keySet().stream().sorted(byElement).map((Element attacker) -> {
			return attacker.toString() + (attacker.toString().length() == 8 ? "  " : "\t  ")
					+ this.get(attacker).keySet().stream().sorted(byElement)
							.map(attacked -> this.get(attacker).get(attacked).toString())
							.collect(Collectors.joining(" "));
		}).collect(Collectors.joining("\n"));
	}
}
