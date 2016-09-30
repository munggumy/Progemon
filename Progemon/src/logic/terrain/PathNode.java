package logic.terrain;

import java.util.Iterator;

/** Terrain -- Terrain -- Terrain -- null */
public class PathNode implements Iterable<PathNode> {
	private PathNode previousNode = null;
	private FightTerrain fightTerrain;

	public PathNode(FightTerrain terrain, PathNode previousNode) {
		this.previousNode = previousNode;
		this.fightTerrain = terrain;
	}

	public PathNode(FightTerrain newNode) {
		this(newNode, null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((previousNode == null) ? 0 : previousNode.hashCode());
		result = prime * result + ((fightTerrain == null) ? 0 : fightTerrain.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathNode other = (PathNode) obj;
		if (previousNode == null) {
			if (other.previousNode != null)
				return false;
		} else if (!previousNode.equals(other.previousNode))
			return false;
		if (fightTerrain == null) {
			if (other.fightTerrain != null)
				return false;
		} else if (!fightTerrain.equals(other.fightTerrain))
			return false;
		return true;
	}

	@Override
	public Iterator<PathNode> iterator() {
		return new pathNodeIterator();
	}

	private class pathNodeIterator implements Iterator<PathNode> {

		@Override
		public boolean hasNext() {
			return previousNode != null;
		}

		@Override
		public PathNode next() {
			return previousNode;
		}

	}

	// Getters and Setters

	public final PathNode getPreviousNodes() {
		return previousNode;
	}

	public final FightTerrain getThisNode() {
		return fightTerrain;
	}

	public final void setPreviousNodes(PathNode previousNodes) {
		this.previousNode = previousNodes;
	}

	public final void setThisNode(FightTerrain thisNode) {
		this.fightTerrain = thisNode;
	}

}
