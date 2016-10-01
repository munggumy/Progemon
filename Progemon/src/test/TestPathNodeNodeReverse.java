package test;

import static org.junit.Assert.*;

import org.junit.Test;

import logic.terrain.FightTerrain;
import logic.terrain.FightTerrain.TerrainType;
import logic.terrain.PathNode;

public class TestPathNodeNodeReverse {

	/*
	 * 0 1 2 3 0 1 2 3 > 4 3 1 > 2 4
	 */
	FightTerrain ft1 = new FightTerrain(1, 3, TerrainType.GRASS);
	PathNode nd1 = new PathNode(ft1);

	FightTerrain ft2 = new FightTerrain(2, 3, TerrainType.GRASS);
	PathNode nd2 = new PathNode(ft2, nd1);

	FightTerrain ft3 = new FightTerrain(2, 2, TerrainType.GRASS);
	PathNode nd3 = new PathNode(ft3, nd2);

	FightTerrain ft4 = new FightTerrain(3, 2, TerrainType.GRASS);
	PathNode nd4 = new PathNode(ft4, nd3);

	// The correct reverse order
	PathNode rev = new PathNode(ft1, new PathNode(ft2, new PathNode(ft3, new PathNode(ft4))));

	@Test
	public void testCase1element() {
		assertFalse(nd1.equals(null));
		PathNode old = nd1;
		PathNode.reverseUtil(nd3, nd3.getPreviousNodes());
	}
	
	@Test
	public void testCase2element(){
		assertFalse(nd2.equals(null));
		
	}

}
