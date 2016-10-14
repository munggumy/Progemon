package main;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import logic.character.Pokemon;
import logic.player.AIPlayer;
import logic.player.HPAIPlayer;
import logic.player.Player;
import manager.GUIFightGameManager;
import utility.FileUtility;
import utility.Pokedex;

public class Main2 {

	public static void main(String[] args) {

		System.out.println("Game loaded without problems.");
		try {
			FileUtility.loadActiveSkills();
			FileUtility.loadPokedex();
			FileUtility.loadPokemons();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Pokemon charlizard = Pokedex.getPokemon("Charlizard");
		charlizard.setLevel(38);
		charlizard.calculateCurrentStats();
		charlizard.resetHP();
		Pokemon caterpie = Pokedex.getPokemon("Caterpie");
		caterpie.setLevel(5);
		caterpie.calculateCurrentStats();
		caterpie.resetHP();

		Pokemon wartortle = Pokedex.getPokemon("Wartortle");
		wartortle.setLevel(34);
		wartortle.calculateCurrentStats();
		wartortle.resetHP();
		Pokemon pidgeotto = Pokedex.getPokemon("Pidgeotto");
		pidgeotto.setLevel(30);
		pidgeotto.calculateCurrentStats();
		pidgeotto.resetHP();

		Player p1 = new HPAIPlayer("AI 1", charlizard, Color.RED);
		p1.addPokemon(caterpie);
		Player p2 = new AIPlayer("AI 2", pidgeotto, Color.BLUE);
		p2.addPokemon(wartortle);

		ArrayList<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);


		System.out.println("Game loaded without problems.");
		@SuppressWarnings("unused")
		GUIFightGameManager gui = new GUIFightGameManager(players);

	}

}
