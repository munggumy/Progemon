/**
 * 
 */
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import logic.character.ActiveSkill;
import logic.character.Pokemon;
import logic.terrain.FightTerrain;
import utility.Pokedex;
import utility.FileUtility;

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
			FileUtility.loadPokedex();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check HashMap properties
		
		Pokedex.printPokedex();

		assertEquals("Length = 28", 28, Pokedex.getPokedex().size());

		// Contains

		assertTrue("Contains Bulbasaur", Pokedex.getPokedex().containsValue("Bulbasaur"));
		assertTrue("Contains Bulbasaur", Pokedex.getPokedex().containsKey(1));
		assertEquals("ID 001 = Bulbasaur", Pokedex.getPokedex().get(1), "Bulbasaur");

		assertTrue("Contains Charmander", Pokedex.getPokedex().containsValue("Charmander"));
		assertTrue("Contains Charmander", Pokedex.getPokedex().containsKey(4));
		assertEquals("ID 004 = Charmander", Pokedex.getPokedex().get(4), "Charmander");
		
		assertTrue("Contains Pikachu", Pokedex.getPokedex().containsValue("Pikachu"));
		assertTrue("Contains Pikachu", Pokedex.getPokedex().containsKey(25));
		assertEquals("ID 025 = Pikachu", Pokedex.getPokedex().get(25), "Pikachu");

		// Doesn't Contains
		assertFalse("Doesn't contains Satoshi", Pokedex.getPokedex().containsValue("Satoshi"));
		assertFalse("Doesn't contains 000", Pokedex.getPokedex().containsKey(0));

	}

	@Test
	public void testLoadFightMap() {
		FightTerrain[][] fightMap = null;
		try {
			fightMap = FileUtility.loadFightMap();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		assertFalse("Fightmap != null", fightMap == null);
		assertEquals("Width = 8", 8, fightMap[0].length);
		assertEquals("Height = 6", 6, fightMap.length);

		// GRASS GRASS GRASS GRASS GRASS GRASS GRASS GRASS
		assertTrue("Top Left Terrain", new FightTerrain(0, 0, FightTerrain.TerrainType.GRASS).equals(fightMap[0][0]));

		for (int i = 1; i <= 7; i++) {
			assertTrue("First Row, Column : " + i,
					new FightTerrain(i, 0, FightTerrain.TerrainType.GRASS).equals(fightMap[0][i]));
		}

		// GRASS ROCK ROCK TREE WATER WATER GRASS GRASS
		assertTrue("Second Row First Column",
				new FightTerrain(0, 1, FightTerrain.TerrainType.GRASS).equals(fightMap[1][0]));
		assertTrue(new FightTerrain(1,1, FightTerrain.TerrainType.ROCK).equals(fightMap[1][1]));
		assertTrue(new FightTerrain(3,1, FightTerrain.TerrainType.TREE).equals(fightMap[1][3]));
		assertTrue(new FightTerrain(5,1, FightTerrain.TerrainType.WATER).equals(fightMap[1][5]));

	}

	@Test
	public void testLoadPokemonList() {
		try {
			FileUtility.loadPokemons();
		} catch (IOException e) {
			e.printStackTrace();
			fail("Can't Load Pokemon File");
		}

		assertEquals("All Pokemons = 10", 10, Pokedex.getAllPokemons().size());

		String[] args = "001 1.01 0.95 10 50 3 1 WALK".split(" ");
		Pokemon testFirstPokemon = new Pokemon(args);
		Pokemon firstPokemon = Pokedex.getAllPokemons().get(0);
		
		assertTrue("First Pokemon Stats", Double.compare(testFirstPokemon.getBase().attackStat, firstPokemon.getBase().attackStat) == 0);
		assertTrue("First Pokemon Stats", Double.compare(testFirstPokemon.getBase().defenceStat, firstPokemon.getBase().defenceStat) == 0);
		assertTrue("First Pokemon Stats",
				Double.compare(testFirstPokemon.getMoveRange(), firstPokemon.getMoveRange()) == 0);
		assertTrue("First Pokemon Stats", Double.compare(testFirstPokemon.getBase().speed, firstPokemon.getBase().speed) == 0);
		assertTrue("First Pokemon Stats", Double.compare(testFirstPokemon.getBase().fullHP, firstPokemon.getBase().fullHP) == 0);
		assertEquals("First Pokemon Moves", 0, firstPokemon.getActiveSkills().size());
	}

	@Test
	public void testLoadActiveSkills() {
		ActiveSkill.clearAllActiveSkills();
		try {
			FileUtility.loadActiveSkills();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<ActiveSkill> aSkills = new ArrayList<ActiveSkill>(ActiveSkill.getAllActiveSkills());

		assertEquals("First Move : Mega Punch", "Mega Punch", aSkills.get(0).getName());
		assertTrue("First Move : Mega Punch", Double.compare(80, aSkills.get(0).getPower()) == 0);
		assertEquals("Second Move : Razor Wind", "Razor Wind", aSkills.get(1).getName());
		assertTrue("Second Move : Razor Wind", Double.compare(80, aSkills.get(1).getPower()) == 0);
	}
	
	
	@Test
	public void testLoadActiveSkills2(){
		ActiveSkill.clearAllActiveSkills();
		String customFilePath = "test/test1loadActiveSkills.txt";
		try {
			FileUtility.loadActiveSkills(customFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<ActiveSkill> aSkills = new ArrayList<ActiveSkill>(ActiveSkill.getAllActiveSkills());
		
		assertEquals("Size of allActiveSkills", 2, aSkills.size());
		
		assertEquals("First Move : Hello WORLD", "Hello World", aSkills.get(0).getName());
		assertTrue("First Move : Hello WORLD", Double.compare(13, aSkills.get(0).getPower()) == 0);
		assertEquals("Second Move : Utit Mo Ko", "Utit Mo Ko", aSkills.get(1).getName());
		assertTrue("Second Move : Utit Mo Ko", Double.compare(91, aSkills.get(1).getPower()) == 0);
	}

}
