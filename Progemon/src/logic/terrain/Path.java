package logic.terrain;

import java.util.Collection;
import java.util.LinkedList;

public class Path extends LinkedList<FightTerrain> implements Comparable<Path> {

	private static final long serialVersionUID = 2418393279076119053L;

	public Path(){
		super();
	}
	public Path(FightTerrain initialFT) {
		super();
		super.add(initialFT);
	}
	
	public Path(Collection<FightTerrain> c){
		super(c);
	}

	public Path(FightTerrain nextFT, Collection<FightTerrain> c) {
		super(c);
		super.add(nextFT);
	}

	/**
	 * Returns a view of the portion of this list between the specified
	 * <code>fromIndex</code>, inclusive, and <code>toIndex</code>, exclusive.
	 */
	public Path subPath(int fromIndex, int toIndex) {
		return new Path(super.subList(fromIndex, toIndex));
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
