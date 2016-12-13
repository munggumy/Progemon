package graphic;

import java.io.File;
import java.util.ArrayList;

import item.Bag;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import utility.InputUtility;

public class ItemBox implements IRenderable{
	
	public static final ItemBox instance = new ItemBox();
	public static final int X = 0, Y = 48;
	private ArrayList<Image> tabImages = new ArrayList<>();
	private Image itemButtonImage;
	private int currentTab;
	private Bag bag;
	private Font font = Font.font("tahoma", 8);
	
	private boolean visible, mouseOnButton;
	
	private ItemBox() {
		// TODO Auto-generated constructor stub
		loadImage();
		visible = false;
		currentTab = 0;
	}
	
	public void checkInput() {
		mouseOnButton = InputUtility.getMouseX() < 50 && InputUtility.getMouseY() < 288 && InputUtility.getMouseY() >= 248;
		if (mouseOnButton && InputUtility.isMouseLeftClick()) {
			visible = !visible;
		}
		if (visible && InputUtility.getMouseY() < 64 && InputUtility.getMouseY() >= 48 && InputUtility.isMouseLeftClick()) {
			if (InputUtility.getMouseX() < 50) {
				currentTab = 1;
			}
			else if (InputUtility.getMouseX() < 100 && InputUtility.getMouseX() >= 50) {
				currentTab = 0;
			}
		}
	}
	
	public void loadImage() {
		File file = new File("load\\img\\HUD\\ballbox.png");
		tabImages.add(new Image(file.toURI().toString()));
		file = new File("load\\img\\HUD\\itembox.png");
		tabImages.add(new Image(file.toURI().toString()));
		file = new File("load\\img\\HUD\\itembutton.png");
		itemButtonImage = new Image(file.toURI().toString());
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		DrawingUtility.drawItemBox(this);
	}

	@Override
	public int getDepth() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		this.visible = visible;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		visible = true;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		visible = false;
	}
	
	public Image getItemButtonImage() {
		return itemButtonImage;
	}
	
	public Image getTabImage() {
		return tabImages.get(currentTab);
	}
	
	public int getCurrentTab() {
		return currentTab;
	}
	
	public Font getFont() {
		return font;
	}

}