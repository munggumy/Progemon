package graphic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import logic.character.Pokemon;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;

public class DrawingUtility {
	
	public static void drawFightMap(FightMap fightMap){
		for (FightTerrain[] fightTerrains : fightMap.getMap()) {
			for (FightTerrain fightTerrain : fightTerrains) {
				fightTerrain.draw();
			}
		}
	}
	
	public static void drawFightTerrain(FightTerrain fightTerrain){
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(fightTerrain.getType().getImageName()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ScreenComponent.g2.drawImage(img, null, fightTerrain.getX() * 40, fightTerrain.getY() * 40);
	}
	
	public static void drawPokemon(Pokemon pokemon){
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(pokemon.getImageName()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ScreenComponent.g2.drawImage(img, null, pokemon.getCurrentFightTerrain().getX() * 40, pokemon.getCurrentFightTerrain().getY() * 40);
	}

}
