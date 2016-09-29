package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import logic.character.Pokemon;
import utility.Pokedex;

public class TestPokedex {

	private HashMap<Integer, String> pokedex;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test1AddToPokedex() {
		Pokedex.addPokemonToPokedex(20, "Test");
		pokedex = Pokedex.getPokedex();
		
		assertTrue(pokedex.containsKey(20));
		assertTrue(pokedex.containsValue("Test"));
		assertTrue(pokedex.size() == 1);
		
		Pokedex.addPokemonToPokedex(30, "Test2");
		
		assertTrue(pokedex.containsKey(30));
		assertTrue(pokedex.containsValue("Test2"));
		assertTrue(pokedex.size() == 2);
		
	}

}
