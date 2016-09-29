package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import logic.character.Pokemon;
import logic.character.Pokemon.MoveType;
import logic.filters.MoveFilter;
import logic.terrain.FightMap;
import utility.FileUtility;

public class TestPokemonFindPath {

	private Pokemon pikachu;
	private int pikachuMoveRange = 3;
	private int pikachuAttackRange = 2;

	@Before
	public void setUp() throws Exception {
		FightMap fightMap = new FightMap(FileUtility.loadFightMap());
		pikachu = new Pokemon(23, 1.01, 1.11, 12.3, 18.5, pikachuMoveRange, pikachuAttackRange, MoveType.FLY);
		pikachu.setCurrentFightMap(fightMap);
		pikachu.move(1, 1);
	}

	@Test
	public void testBasics() {
		assertEquals(1, pikachu.getX());
		assertEquals(1, pikachu.getY());
		assertEquals(MoveType.FLY, pikachu.getMoveType());
		assertEquals(pikachuMoveRange, pikachu.getMoveRange());
		assertEquals(pikachuAttackRange, pikachu.getAttackRange());
	}

	@Test
	public void testFindBlocks() {
		
		// case range = 0
		
		pikachu.findBlocksAround(0, new MoveFilter());
		System.out.println(pikachu.getPaths());
		assertEquals(1, pikachu.getPaths().size());
		assertEquals(pikachu.getCurrentFightTerrain(), pikachu.getPaths().get(0).getThisNode());
	}

}
