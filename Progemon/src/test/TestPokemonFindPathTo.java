package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import logic.character.Pokemon;
import logic.character.Pokemon.MoveType;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;
import logic.terrain.FightTerrain.TerrainType;
import logic.terrain.Path;
import utility.FileUtility;

public class TestPokemonFindPathTo {

	FightMap fightMap;
	Pokemon fearow, squirtle;

	@Before
	public void setUp() throws Exception {
		fightMap = new FightMap(FileUtility.loadFightMap());
		fearow = new Pokemon(23, 45, 65, 59, 60, 2, 2, MoveType.FLY);
		squirtle = new Pokemon(7, 30, 34, 29, 37, 1, 1, MoveType.SWIM);
		fearow.setCurrentFightMap(fightMap);
		squirtle.setCurrentFightMap(fightMap);
		fearow.move(1, 1);
	}

	@Test
	public void testBasics() {
		assertNotNull(fearow);
		assertNotNull(fightMap);
		assertNotNull(fearow.getCurrentFightTerrain());
		assertNotNull(fearow.getCurrentFightMap());
		assertEquals(1, fearow.getCurrentFightTerrain().getX());
		assertEquals(1, fearow.getCurrentFightTerrain().getY());
		assertEquals(MoveType.FLY, fearow.getMoveType());
	}

	@Test
	public void testMoveOneBlockNormal() {

		// move from (1, 1) to (1, 0) UP

		fearow.move(1, 1);

		FightTerrain destination = fightMap.getFightTerrainAt(1, 0);
		Path out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		// move from (3, 4) to (2, 4) LEFT

		fearow.move(3, 4);

		destination = fightMap.getFightTerrainAt(2, 4);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		// move from (5, 2) to (5, 3) DOWN

		fearow.move(5, 2);
		destination = fightMap.getFightTerrainAt(5, 3);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		// move from (6, 4) to (7, 4) RIGHT

		fearow.move(6, 4);

		destination = fightMap.getFightTerrainAt(7, 4);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

	}

	@Test
	public void moveOneBlockBoundary() {

		// Top Border

		fearow.move(3, 0);

		FightTerrain destination = fightMap.getFightTerrainAt(2, 0);
		Path out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		destination = fightMap.getFightTerrainAt(4, 0);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		destination = fightMap.getFightTerrainAt(3, 1);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		// Left Border

		fearow.move(0, 3);

		destination = fightMap.getFightTerrainAt(0, 4);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		destination = fightMap.getFightTerrainAt(0, 2);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		destination = fightMap.getFightTerrainAt(1, 3);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		// Bottom Border

		fearow.move(3, 5);

		destination = fightMap.getFightTerrainAt(2, 5);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		destination = fightMap.getFightTerrainAt(4, 5);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		destination = fightMap.getFightTerrainAt(3, 4);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		// Right Border

		fearow.move(7, 3);

		destination = fightMap.getFightTerrainAt(7, 2);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		destination = fightMap.getFightTerrainAt(7, 4);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		destination = fightMap.getFightTerrainAt(6, 3);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		// Top-Left Corner

		fearow.move(0, 0);

		destination = fightMap.getFightTerrainAt(0, 1);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		destination = fightMap.getFightTerrainAt(1, 0);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		// Bottom-Left Corner

		fearow.move(0, 5);

		destination = fightMap.getFightTerrainAt(0, 4);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

		destination = fightMap.getFightTerrainAt(1, 5);
		out = fearow.findPathTo(destination, 10);
		assertEquals(2, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(1));

	}

	@Test
	public void moveBlockImpossible() {

		// Surrounded By Blocks
		/*
		 * 0 1 2 3 4 5 6 0 X 1 X O X 2 X X 3
		 */
		squirtle.move(1, 1);
		fightMap.setFightTerrainAt(1, 0, new FightTerrain(1, 0, TerrainType.ROCK));
		fightMap.setFightTerrainAt(2, 1, new FightTerrain(2, 1, TerrainType.ROCK));
		fightMap.setFightTerrainAt(1, 2, new FightTerrain(1, 2, TerrainType.ROCK));
		fightMap.setFightTerrainAt(0, 1, new FightTerrain(0, 1, TerrainType.ROCK));

		// move from (1, 1) to (3, 1)
		FightTerrain destination = fightMap.getFightTerrainAt(3, 1);
		System.out.print("Expected Error will appear --> ");
		Path out = squirtle.findPathTo(destination, 10); // this line will error
		assertNull(out);

		// Surrounded By Blocks and Boundary

		squirtle.move(0, 0);
		destination = fightMap.getFightTerrainAt(1, 1);
		System.out.print("Expected Error will appear --> ");
		out = squirtle.findPathTo(destination, 10);
		assertNull(out);

		// reach limit
		// move (1, 4) -> (4, 4)
		squirtle.move(1, 4);
		destination = fightMap.getFightTerrainAt(4, 4);
		System.out.print("Expected Error will appear --> ");
		out = squirtle.findPathTo(destination, 1);
		assertNull(out);
		System.out.print("Expected Error will appear --> ");
		out = squirtle.findPathTo(destination, 2);
		assertNull(out);
		System.out.print("Expected Error will appear --> ");
		out = squirtle.findPathTo(destination, 3);
		assertNull(out);
		// limit is not fully supported...
		
		
	}

	@Test
	public void moveTwoBlocksNormal() {

		// move from (3, 3) -> (2, 2)

		fearow.move(3, 3);

		FightTerrain destination = fightMap.getFightTerrainAt(2, 2);
		Path out = fearow.findPathTo(destination, 10);
		assertEquals(3, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(2));

		destination = fightMap.getFightTerrainAt(4, 4);
		out = fearow.findPathTo(destination, 10);
		assertEquals(3, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(2));

	}
	
	@Test
	public void moveThreeBlocksNormal() {

		// move from (3, 3) -> (2, 1)

		fearow.move(3, 3);

		FightTerrain destination = fightMap.getFightTerrainAt(2, 1);
		Path out = fearow.findPathTo(destination, 10);
		assertEquals(4, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(3));

		destination = fightMap.getFightTerrainAt(4, 5);
		out = fearow.findPathTo(destination, 10);
		assertEquals(4, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(3));
		
		destination = fightMap.getFightTerrainAt(0, 3);
		out = fearow.findPathTo(destination, 15);
		assertEquals(4, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(3));
		
		destination = fightMap.getFightTerrainAt(6, 3);
		out = fearow.findPathTo(destination, 20);
		assertEquals(4, out.size());
		assertEquals(fearow.getCurrentFightTerrain(), out.get(0));
		assertEquals(destination, out.get(3));
		
		destination = fightMap.getFightTerrainAt(3, 6);
		System.out.print("Expected Error will appear --> ");
		out = fearow.findPathTo(destination, 48); // Upper Limit for 3 blocks
		assertNull(out);

	}

}
