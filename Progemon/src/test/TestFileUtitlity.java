/**
 * 
 */
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import logic.character.ActiveSkill;
import logic.character.Element;
import logic.character.Element.SW;
import logic.character.Pokemon;
import logic.terrain.FightTerrain;
import utility.FileUtility;
import utility.Pokedex;

/**
 * @author Kris
 *
 */
public class TestFileUtitlity {

	public static final double EPSILON = 1e-15;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testLoadPokedex() {

		FileUtility.loadPokedex();

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
		fightMap = FileUtility.loadFightMap();

		assertFalse("Fightmap != null", fightMap == null);
		assertEquals("Width = 8", 8, fightMap[0].length);
		assertEquals("Height = 6", 6, fightMap.length);

		// GRASS GRASS GRASS GRASS GRASS GRASS GRASS GRASS
		assertTrue("Top Left Terrain",
				new FightTerrain((short) 0, (short) 0, FightTerrain.TerrainType.GRASS).equals(fightMap[0][0]));

		for (short i = 1; i <= 7; i++) {
			assertTrue("First Row, Column : " + i,
					new FightTerrain(i, (short) 0, FightTerrain.TerrainType.GRASS).equals(fightMap[0][i]));
		}

		// GRASS ROCK ROCK TREE WATER WATER GRASS GRASS
		assertTrue("Second Row First Column",
				new FightTerrain((short) 0, (short) 1, FightTerrain.TerrainType.GRASS).equals(fightMap[1][0]));
		assertTrue(new FightTerrain((short) 1, (short) 1, FightTerrain.TerrainType.ROCK).equals(fightMap[1][1]));
		assertTrue(new FightTerrain((short) 3, (short) 1, FightTerrain.TerrainType.TREE).equals(fightMap[1][3]));
		assertTrue(new FightTerrain((short) 5, (short) 1, FightTerrain.TerrainType.WATER).equals(fightMap[1][5]));

	}

	@Test
	public void testLoadPokemonList() {
		FileUtility.loadActiveSkills();
		FileUtility.loadPokemons();

		assertEquals("All Pokemons = 18", 18, Pokedex.getAllPokemons().size());

		String[] args = "001 49 49 45 45 1 1 WALK".split(" ");
		Pokemon testFirstPokemon = new Pokemon(args);
		Pokemon firstPokemon = Pokedex.getAllPokemons().get(0);
		
		assertEquals("First Pokemon Stats", testFirstPokemon.getBase().attackStat, firstPokemon.getBase().attackStat,
				EPSILON);
		assertEquals("First Pokemon Stats DEF", testFirstPokemon.getBase().defenceStat, firstPokemon.getBase().defenceStat,
				EPSILON);
		assertEquals("First Pokemon Stats", testFirstPokemon.getMoveRange(), firstPokemon.getMoveRange(), EPSILON);
		assertEquals("First Pokemon Stats", testFirstPokemon.getBase().speed, firstPokemon.getBase().speed, EPSILON);
		assertEquals("First Pokemon Stats", testFirstPokemon.getBase().fullHP, firstPokemon.getBase().fullHP, EPSILON);
		assertEquals("First Pokemon Moves", 2, firstPokemon.getActiveSkills().size());

		// Ivysaur Moves = 2
		assertEquals("Ivysaur ActiveSkill", 2, Pokedex.getAllPokemons().get(1).getActiveSkills().size());
		assertEquals("Ivysaur ActiveSkill", "Tackle",
				Pokedex.getAllPokemons().get(1).getActiveSkills().get(0).getName());
		assertEquals("Ivysaur ActiveSkill", "Razor Leaf",
				Pokedex.getAllPokemons().get(1).getActiveSkills().get(1).getName());
	}

	@Test
	public void testLoadActiveSkills() {
		ActiveSkill.clearAllActiveSkills();

		FileUtility.loadActiveSkills();

		ArrayList<ActiveSkill> aSkills = new ArrayList<ActiveSkill>(ActiveSkill.getAllActiveSkills());

		assertEquals("First Move : Mega Punch", "Mega Punch", aSkills.get(0).getName());
		assertTrue("First Move : Mega Punch", Double.compare(80, aSkills.get(0).getPower()) == 0);
		assertEquals("Second Move : Razor Wind", "Razor Wind", aSkills.get(1).getName());
		assertTrue("Second Move : Razor Wind", Double.compare(80, aSkills.get(1).getPower()) == 0);
	}

	@Test
	public void testLoadActiveSkills2() {
		ActiveSkill.clearAllActiveSkills();
		String customFilePath = "test/test1loadActiveSkills.txt";

		FileUtility.loadActiveSkills(customFilePath);

		ArrayList<ActiveSkill> aSkills = new ArrayList<ActiveSkill>(ActiveSkill.getAllActiveSkills());

		assertEquals("Size of allActiveSkills", 2, aSkills.size());

		assertEquals("First Move : Hello WORLD", "Hello World", aSkills.get(0).getName());
		assertTrue("First Move : Hello WORLD", Double.compare(13, aSkills.get(0).getPower()) == 0);
		assertEquals("Second Move : Utit Mo Ko", "Utit Mo Ko", aSkills.get(1).getName());
		assertTrue("Second Move : Utit Mo Ko", Double.compare(91, aSkills.get(1).getPower()) == 0);
	}

	@Test
	public void testLoadSWTable() {
		FileUtility.loadStrengthWeaknessTable();
		SW[][] table = Element.SWFactor;
		assertNotNull(table);
		assertEquals(17, table.length);
		assertEquals(17, table[0].length);
		assertEquals(Element.SW.N, table[0][0]);
		assertEquals(Element.SW.W, table[1][1]);
		assertEquals(Element.SW.S, table[2][1]);
		assertEquals(Element.SW.W, table[1][2]);
		assertEquals(Element.SW.N, table[5][0]);
	}

}
