package graphic;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import utility.GlobalPhase;

public class IRenderableHolder {
	
	private static CopyOnWriteArrayList<IRenderable> worldObjects = new CopyOnWriteArrayList<IRenderable>();
	private static CopyOnWriteArrayList<IRenderable> fightObjects = new CopyOnWriteArrayList<IRenderable>();
	
	public static void addWorldObject(IRenderable object) {
		if(!worldObjects.contains(object)) {
			worldObjects.add(object);
		}
	}
	
	public static void removeWorldObject(IRenderable object){
		if(worldObjects.contains(object)) {
			worldObjects.remove(object);
		}
	}
	
	public static void addFightObject(IRenderable object) {
		if(!fightObjects.contains(object)) {
			fightObjects.add(object);
		}
	}
	
	public static void removeFightObject(IRenderable object){
		if(fightObjects.contains(object)) {
			fightObjects.remove(object);
		}
	}
	
	public static CopyOnWriteArrayList<IRenderable> getObjectsOnScreen() {
		
		if (GlobalPhase.getCurrentPhase() == GlobalPhase.WORLD) {
//			Collections.sort(worldObjects, new Comparator<IRenderable>() {
//
//				@Override
//				public int compare(IRenderable o1, IRenderable o2) {
//					if(o1.getDepth() > o2.getDepth()){
//						return 1;
//					}
//					if(o1.getDepth() < o2.getDepth()){
//						return -1;
//					}
//					return 0;
//				}
//			});
			Collections.sort(worldObjects);
			return worldObjects;
		} else if (GlobalPhase.getCurrentPhase() == GlobalPhase.FIGHT) {
			Collections.sort(fightObjects);
			return fightObjects;
		} else {
			throw new IllegalStateException("Error in getObjectsOnScreen()");
		}
		
		
	}
	
	public static void clearObjects() {
		worldObjects.clear();
	}

}
