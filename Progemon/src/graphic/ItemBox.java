package graphic;

import java.io.File;
import java.util.ArrayList;

import item.Bag;
import item.Item;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import logic_fight.FightPhase;
import logic_fight.player.HumanPlayer;
import logic_world.player.PlayerCharacter;
import manager.GUIFightGameManager;
import manager.GUIFightGameManager.mouseRegion;
import manager.WorldManager;
import utility.InputUtility;

public class ItemBox implements IRenderable {

	public static final ItemBox instance = new ItemBox();
	public static final int X = 0, Y = 48;
	private int[] scrollBarY = { 0, 0 };
	private ArrayList<Image> tabImages = new ArrayList<>();
	private Image itemButtonImage, runButtonImage;
	private int currentTab;
	private Bag bag;
	private Font font = Font.font("tahoma", 8);
	private int selectedLabel = -1;
	private boolean running = false;

	private boolean visible, mouseOnItemButton, showScrollBar, scrollBarDrag, mouseOnRunButton;

	private ItemBox() {
		// TODO Auto-generated constructor stub
		loadImage();
		visible = false;
		showScrollBar = false;
		scrollBarDrag = false;
		currentTab = 0;
		bag = PlayerCharacter.instance.getBag();
	}

	public void checkInput() {
		selectedLabel = -1;
		int barSize = getScrollBarSize();
		if (!InputUtility.isMouseLeftPress()) {
			scrollBarDrag = false;
		}
		if (scrollBarDrag) {
			scrollBarY[currentTab] += InputUtility.getDragY();
			if (scrollBarY[currentTab] < 0) {
				scrollBarY[currentTab] = 0;
			} else if (scrollBarY[currentTab] + barSize > 109) {
				scrollBarY[currentTab] = 109 - barSize;
			}
		}
		if (!scrollBarDrag) {
			showScrollBar = false;
		}
		if (GUIFightGameManager.instance.getCurrentMouseRegion() != mouseRegion.ITEM) {
			return;
		}
		int mouseX = InputUtility.getMouseX();
		int mouseY = InputUtility.getMouseY();
		mouseOnItemButton = mouseX < 50 && mouseY >= 248;
		mouseOnRunButton = mouseX >= 50 && mouseX < 100 && mouseY >= 248;
		if (mouseOnItemButton && InputUtility.isMouseLeftClick()
				&& (GUIFightGameManager.instance.getCurrentPhase() == FightPhase.inputMovePhase
						|| GUIFightGameManager.instance.getCurrentPhase() == FightPhase.inputItemPhase)
				&& GUIFightGameManager.instance.getCurrentPokemon().getOwner() instanceof HumanPlayer) {
			visible = !visible;
		}
		if (mouseOnRunButton && InputUtility.isMouseLeftClick()
				&& GUIFightGameManager.instance.getCurrentPhase() == FightPhase.inputMovePhase
				&& GUIFightGameManager.instance.getCurrentPokemon().getOwner() instanceof HumanPlayer) {
			running = true;
			System.err.println("ItemBox checkInput -->  Run!!!!");
		}
		if (visible && mouseY < 64 && mouseY >= 48 && InputUtility.isMouseLeftClick()) {
			if (InputUtility.getMouseX() < 50) {
				currentTab = 1;
			} else if (mouseX < 100 && mouseX >= 50) {
				currentTab = 0;
			}
		} else if (mouseY >= 90 && mouseY < 199 && mouseX >= 5 && mouseX < 95) {
			showScrollBar = true;
			if (mouseX >= 90 && InputUtility.isMouseLeftClick()) {
				scrollBarDrag = true;
			} else if (InputUtility.isMouseLeftClick()) {
				selectedLabel = Math.floorDiv((mouseY - 90) + getLabelOffset(), 27);
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
		file = new File("load\\img\\HUD\\runbutton.png");
		runButtonImage = new Image(file.toURI().toString());
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

	public Image getRunButtonImage() {
		return runButtonImage;
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

	public boolean isShowScrollBar() {
		return showScrollBar;
	}

	public int getScrollBarY() {
		return scrollBarY[currentTab];
	}

	public Bag getBag() {
		return bag;
	}

	public int getScrollBarSize() {
		int amount;
		if (currentTab == 0) {
			amount = bag.getPokeballs().size();
		} else {
			amount = bag.getNonPokeballs().size();
		}
		if (amount <= 4) {
			return 109;
		} else {
			return 4 * 109 / amount;
		}
	}

	public int getLabelOffset() {
		int amount;
		if (currentTab == 0) {
			amount = bag.getPokeballs().size();
		} else {
			amount = bag.getNonPokeballs().size();
		}
		return (amount * 27 + 1) * scrollBarY[currentTab] / 109;
	}

	public Item getItemBoxInput() {
		if (selectedLabel != -1) {
			int i = 0;
			if (currentTab == 0) {
				for (Item item : bag.getPokeballs().keySet()) {
					if (i == selectedLabel) {
						visible = false;
						return item;
					}
					i++;
				}
			} else {
				for (Item item : bag.getNonPokeballs().keySet()) {
					if (i == selectedLabel) {
						visible = false;
						return item;
					}
					i++;
				}
			}
		}
		return null;
	}

	public boolean getRunAndSetFalse() {
		try {
			return running;
		} finally {
			running = false;
		}
	}

}
