package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import logic_fight.character.pokemon.Pokemon;
import logic_fight.character.pokemon.Pokemon.MoveType;
import logic_fight.filters.MoveFilter;
import logic_fight.terrain.FightMap;
import logic_fight.terrain.FightTerrain;
import logic_fight.terrain.Path;
import logic_fight.terrain.FightTerrain.TerrainType;
import utility.FileUtility;

public class TestPokemonFindPath {

	private Pokemon fearow, pikachu;
	private int fearowMoveRange = 3;
	private int fearowAttackRange = 2;

	private int pikachuMoveRange = 3;
	private int pikachuAttackRange = 1;

	FightMap fightMap;

	@Before
	public void setUp() throws Exception {
		fightMap = new FightMap(FileUtility.loadFightMap());
		fearow = new Pokemon(22, 1.01, 1.11, 12.3, 18.5, fearowMoveRange, fearowAttackRange, MoveType.FLY);
		pikachu = new Pokemon(25, 0.98, 1.35, 10.9, 22.1, pikachuMoveRange, pikachuAttackRange, MoveType.WALK);
		fearow.setCurrentFightMap(fightMap);
		pikachu.setCurrentFightMap(fightMap);
		fearow.move(1, 1);
		pikachu.move(0, 0);
	}

	@Test
	public void testBasics() {

		System.out.println("Test Basics");
		assertEquals(1, fearow.getCurrentFightTerrain().getX());
		assertEquals(1, fearow.getCurrentFightTerrain().getY());
		assertEquals(MoveType.FLY, fearow.getMoveType());
		assertEquals(fearowMoveRange, fearow.getMoveRange());
		assertEquals(fearowAttackRange, fearow.getAttackRange());

		assertEquals(TerrainType.ROCK, fearow.getCurrentFightTerrain().getType());
		assertEquals(fearow.getCurrentFightMap(), pikachu.getCurrentFightMap());

	}

	@Test
	public void testFindBlocksCaseZero() {

		System.out.println("Test Find Blocks Case Zero");

		// case range = 0
		System.out.println("(0, 0)");
		fearow.move(0, 0);

		fearow.findBlocksAround(0, new MoveFilter());
		assertEquals(1, fearow.getPaths().size());
		assertEquals(fearow.getCurrentFightTerrain(), fearow.getPaths().get(0).getLast());

		System.out.println("(5, 5)");
		fearow.move(5, 5);

		fearow.findBlocksAround(0, new MoveFilter());
		assertEquals(1, fearow.getPaths().size());
		assertEquals(fearow.getCurrentFightTerrain(), fearow.getPaths().get(0).getLast());
	}

