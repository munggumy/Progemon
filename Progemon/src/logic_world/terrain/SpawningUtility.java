package logic_world.terrain;

import java.util.HashSet;
import java.util.Set;

import audio.MusicUtility;
import javafx.scene.paint.Color;
import logic_fight.player.HPAIPlayer;
import logic_fight.player.Player;
import logic_world.player.PlayerCharacter;
import manager.GUIFightGameManager;
import manager.WorldManager;
import utility.AnimationUtility;
import utility.Clock;
import utility.RandomUtility;

public class SpawningUtility {
	public static void trySpawnPokemon(WorldMap map) {
		if(WorldManager.getPlayer().hasRepel()){
			return;
		}
		Player wildAI = new HPAIPlayer("Joe", Color.RED);
		for (SpawningPokemonEntry entry : map.getSortedPokemonSpawningChance()) {
			if (RandomUtility.randomPercent(100) <= entry.getRate() / 187.5) {
				if (wildAI.getPokemons().size() < 3) {
					wildAI.addPokemon(entry.getPokemon());
				}
			}
		}
		if (wildAI.getPokemons().size() > 0) {
			MusicUtility.playMusic("battle_wild", false);
			System.out.println("to fightmap !!!");
			Set<Player> players = new HashSet<Player>();
			players.add(PlayerCharacter.instance.getMe());
			players.add(wildAI);
			GUIFightGameManager fight = new GUIFightGameManager(players, true);
			AnimationUtility.getLoadScreen00().show();
			AnimationUtility.getLoadScreen00().play();
			while (AnimationUtility.getLoadScreen00().isPlaying()) {
				Clock.tick();
			}
			fight.startFight();
			MusicUtility.playMusic(WorldManager.getWorldMap().getMapProperties().getProperty("music"), true);
		}
	}
}
