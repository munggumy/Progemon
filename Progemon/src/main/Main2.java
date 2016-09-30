package main;

import java.io.IOException;
import java.util.ArrayList;

import logic.character.AIPlayer;
import logic.character.ActiveSkill;
import logic.character.Player;
import logic.character.Pokemon;
import manager.GUIFightGameManager;
import utility.FileUtility;
import utility.Pokedex;

public class Main2 {
	
	public static void main(String[] args) {
		try {
			FileUtility.loadPokedex();
			FileUtility.loadPokemons();
			FileUtility.loadActiveSkills();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Pokemon charlizard = Pokedex.getPokemon("Charlizard");
		charlizard.addActiveSkill(ActiveSkill.getActiveSkill("Tackle"));
		charlizard.addActiveSkill(ActiveSkill.getActiveSkill("Ember"));
		
		Pokemon ivysaur = Pokedex.getPokemon("Ivysaur");
		ivysaur.addActiveSkill(ActiveSkill.getActiveSkill("Razor Leaf"));
		
		Pokemon wartortle = Pokedex.getPokemon("Wartortle");
		wartortle.addActiveSkill(ActiveSkill.getActiveSkill("Surf"));
		wartortle.addActiveSkill(ActiveSkill.getActiveSkill("Bubblebeam"));
		wartortle.addActiveSkill(ActiveSkill.getActiveSkill("Tackle"));
		
		Pokemon ivysaur2 = Pokedex.getPokemon("Ivysaur");
		
		
		Player p1 = new AIPlayer("AI 1", charlizard);
		p1.addPokemon(ivysaur2);
		Player p2 = new AIPlayer("AI 2", ivysaur);
		p2.addPokemon(wartortle);
		
		
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
		
		GUIFightGameManager gui = new GUIFightGameManager(players);
		
	}

}
