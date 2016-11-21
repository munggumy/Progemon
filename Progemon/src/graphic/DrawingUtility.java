package graphic;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.sun.javafx.tk.Toolkit;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import logic.character.ActiveSkill;
import logic.character.Pokemon;
import logic.terrain.FightMap;
import logic.terrain.FightTerrain;

public class DrawingUtility {

	private static Image shadow;
	private static Image cursor;
	private static Image highlight;

	private static Image qimg;

	private static Image sign;

	private static GraphicsContext gc;

	public DrawingUtility() {
		// TODO Auto-generated constructor stub
		File sfile = new File("load\\img\\terrain\\shadow20.png");
		shadow = new Image(sfile.toURI().toString());
		File cfile = new File("load\\img\\terrain\\cursur.png");
		cursor = new Image(cfile.toURI().toString());
		File hfile = new File("load\\img\\terrain\\highlight.png");
		highlight = new Image(hfile.toURI().toString());

		File qfile = new File(QueueBox.QUEUE_BOX_PATH);
		qimg = new Image(qfile.toURI().toString());

		File signfile = new File("load\\img\\dialogbox\\Theme1_sign.gif");
		sign = new Image(signfile.toURI().toString());
	}

	public static void drawFightMap(FightMap fightMap) {
		// for (FightTerrain[] fightTerrains : fightMap.getMap()) {
		// for (FightTerrain fightTerrain : fightTerrains) {
		// fightTerrain.draw();
		// }
		// }
		Arrays.asList(fightMap.getMap()).stream().flatMap((FightTerrain[] ft) -> Arrays.asList(ft).stream())
				.forEach(ft -> ft.draw());
		fightMap.getPokemonsOnMap().forEach(p -> p.draw());
//		for (int i = 0; i < fightMap.getPokemonsOnMap().size(); i++) {
//			fightMap.getPokemonsOnMap().get(i).draw();
//		}
	}

	public static void drawFightTerrain(FightTerrain fightTerrain) {
		/*
		 * File file = new File("load\\img\\terrain\\shadow20.png"); Image
		 * shadow = new Image(file.toURI().toString()); file = new
		 * File("load\\img\\terrain\\cursur.png"); Image cursor = new
		 * Image(file.toURI().toString()); file = new
		 * File("load\\img\\terrain\\highlight.png"); Image highlight = new
		 * Image(file.toURI().toString());
		 */

		gc.drawImage(fightTerrain.getTerrainImage(), fightTerrain.getX() * 40, fightTerrain.getY() * 40);
		if (fightTerrain.isShadowed()) {
			gc.drawImage(shadow, fightTerrain.getX() * 40, fightTerrain.getY() * 40);
		}
		if (fightTerrain.isCursor()) {
			gc.drawImage(cursor, fightTerrain.getX() * 40, fightTerrain.getY() * 40);
			fightTerrain.setCursor(false);
		}
		if (fightTerrain.isHighlight()) {
			gc.drawImage(highlight, fightTerrain.getX() * 40, fightTerrain.getY() * 40);
		}
	}

	public static void drawPokemon(Pokemon pokemon) {
		if (pokemon.getCurrentFightTerrain() == null) {
			return;
		}
		int x = pokemon.getCurrentFightTerrain().getX();
		int y = pokemon.getCurrentFightTerrain().getY();
		gc.drawImage(pokemon.getImage(), x * FightTerrain.IMG_SIZE_X, y * FightTerrain.IMG_SIZE_Y);
		/*
		 * Image img = new ImageIcon(pokemon.getImageName()).getImage();
		 * gc.drawImage(img, pokemon.getX() * 40, pokemon.getY() * 40, 40, 40,
		 * null);
		 */
		gc.setFill(Color.RED);
		gc.fillRect(x * 40 + 5, y * 40 + 32, 30, 6);
		gc.setFill(Color.GREEN);
		gc.fillRect(x * 40 + 5, y * 40 + 32, (int) (pokemon.getCurrentHP() * 30 / pokemon.getFullHP()), 6);
	}

	public static void drawDialogBox() {
		/*
		 * File signfile = new File("load\\img\\dialogbox\\Theme1_sign.gif");
		 * Image sign = new Image(signfile.toURI().toString());
		 */
		gc.save();
		gc.beginPath();
		gc.rect(0, 255, 320, 65);
		gc.closePath();

		gc.drawImage(DialogBox.getDialogBoxImage(), DialogBox.getX(), DialogBox.getY());

		gc.setFill(Color.BLACK);
		gc.setFont(DialogBox.getFont());
		double messageHeight = new Text("Test").getLayoutBounds().getHeight();
		gc.fillText(DialogBox.getMessageOnScreen()[0], DialogBox.getX() + 20,
				DialogBox.getY() + 12 + messageHeight - DialogBox.getyShift());
		gc.fillText(DialogBox.getMessageOnScreen()[1], DialogBox.getX() + 20,
				DialogBox.getY() + 37 + messageHeight - DialogBox.getyShift());
		if (DialogBox.getEndLineWidth() != 0) {
			gc.drawImage(sign, DialogBox.getX() + 25 + DialogBox.getEndLineWidth(),
					DialogBox.getY() + DialogBox.getCurrentLine() * 25 + 14);
		}
		gc.restore();
	}

