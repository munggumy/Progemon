/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import logic.terrain.FightMap;
import logic.terrain.FightTerrain;
import utility.Pokedex;
import utility.fileUtility;

/**
 * @author Kris
 *
 */
public class TestFileUtitlity {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testLoadPokedex() {
		try {
			fileUtility.loadPokedex();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check HashMap properties

		assertEquals("Length = 11", 11, Pokedex.getPokedex().size());

		// Contains

		assertTrue("Contains Bulbasaur", Pokedex.getPokedex().containsValue("Bulbasaur"));
		assertTrue("Contains Bulbasaur", Pokedex.getPokedex().containsKey(1));
		assertEquals("ID 001 = Bulbasaur", Pokedex.getPokedex().get(1), "Bulbasaur");

		assertTrue("Contains Charmander", Pokedex.getPokedex().containsValue("Charmander"));
		assertTrue("Contains Charmander", Pokedex.getPokedex().containsKey(4));
		assertEquals("ID 004 = Charmander", Pokedex.getPokedex().get(4), "Charmander");

		// Doesn't Contains
		assertFalse("Doesn't contains Satoshi", Pokedex.getPokedex().containsValue("Satoshi"));
		assertFalse("Doesn't contains 000", Pokedex.getPokedex().containsKey(0));

	}

	@Test
	public void testLoadFightMap() {
		FightTerrain.TerrainType[][] fightMap = null;
		try {
			fightMap = fileUtility.loadFightMap();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		assertFalse("Fightmap != null", fightMap == null);
		assertEquals("Width = 8", 8, fightMap[0].length);
		assertEquals("Height = 6", 6, fightMap.length);

		// GRASS GRASS GRASS GRASS GRASS GRASS GRASS GRASS
		FightTerrain.TerrainType[] testFirstLine = { FightTerrain.TerrainType.GRASS, FightTerrain.TerrainType.GRASS,
				FightTerrain.TerrainType.GRASS, FightTerrain.TerrainType.GRASS, FightTerrain.TerrainType.GRASS,
				FightTerrain.TerrainType.GRASS, FightTerrain.TerrainType.GRASS, FightTerrain.TerrainType.GRASS };

		assertArrayEquals("First Line", testFirstLine, fightMap[0]);

		// GRASS ROCK ROCK TREE WATER WATER GRASS GRASS
		FightTerrain.TerrainType[] testSecondLine = { FightTerrain.TerrainType.GRASS, FightTerrain.TerrainType.ROCK,
				FightTerrain.TerrainType.ROCK, FightTerrain.TerrainType.TREE, FightTerrain.TerrainType.WATER,
				FightTerrain.TerrainType.WATER, FightTerrain.TerrainType.GRASS, FightTerrain.TerrainType.GRASS };

		assertArrayEquals("First Line", testSecondLine, fightMap[1]);
	}

}
