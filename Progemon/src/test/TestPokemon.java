package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import logic.character.Pokemon;
import logic.character.Pokemon.MoveType;

public class TestPokemon {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructor() {
		Pokemon pk1 = new Pokemon(1, 1.11, 2.22, 3.33, 4.44,6, 7, MoveType.SWIM);
		assertEquals("ID", 1, pk1.getID());
		assertTrue("Base Attack", Double.compare(1.11, pk1.getAttackStat()) == 0);
		assertTrue("Base Defence", Double.compare(2.22, pk1.getDefenceStat()) == 0);
		assertTrue("Base Speed", Double.compare(3.33, pk1.getSpeed()) == 0);
		assertTrue("Base HP", Double.compare(4.44, pk1.getFullHP()) == 0);
		assertEquals("Move Range", 6, pk1.getMoveRange());
		assertEquals("Attack Range", 7, pk1.getAttackRange());
		assertEquals("Move Type", MoveType.SWIM, pk1.getMoveType());
		
		// currentHP, nextTurnTime, x, y, owner,element, activeSkills, passiveSkills, current, image, iv
		
		
		
		
		
		
	}

}
