package graphic;

public interface IRenderable {
	
	public void draw();
	public int getDepth();
	public boolean isVisible();
	public void setVisible(boolean visible);
	public void show();
	public void hide();

}
