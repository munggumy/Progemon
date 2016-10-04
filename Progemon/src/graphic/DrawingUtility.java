package graphic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import logic.character.Pokemon;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;

public class DrawingUtility {

	public static void drawFightMap(FightMap fightMap) {
		for (FightTerrain[] fightTerrains : fightMap.getMap()) {
			for (FightTerrain fightTerrain : fightTerrains) {
				fightTerrain.draw();
			}
		}
		for (int i = 0; i < fightMap.getPokemonsOnMap().size(); i++) {
			fightMap.getPokemonsOnMap().get(i).draw();
		}
	}

	public static void drawFightTerrain(FightTerrain fightTerrain) {
		BufferedImage img = null;
		BufferedImage shadow = null;
		BufferedImage cursur = null;
		try {
			img = ImageIO.read(new File(fightTerrain.getType().getImageName()));
			shadow = ImageIO.read(new File("load\\img\\terrain\\shadow20.png"));
			cursur = ImageIO.read(new File("load\\img\\terrain\\cursur.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ScreenComponent.g2.drawImage(img, null, fightTerrain.getX() * 40, fightTerrain.getY() * 40);
		if(fightTerrain.isShadowed()){
			ScreenComponent.g2.drawImage(shadow, null, fightTerrain.getX() * 40, fightTerrain.getY() * 40);
		}
		if(fightTerrain.isCursur()){
			ScreenComponent.g2.drawImage(cursur, null, fightTerrain.getX() * 40, fightTerrain.getY() * 40);
			fightTerrain.setCursur(false);
		}
	}

	public static void drawPokemon(Pokemon pokemon) {
		if (pokemon.getCurrentFightTerrain() == null) {
			return;
		}
		int x = pokemon.getCurrentFightTerrain().getX();
		int y = pokemon.getCurrentFightTerrain().getY();
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(pokemon.getImageName()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ScreenComponent.g2.drawImage(img, null, x * 40, y * 40);
		/*
		 * Image img = new ImageIcon(pokemon.getImageName()).getImage();
		 * ScreenComponent.g2.drawImage(img, pokemon.getX() * 40, pokemon.getY()
		 * * 40, 40, 40, null);
		 */
		ScreenComponent.g2.setColor(Color.RED);
		ScreenComponent.g2.fillRect(x * 40 + 5, y * 40 + 32, 30, 6);
		ScreenComponent.g2.setColor(Color.GREEN);
		ScreenComponent.g2.fillRect(x * 40 + 5, y * 40 + 32, (int) (pokemon.getCurrentHP() * 30 / pokemon.getFullHP()),
				6);
	}

	public static void drawDialogBox() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(DialogBox.DIALOG_BOX_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ScreenComponent.g2.drawImage(img, null, DialogBox.getX(), DialogBox.getY());
		ScreenComponent.g2.clipRect(0, 260, 320, 60);
		ScreenComponent.g2.setColor(Color.BLACK);
		ScreenComponent.g2.setFont(DialogBox.getFont());
		int messageHeight = ScreenComponent.g2.getFontMetrics(DialogBox.getFont()).getHeight();
		ScreenComponent.g2.drawString(DialogBox.getMessageOnScreen()[0], DialogBox.getX() + 20,
				DialogBox.getY() + 12 + messageHeight - DialogBox.getyShift());
		ScreenComponent.g2.drawString(DialogBox.getMessageOnScreen()[1], DialogBox.getX() + 20,
				DialogBox.getY() + 37 + messageHeight - DialogBox.getyShift());
		ScreenComponent.g2.setClip(null);
	}

	public static void drawQueueBox() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(QueueBox.QUEUE_BOX_PATH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ScreenComponent.g2.drawImage(img, null, QueueBox.getBOX_X(), QueueBox.getBOX_Y());
		ScreenComponent.g2.clipRect(QueueBox.getOriginX(), QueueBox.getOriginY(), 68, 204);
		List<Pokemon> pokemonsOnQueue = QueueBox.getPokemonsOnQueue();
		for (int i = 0; i < pokemonsOnQueue.size(); i++) {
			try {
				img = ImageIO.read(new File(pokemonsOnQueue.get(i).getImageName()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (pokemonsOnQueue.get(i).getOwner().getName() == "AI 1") {
				ScreenComponent.g2.setColor(Color.BLUE);
			} else {
				ScreenComponent.g2.setColor(Color.RED);
			}
			ScreenComponent.g2.fillRect(QueueBox.getOriginX() + QueueBox.getDelta()[i][0] + 6,
					QueueBox.getOriginY() + QueueBox.getDelta()[i][1] + 2 + i * 40, 6, 36);
			ScreenComponent.g2.setColor(Color.BLACK);
			ScreenComponent.g2.setFont(DialogBox.getFont());
			int messageHeight = ScreenComponent.g2.getFontMetrics(DialogBox.getFont()).getHeight();
			ScreenComponent.g2.drawString("Lv." + pokemonsOnQueue.get(i).getLevel(),
					QueueBox.getOriginX() + QueueBox.getDelta()[i][0] + 24,
					QueueBox.getOriginY() + QueueBox.getDelta()[i][1] + 15 + i * 40 + messageHeight);

			// pixel error test
			// ScreenComponent.g2.setColor(Color.BLUE);
			// ScreenComponent.g2.fillRect(326, 450, 68, 40);
			// ScreenComponent.g2.setColor(Color.RED);
			// ScreenComponent.g2.fillRect(326, 490, 68, 40);
			// ScreenComponent.g2.setColor(Color.GREEN);
			// ScreenComponent.g2.fillRect(326, 530, 68, 40);
			// ScreenComponent.g2.setColor(Color.BLACK);
			// ScreenComponent.g2.drawRect(326, 450, 68, 40);
			// ScreenComponent.g2.drawRect(326, 490, 68, 40);
			// ScreenComponent.g2.drawRect(326, 530, 68, 40);

			ScreenComponent.g2.drawImage(img, null, QueueBox.getOriginX() + QueueBox.getDelta()[i][0],
					QueueBox.getOriginY() + QueueBox.getDelta()[i][1] + i * 40);
		}
		ScreenComponent.g2.setClip(null);

		// gif test
		/*
		 * Image img2 = new
		 * ImageIcon("load\\img\\pokemon\\Dratini.gif").getImage();
		 * ScreenComponent.g2.drawImage(img2, 0, 0, 40, 40, null);
		 */
	}
}
