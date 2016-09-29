package logic.terrain;

import java.util.Iterator;

/** Terrain -- Terrain -- Terrain -- null */
public class PathNode implements Iterable<PathNode> {
	private PathNode previousNodes = null;
	private FightTerrain thisNode;

	public PathNode(FightTerrain thisNode, PathNode previousNodes) {
		this.previousNodes = previousNodes;
		this.thisNode = thisNode;
	}

	public PathNode(FightTerrain newNode) {
		this(newNode, null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((previousNodes == null) ? 0 : previousNodes.hashCode());
		result = prime * result + ((thisNode == null) ? 0 : thisNode.hashCode());
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
		if (previousNodes == null) {
			if (other.previousNodes != null)
				return false;
		} else if (!previousNodes.equals(other.previousNodes))
			return false;
		if (thisNode == null) {
			if (other.thisNode != null)
				return false;
		} else if (!thisNode.equals(other.thisNode))
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
			return previousNodes != null;
		}

		@Override
		public PathNode next() {
			return previousNodes;
		}

	}

	// Getters and Setters

	public final PathNode getPreviousNodes() {
		return previousNodes;
	}

	public final FightTerrain getThisNode() {
		return thisNode;
	}

	public final void setPreviousNodes(PathNode previousNodes) {
		this.previousNodes = previousNodes;
	}

	public final void setThisNode(FightTerrain thisNode) {
		this.thisNode = thisNode;
	}

}
