package graphic;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

public class IRenderableHolder {
	
	private static CopyOnWriteArrayList<IRenderable> worldObjects = new CopyOnWriteArrayList<IRenderable>();
	
	public static void addWorldObjects(IRenderable object) {
		if(!worldObjects.contains(object)) {
			worldObjects.add(object);
		}
	}
	
	public static void removeWorldObjects(IRenderable object){
		if(worldObjects.contains(object)) {
			worldObjects.remove(object);
		}
	}
	
	public static CopyOnWriteArrayList<IRenderable> getObjectsOnScreen() {
		Collections.sort(worldObjects, new Comparator<IRenderable>() {

			@Override
			public int compare(IRenderable o1, IRenderable o2) {
				// TODO Auto-generated method stub
				if(o1.getDepth() > o2.getDepth()){
					return 1;
				}
				if(o1.getDepth() < o2.getDepth()){
					return -1;
				}
				return 0;
			}
		});
		return worldObjects;
	}
	
	public static void clearObjects() {
		worldObjects.clear();
	}

}