	public static void drawQueueBox() {
		/*
		 * File qfile = new File(QueueBox.QUEUE_BOX_PATH); Image qimg = new
		 * Image(qfile.toURI().toString());
		 */
		Image img;
		gc.drawImage(qimg, QueueBox.getBOX_X(), QueueBox.getBOX_Y());

		gc.save();
		gc.beginPath();
		gc.rect(QueueBox.getOriginX(), QueueBox.getOriginY(), 68, 204);
		gc.clip();
		gc.closePath();

		List<Pokemon> pokemonsOnQueue = QueueBox.getPokemonsOnQueue();
		for (int i = 0; i < pokemonsOnQueue.size(); i++) {
			img = pokemonsOnQueue.get(i).getImage();
			gc.setFill(pokemonsOnQueue.get(i).getOwner().getColor());
			gc.fillRect(QueueBox.getOriginX() + QueueBox.getDelta()[i][0] + 6,
					QueueBox.getOriginY() + QueueBox.getDelta()[i][1] + 2 + i * 40, 6, 36);
			gc.setFill(Color.BLACK);
			gc.setFont(DialogBox.getFont());
			double messageHeight = new Text("LV.").getLayoutBounds().getHeight();
			gc.fillText("Lv." + pokemonsOnQueue.get(i).getLevel(),
					QueueBox.getOriginX() + QueueBox.getDelta()[i][0] + 24,
					QueueBox.getOriginY() + QueueBox.getDelta()[i][1] + 15 + i * 40 + messageHeight);

			// pixel error test
			// gc.setColor(Color.BLUE);
			// gc.fillRect(326, 450, 68, 40);
			// gc.setColor(Color.RED);
			// gc.fillRect(326, 490, 68, 40);
			// gc.setColor(Color.GREEN);
			// gc.fillRect(326, 530, 68, 40);
			// gc.setColor(Color.BLACK);
			// gc.drawRect(326, 450, 68, 40);
			// gc.drawRect(326, 490, 68, 40);
			// gc.drawRect(326, 530, 68, 40);

			gc.drawImage(img, QueueBox.getOriginX() + QueueBox.getDelta()[i][0],
					QueueBox.getOriginY() + QueueBox.getDelta()[i][1] + i * 40);
		}
		gc.restore();

		// gif test
		/*
		 * Image img2 = new
		 * ImageIcon("load\\img\\pokemon\\Dratini.gif").getImage();
		 * gc.drawImage(img2, 0, 0, 40, 40, null);
		 */
	}

	public static void drawSkill(ActiveSkill skill) {
		int ax = skill.getAttackTerrain().getX();
		int ay = skill.getAttackTerrain().getY();
		int tx = skill.getTargetTerrain().getX();
		int ty = skill.getTargetTerrain().getY();
		Image img = skill.getCurrentImage();

		gc.save();
		gc.translate(ax * 40 + 20, ay * 40 + 20);

		// test skill
		/*
		 * ax = 2; ay = 0; tx = 0; ty = 2;
		 */

		double distance = Math.sqrt((ax - tx) * (ax - tx) + (ay - ty) * (ay - ty));
		double angle = 0;
		angle = Math.acos((tx - ax) / distance) / Math.PI * 180;
		if (ay - ty < 0) {
			angle = -angle;
		}
		if (ax - tx > 0) {
			img = verticalFlip(img);
		}

		gc.rotate(-angle);
		gc.drawImage(img, -20, -20, distance * 40 + 40, 40);
		gc.restore();
	}

	public static double computeStringWidth(String str, Font font) {
		if (str.length() == 0 || gc == null) {
			return 0;
		} else {
			return Toolkit.getToolkit().getFontLoader().computeStringWidth(str, font);
		}
	}

	public static void setGC(GraphicsContext gc) {
		DrawingUtility.gc = gc;
	}

	public static GraphicsContext getGC() {
		return gc;
	}

	public static Image verticalFlip(Image img) {
		int height = (int) img.getHeight();
		int width = (int) img.getWidth();
		PixelReader pr = img.getPixelReader();
		WritableImage wimg = new WritableImage(width, height);
		PixelWriter pw = wimg.getPixelWriter();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pw.setColor(x, height - 1 - y, pr.getColor(x, y));
			}
		}
		return wimg;
	}

	public static Image horizontalFlip(Image img) {
		int height = (int) img.getHeight();
		int width = (int) img.getWidth();
		PixelReader pr = img.getPixelReader();
		WritableImage wimg = new WritableImage(width, height);
		PixelWriter pw = wimg.getPixelWriter();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pw.setColor(width - 1 - x, y, pr.getColor(x, y));
			}
		}
		return wimg;
	}

}