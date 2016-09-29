package graphic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;

import javax.swing.JComponent;

import logic.terrain.FightMap;
import logic.terrain.FightTerrain;
import main.Main;

public class FightMapComponent extends JComponent{
	
	private FightMap fightMap;
	
	public FightMapComponent(FightMap fightMap) {
		// TODO Auto-generated constructor stub
		this.fightMap = fightMap;
		System.out.println("reach");
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2 = (Graphics2D) g;
		System.out.println("reach");
		g2.fillRect(200, 200, 100, 100);
		/*for (FightTerrain[] fightTerrains : fightMap.getMap()) {
			for (FightTerrain fightTerrain : fightTerrains) {
				BufferedImage img = null;
				try {
					//img = ImageIO.read(new File(fightTerrain.getType().getImageName()));
					img = ImageIO.read(new File("D:\\ik\\CP_CU\\ProgMeth\\Project\\Progemon\\Progemon\\load\\img\\TREE.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				g2.drawImage(img, null, fightTerrain.getX() * 40, fightTerrain.getY() * 40);
			}
		}*/
		fightMap.draw(g2);
	}

}
