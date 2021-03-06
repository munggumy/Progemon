package logic_fight.terrain;

import java.io.File;

import graphic.DrawingUtility;
import graphic.IRenderable;
import graphic.IRenderableHolder;
import javafx.scene.image.Image;

/** FightTerrain */
public class FightTerrain implements IRenderable {
	
	public final static int IMG_SIZE_X = 40, IMG_SIZE_Y = 40;
	private short x, y;
	private boolean isShadowed, isCursor, isPathSign, isHighlight;
	private TerrainType type;
	private Image terrainImage = null;
	private boolean visible = true;

	public static enum TerrainType {
		GRASS, ROCK, WATER, TREE, GROUND;
		private int moveCost;

		private TerrainType(int moveCost) {
			this.moveCost = moveCost;
		}

		private TerrainType() {
			this(1);
		}

		public int getMoveCost() {
			return this.moveCost;
		}

		public String getDefaultImageName() {
			return "load/img/terrain/" + this.toString() + ".png";
		}
	}

	public FightTerrain(short x, short y, TerrainType type) {
		this.x = x;
		this.y = y;
		this.type = type;
		isShadowed = false;
		isCursor = false;
		loadDefaultTerrainImage();
	}

	@Override
	public String toString() {
		return "FightTerrain [x=" + x + ", y=" + y + ", type=" + type + "]";
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public final boolean isShadowed() {
		return isShadowed;
	}

	public final void setShadowed(boolean isShadowed) {
		this.isShadowed = isShadowed;
	}

	public boolean isCursor() {
		return isCursor;
	}

	public void setCursor(boolean isCursor) {
		this.isCursor = isCursor;
	}

	public boolean isPathSign() {
		return isPathSign;
	}

	public void setPathSign(boolean isPathSign) {
		this.isPathSign = isPathSign;
	}

	public boolean isHighlight() {
		return isHighlight;
	}

	public void setHighlight(boolean isHighlight) {
		this.isHighlight = isHighlight;
	}

	public final TerrainType getType() {
		return type;
	}

	public static TerrainType toFightTerrainType(String fightTerrainString) {
		for (TerrainType tt : TerrainType.values()) {
			if (fightTerrainString.equalsIgnoreCase(tt.toString())) {
				return tt;
			}
		}
		return null;
	}

	public void draw() {
		DrawingUtility.drawFightTerrain(this);
	}

	@Override
	public int getDepth() {
		// TODO Auto-generated method stub
		return 0;
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
	public void hide() {
		// TODO Auto-generated method stub
		visible = false;
		IRenderableHolder.removeWorldObject(this);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		IRenderableHolder.addWorldObject(this);
		visible = true;
	}

	public final Image getTerrainImage() {
		return terrainImage;
	}

	public final void setTerrainImage(Image terrainImage) {
		this.terrainImage = terrainImage;
	}

	public final void loadDefaultTerrainImage() {
		File file = new File(type.getDefaultImageName());
		terrainImage = new Image(file.toURI().toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isShadowed ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FightTerrain other = (FightTerrain) obj;
		if (isShadowed != other.isShadowed)
			return false;
		if (type != other.type)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

}
