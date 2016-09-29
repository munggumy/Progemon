package test;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import logic.character.AIPlayer;
import logic.character.ActiveSkill;
import logic.character.Player;
import logic.character.Pokemon;
import logic.terrain.FightMap;
import manager.FightGameManager;
import utility.FileUtility;
import utility.Pokedex;

public class TestFightGameManager {
	
	static FightMap map;
	static Player p1,p2;
	static ArrayList<Player> players;

	@Before
	public void setUp() throws Exception {
		FileUtility.loadPokemons();
		FileUtility.loadPokedex();
		FileUtility.loadActiveSkills();
		map = new FightMap(FileUtility.loadFightMap());
		Pokemon charlizard = Pokedex.getPokemon(6);
		charlizard.addActiveSkill(ActiveSkill.getActiveSkill("Tackle"));
		charlizard.addActiveSkill(ActiveSkill.getActiveSkill("Fire Blast"));
		charlizard.addActiveSkill(ActiveSkill.getActiveSkill("Fly"));
		Pokemon ivysaur = Pokedex.getPokemon(2);
		ivysaur.addActiveSkill(ActiveSkill.getActiveSkill("Tackle"));
		ivysaur.addActiveSkill(ActiveSkill.getActiveSkill("Razor Leaf"));
		
		p1 = new AIPlayer("AI 1", charlizard);
		p2 = new AIPlayer("AI 2", ivysaur);
		
		players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
	}

	@Test
	public void testConstructor() {
		@SuppressWarnings("unused")
		FightGameManager gm = new FightGameManager(players);
//		assertEquals(p1, FightGameManager.getPlayers().get(0));
//		assertEquals(p2, FightGameManager.getPlayers().get(1));
//		assertTrue(FightGameManager.getWinner() == null);
//		assertTrue(FightGameManager.getCurrentPlayers().contains(p1));
		
	}

}
