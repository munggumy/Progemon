package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import logic.character.AIPlayer;
import logic.character.Player;
import logic.terrain.FightMap;
import manager.FightGameManager;
import utility.Pokedex;
import utility.FileUtility;

public class TestFightGameManager {
	
	static FightMap map;
	static Player p1,p2;
	static ArrayList<Player> players;

	@Before
	public void setUp() throws Exception {
		FileUtility.loadPokemons();
		FileUtility.loadPokedex();
		map = new FightMap(FileUtility.loadFightMap());
		
		p1 = new AIPlayer("AI 1", Pokedex.getPokemon(4));
		p2 = new AIPlayer("AI 2", Pokedex.getPokemon(1));
		
		players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
	}

	@Test
	public void testConstructor() {
		FightGameManager gm = new FightGameManager(players);
//		assertEquals(p1, FightGameManager.getPlayers().get(0));
//		assertEquals(p2, FightGameManager.getPlayers().get(1));
//		assertTrue(FightGameManager.getWinner() == null);
//		assertTrue(FightGameManager.getCurrentPlayers().contains(p1));
		
	}

}
