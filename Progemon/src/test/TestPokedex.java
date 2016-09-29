package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import logic.character.Pokemon;
import logic.character.Pokemon.MoveType;
import utility.Pokedex;

public class TestPokedex {

	private HashMap<Integer, String> pokedex;
	//Usage name/ID attack defense speed hp moveRange attackRange moveType
	private Pokemon pikachu = new Pokemon(23, 1.01, 1.01, 3, 8.9, 3,3, MoveType.WALK);
	private Pokemon pikachu2 = new Pokemon(23, 1.10, 1.07, 3, 8.87, 2,4, MoveType.WALK);
	private Pokemon weedle = new Pokemon(7, 0.79, 0.91, 4, 10.9, 3,6, MoveType.WALK);
	private Pokemon pidgeot = new Pokemon(14, 1.59, 1.30, 5, 16.0, 1,4, MoveType.FLY);

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testAddToPokedex() {
		pokedex = Pokedex.getPokedex();
		pokedex.clear();
		Pokedex.addPokemonToPokedex(20, "Test1");

		assertEquals(1, pokedex.size());
		assertTrue(pokedex.containsKey(20));
		assertTrue(pokedex.containsValue("Test1"));

		Pokedex.addPokemonToPokedex(30, "Test2");

		assertEquals(2, pokedex.size());
		assertTrue(pokedex.containsKey(30));
		assertTrue(pokedex.containsValue("Test2"));

		Pokedex.addPokemonToPokedex(30, "Override");

		assertEquals(2, pokedex.size());
		assertTrue(pokedex.containsKey(30));
		assertFalse(pokedex.containsValue("Override"));

		Pokedex.addPokemonToPokedex(21, "Test1");

		assertEquals(2, pokedex.size());
		assertFalse(pokedex.containsKey(21));
		assertTrue(pokedex.containsValue("Test1"));

	}

	@Test
	public void testAddPokemonToList() {
		Pokedex.clearAllPokemons();
		assertTrue(Pokedex.getAllPokemons().isEmpty());

		Pokedex.addPokemonToList(pikachu);

		assertFalse(Pokedex.getAllPokemons().isEmpty());
		assertEquals(1, Pokedex.getAllPokemons().size());
		assertEquals(pikachu, Pokedex.getAllPokemons().get(0));

		Pokedex.addPokemonToList(pidgeot);

		assertEquals(2, Pokedex.getAllPokemons().size());
		assertEquals(pidgeot, Pokedex.getAllPokemons().get(0));
		assertEquals(pikachu, Pokedex.getAllPokemons().get(1));

		Pokedex.addPokemonToList(weedle);

		assertEquals(3, Pokedex.getAllPokemons().size());
		assertEquals(weedle, Pokedex.getAllPokemons().get(0));
		assertEquals(pidgeot, Pokedex.getAllPokemons().get(1));
		assertEquals(pikachu, Pokedex.getAllPokemons().get(2));

		Pokedex.addPokemonToList(pikachu2);

		assertEquals(3, Pokedex.getAllPokemons().size());
		assertEquals(weedle, Pokedex.getAllPokemons().get(0));
		assertEquals(pidgeot, Pokedex.getAllPokemons().get(1));
		assertEquals(pikachu, Pokedex.getAllPokemons().get(2));

	}

	@Test
	public void testGetPokemonInt() {
		Pokedex.clearAllPokemons();
		assertTrue(Pokedex.getAllPokemons().isEmpty());

		Pokedex.addPokemonToList(pikachu);
		Pokedex.addPokemonToList(pidgeot);
		assertEquals(2, Pokedex.getAllPokemons().size());

		Pokemon tester;
		tester = Pokedex.getPokemon(pikachu.getID());
		assertEquals(pikachu, tester);
		assertNotEquals(weedle, tester);
		tester = Pokedex.getPokemon(pidgeot.getID());
		assertEquals(pidgeot, tester);
		assertNotEquals(pikachu, tester);

		tester = Pokedex.getPokemon(weedle.getID());
		assertNull(tester);

	}

	@Test public void testGetPokemonString(){
		Pokedex.clearAllPokemons();
		assertTrue(Pokedex.getAllPokemons().isEmpty());
		
		Pokedex.addPokemonToPokedex(pikachu.getID(), "Pikachu");
		
		Pokedex.addPokemonToList(pikachu);
		Pokedex.addPokemonToList(pidgeot);
		assertEquals(2, Pokedex.getAllPokemons().size());
		
		Pokemon tester;
		
		// Equals ignore case
		
		tester = Pokedex.getPokemon("Pikachu");
		assertEquals(pikachu, tester);
		tester = Pokedex.getPokemon("pikachu");
		assertEquals(pikachu, tester);
		tester = Pokedex.getPokemon("PIkaChU");
		assertEquals(pikachu, tester);
		
		
	}
}
