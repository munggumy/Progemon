package logic.terrain;

import java.util.Collection;
import java.util.LinkedList;

public class Path extends LinkedList<FightTerrain> implements Comparable<Path> {

	private static final long serialVersionUID = 2418393279076119053L;

	public Path(FightTerrain paramFT) {
		super();
		super.add(paramFT);
	}

	public Path(FightTerrain paramFT, Collection<FightTerrain> c) {
		super(c);
		super.add(paramFT);
	}

	@Override
	public boolean containsAll(Collection<?> paramCollection) {
		return super.containsAll(paramCollection);
	}

	@Override
	public int compareTo(Path paramPath) {
		if (paramPath == null) {
			return 1;
		}
		return this.size() - paramPath.size();
	}
	
}
