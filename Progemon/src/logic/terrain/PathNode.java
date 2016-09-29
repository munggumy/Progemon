package logic.terrain;

import java.util.Iterator;


/** Terrain -- Terrain -- Terrain -- null */
public class PathNode implements Iterable<PathNode> {
	private PathNode previousNodes = null;
	private FightTerrain thisNode;
	
	public PathNode(FightTerrain thisNode, PathNode previousNodes){
		this.previousNodes = previousNodes;
		this.thisNode = thisNode;
	}
	
	public PathNode(FightTerrain newNode){
		this(newNode, null);
	}
	
	@Override
	public Iterator<PathNode> iterator() {
		return new pathNodeIterator();
	}
	
	private class pathNodeIterator implements Iterator<PathNode>{

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
