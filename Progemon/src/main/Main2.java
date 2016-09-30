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
			FileUtility.loadActiveSkills();
			FileUtility.loadPokedex();
			FileUtility.loadPokemons();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Pokemon charlizard = Pokedex.getPokemon("Charlizard");
		charlizard.setLevel(40);
		charlizard.calculateCurrentStats();
		Pokemon caterpie = Pokedex.getPokemon("Caterpie");
		caterpie.setLevel(5);
		caterpie.calculateCurrentStats();

		
		Pokemon wartortle = Pokedex.getPokemon("Wartortle");
		wartortle.setLevel(34);
		wartortle.calculateCurrentStats();
		Pokemon pidgeotto = Pokedex.getPokemon("Pidgeotto");
		pidgeotto.setLevel(30);
		pidgeotto.calculateCurrentStats();
		
		
		Player p1 = new AIPlayer("AI 1", charlizard);
		p1.addPokemon(caterpie);
		Player p2 = new AIPlayer("AI 2", pidgeotto);
		p2.addPokemon(wartortle);
		
		
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
		
		GUIFightGameManager gui = new GUIFightGameManager(players);
		
	}

}
