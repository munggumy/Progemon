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
import utility.fileUtility;

public class TestFightGameManager {
	
	FightMap map;
	Player p1,p2;
	ArrayList<Player> players;

	@Before
	public void setUp() throws Exception {
		fileUtility.loadPokemons();
		fileUtility.loadPokedex();
		map = new FightMap(fileUtility.loadFightMap());
		
		p1 = new AIPlayer("AI 1", Pokedex.getPokemon(4));
		p2 = new AIPlayer("AI 2", Pokedex.getPokemon(1));
		
		players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
	}

	@Test
	public void testConstructor() {
		FightGameManager gm = new FightGameManager(players);
		assertEquals(p1, FightGameManager.getPlayers().get(0));
		assertEquals(p2, FightGameManager.getPlayers().get(1));
		assertTrue(FightGameManager.getWinner() == null);
		assertTrue(FightGameManager.getCurrentPlayers().contains(p1));
		
	}

}
