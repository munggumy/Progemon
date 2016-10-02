package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import logic.character.Pokemon;
import logic.character.Pokemon.MoveType;
import logic.player.AIPlayer;
import logic.player.Player;

public class TestPokemon {
	
	static final double EPSILON = 1e-15;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testConstructor() {
		
		// Constructor 01
		
		Pokemon pk1 = new Pokemon(1, 1.11, 2.22, 3.33, 4.44, 6, 7, MoveType.SWIM);
		assertEquals("ID", 1, pk1.getID());
		assertTrue("Base Attack", Double.compare(1.11, pk1.getBase().attackStat) == 0);
		assertTrue("Base Defence", Double.compare(2.22, pk1.getBase().defenceStat) == 0);
		assertTrue("Base Speed", Double.compare(3.33, pk1.getBase().speed) == 0);
		assertTrue("Base HP", Double.compare(4.44, pk1.getBase().fullHP) == 0);
		assertEquals("Move Range", 6, pk1.getMoveRange());
		assertEquals("Attack Range", 7, pk1.getAttackRange());
		assertEquals("Move Type", MoveType.SWIM, pk1.getMoveType());
		assertEquals("Active Skills", 0, pk1.getActiveSkills().size());
		
		// Test Ownership
		
		assertNull(pk1.getOwner());
		
		Player p1 = new AIPlayer("John Doe", pk1);
		
		assertNotNull(pk1.getOwner());
		assertEquals("Owner", p1, pk1.getOwner());
		assertEquals(1, p1.getPokemons().size());
		
		// Constructor 02
		
		Pokemon pk2 = new Pokemon(2, 1.12, 2.23, 3.34, 4.45, 5, 6, MoveType.FLY, p1);
		assertEquals("ID", 2, pk2.getID());
		assertTrue("Base Attack", Double.compare(1.12, pk2.getBase().attackStat) == 0);
		assertTrue("Base Defence", Double.compare(2.23, pk2.getBase().defenceStat) == 0);
		assertTrue("Base Speed", Double.compare(3.34, pk2.getBase().speed) == 0);
		assertTrue("Base HP", Double.compare(4.45, pk2.getBase().fullHP) == 0);
		assertEquals("Move Range", 5, pk2.getMoveRange());
		assertEquals("Attack Range", 6, pk2.getAttackRange());
		assertEquals("Move Type", MoveType.FLY, pk2.getMoveType());
		assertEquals("Owner", p1, pk2.getOwner());
		assertEquals(2, p1.getPokemons().size());
		assertTrue(p1.getPokemons().contains(pk2));
		
		
		// Next Turn Time
		
		double speed = pk2.getSpeed();
		double nextTurnTime = 1 / speed;
		assertTrue("Initial next turn time", Double.compare(nextTurnTime, pk2.getNextTurnTime()) == 0);
		
		pk2.calculateNextTurnTime();
		
		assertTrue("2nd next turn time", Double.compare(2 * nextTurnTime, pk2.getNextTurnTime()) == 0);
		
		for(int i = 3; i <= 10 ; i++){
			pk2.calculateNextTurnTime();
			assertEquals(i * nextTurnTime, pk2.getNextTurnTime(), EPSILON);
		}
		
		// Current HP
		
		double b = pk2.getBase().fullHP;
		
		assertTrue(Double.compare(pk2.getCurrentHP(), pk2.getFullHP()) == 0);
		
		pk2.changeHP(-1);
		assertEquals(pk2.getCurrentHP(), pk2.getFullHP() - 1, EPSILON);
		assertEquals(b, pk2.getBase().fullHP, EPSILON);
		
		pk2.changeHP(1);
		assertEquals(pk2.getCurrentHP(), pk2.getFullHP(), EPSILON);
		assertEquals(b, pk2.getBase().fullHP, EPSILON);
		
		double a = pk2.getFullHP();
		
		pk2.changeHP(-1000);
		assertEquals(0, pk2.getCurrentHP(), EPSILON);
		assertEquals(a, pk2.getFullHP(), EPSILON);
		assertEquals(b, pk2.getBase().fullHP, EPSILON);
		
		// currentHP, nextTurnTime, owner,element, activeSkills, passiveSkills,
		// current, image, iv, level, exp
	
	}

}
