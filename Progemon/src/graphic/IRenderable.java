package graphic;

public interface IRenderable extends Comparable<IRenderable>{
	
	public void draw();
	public int getDepth();
	public boolean isVisible();
	public void setVisible(boolean visible);
	public void show();
	public void hide();
	
	
	@Override
	default int compareTo(IRenderable other) {
		if(getDepth() > other.getDepth()){
			return 1;
		}
		if(getDepth() < other.getDepth()){
			return -1;
		}
		return 0;
	}

}
