/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import utility.Pokedex;
import utility.fileUtility;

/**
 * @author Kris
 *
 */
public class fileUtitlityTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void test() {
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
		
		//Doesn't Contains
		assertFalse("Doesn't contains Satoshi", Pokedex.getPokedex().containsValue("Satoshi"));
		assertFalse("Doesn't contains 000", Pokedex.getPokedex().containsKey(0));

	}

}
