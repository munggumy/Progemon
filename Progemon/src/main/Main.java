package main;

import graphic.Frame;
import graphic.ScreenComponent;
import logic.character.Pokemon;
import logic.terrain.FightMap;
import utility.FileUtility;
import utility.Pokedex;
import utility.RandomUtility;

public class Main {

	public static void main(String[] args) {

		new Frame();

		int tick = 0;

		FightMap fightMap = new FightMap(FileUtility.loadFightMap());
		FileUtility.loadPokedex();
		FileUtility.loadPokemons();

		Pokemon a = Pokedex.getPokemon("Charlizard");
		Pokemon b = Pokedex.getPokemon("Ivysaur");
		a.setCurrentFightMap(fightMap);
		b.setCurrentFightMap(fightMap);
		a.move(8, 8);
		b.move(7, 7);

		// add
		ScreenComponent.addObject(fightMap);
		ScreenComponent.addObject(a);
		ScreenComponent.addObject(b);

		// update
		Frame.getGraphicComponent().repaint();

		while (true) {
			tick++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (tick == 100) {
				tick = 0;
				a.move(RandomUtility.randomInt(7), RandomUtility.randomInt(5));
				b.move(RandomUtility.randomInt(7), RandomUtility.randomInt(5));
			}

			// updates
			Frame.getGraphicComponent().repaint();
		}
	}

}