	@Test
	public void testFindBlocksCenter() {

		System.out.println("Test Find Blocks Center");

		// Set terrains
		pikachu.getCurrentFightMap().setFightTerrainAt(1, 1, new FightTerrain((short) 1, (short) 1, TerrainType.ROCK));
		pikachu.getCurrentFightMap().setFightTerrainAt(2, 1, new FightTerrain((short) 2, (short) 1, TerrainType.WATER));
		pikachu.getCurrentFightMap().setFightTerrainAt(1, 2, new FightTerrain((short) 1, (short) 2, TerrainType.ROCK));

		// test terrains
		fearow.move(1, 1);
		assertEquals(TerrainType.ROCK, fearow.getCurrentFightTerrain().getType());
		fearow.move(2, 1);
		assertEquals(TerrainType.WATER, fearow.getCurrentFightTerrain().getType());
		fearow.move(1, 2);
		assertEquals(TerrainType.ROCK, fearow.getCurrentFightTerrain().getType());

		// case range = 0

		fearow.move(5, 5);
		fearow.findBlocksAround(0, new MoveFilter());
		ArrayList<Path> nonNode = new ArrayList<Path>(fearow.getPaths());
		assertTrue(fearow.getPaths().get(0).getFirst().getX() == 5);
		assertTrue(fearow.getPaths().get(0).getFirst().getY() == 5);
		assertTrue(fearow.getPaths().get(0).getLast().getX() == 5);
		assertTrue(fearow.getPaths().get(0).getLast().getY() == 5);
		assertEquals(nonNode, fearow.getPaths());

		fearow.move(3, 3);
		fearow.findBlocksAround(0, new MoveFilter());
		ArrayList<Path> nodeZero = new ArrayList<Path>(fearow.getPaths());
		assertTrue(fearow.getPaths().get(0).getFirst().getX() == 3);
		assertTrue(fearow.getPaths().get(0).getFirst().getY() == 3);
		assertTrue(fearow.getPaths().get(0).getLast().getX() == 3);
		assertTrue(fearow.getPaths().get(0).getLast().getY() == 3);
		assertNotEquals(nonNode, nodeZero);
		assertEquals(nodeZero, fearow.getPaths());
		assertFalse(fearow.getPaths().containsAll(nonNode));

		// case range = 1

		fearow.findBlocksAround(1, new MoveFilter());
		ArrayList<Path> nodeOne = new ArrayList<Path>(fearow.getPaths());

		assertEquals(5, fearow.getPaths().size());
		assertTrue(fearow.getPaths().contains(nodeZero.get(0)));
		assertTrue(fearow.getPaths().containsAll(nodeOne));
		assertFalse(fearow.getPaths().contains(nonNode));

		// case range = 2

		fearow.findBlocksAround(2, new MoveFilter());
		ArrayList<Path> nodeTwo = new ArrayList<Path>(fearow.getPaths());

		assertEquals(21, fearow.getPaths().size());
		assertTrue(fearow.getPaths().contains(nodeZero.get(0)));
		assertTrue(fearow.getPaths().containsAll(nodeOne));
		assertFalse(fearow.getPaths().contains(nonNode));

		// case range = 3

		fearow.findBlocksAround(3, new MoveFilter());

		assertTrue(fearow.getPaths().contains(nodeZero.get(0)));
		assertTrue(fearow.getPaths().containsAll(nodeOne));
		assertTrue(fearow.getPaths().containsAll(nodeTwo));
		assertEquals(84, fearow.getPaths().size()); // 84 because reach map
													// bounds
		assertFalse(fearow.getPaths().containsAll(nonNode));
	}

	@Test
	public void testMapBoundary() {

		System.out.println("Test Map Boundary");

		// case range = 0

		fearow.move(0, 0);
		fearow.findBlocksAround(0, new MoveFilter());
		ArrayList<Path> nodeZero = new ArrayList<Path>(fearow.getPaths());

		// case range = 1 at position (0,0)

		fearow.findBlocksAround(1, new MoveFilter());
		ArrayList<Path> nodeOne = new ArrayList<Path>(fearow.getPaths());

		assertEquals(3, fearow.getPaths().size());
		assertTrue(fearow.getPaths().containsAll(nodeZero));

		// case range = 2 at position (0,0)

		fearow.findBlocksAround(2, new MoveFilter());

		assertEquals(9, fearow.getPaths().size());
		assertTrue(fearow.getPaths().containsAll(nodeZero));
		assertTrue(fearow.getPaths().containsAll(nodeOne));

	}

	@Test
	public void testUnmovableTerrains() {

		System.out.println("Test Unmovable Terrains");

		// case range 0 at position (0,1)

		pikachu.move(0, 1);
		pikachu.findBlocksAround(0, new MoveFilter());
		ArrayList<Path> nodeZero = new ArrayList<Path>(pikachu.getPaths());
		assertEquals(1, pikachu.getPaths().size());

		// case range 1 at position (0,1) -- rock on right side, boundary on
		// left side

		pikachu.findBlocksAround(1, new MoveFilter());
		ArrayList<Path> nodeOne = new ArrayList<Path>(pikachu.getPaths());
		assertEquals(3, pikachu.getPaths().size());
		assertTrue(pikachu.getPaths().containsAll(nodeZero));

		// case range 2 at position (0,1)

		pikachu.findBlocksAround(2, new MoveFilter());
		assertEquals(8, pikachu.getPaths().size());
		assertTrue(pikachu.getPaths().containsAll(nodeZero));
		assertTrue(pikachu.getPaths().containsAll(nodeOne));

	}

}
