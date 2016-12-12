package graphic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import logic_world.terrain.WorldObject;
import utility.GlobalPhase;

public class IRenderableHolder {

	private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private static final Lock readLock = readWriteLock.readLock();
	private static final Lock writeLock = readWriteLock.writeLock();

	private static List<IRenderable> fightObjects = new ArrayList<IRenderable>();
	private static List<IRenderable> worldObjects = new ArrayList<IRenderable>();
	private static List<IRenderable> screenTransitions = new ArrayList<IRenderable>();

	public static void addWorldObject(IRenderable object) {
		writeLock.lock();
		try {
			if (!worldObjects.contains(object)) {
				worldObjects.add(object);
			}
		} finally {
			writeLock.unlock();
		}
	}

	public static void removeWorldObject(IRenderable object) {
		writeLock.lock();
		try {
			if (worldObjects.contains(object)) {
				worldObjects.remove(object);
			}
		} finally {
			writeLock.unlock();
		}
	}

	public static void addFightObject(IRenderable object) {
		writeLock.lock();
		try {
			if (!fightObjects.contains(object)) {
				fightObjects.add(object);
			}
		} finally {
			writeLock.unlock();
		}
	}

	public static void removeFightObject(IRenderable object) {
		writeLock.lock();
		try {
			if (fightObjects.contains(object)) {
				fightObjects.remove(object);
			}
		} finally {
			writeLock.unlock();
		}
	}

	public static void addScreenTransitionObject(IRenderable object) {
		writeLock.lock();
		try {
			if (!screenTransitions.contains(object)) {
				screenTransitions.add(object);
			}
		} finally {
			writeLock.unlock();
		}
	}

	public static void removeScreenTransitionObject(IRenderable object) {
		writeLock.lock();
		try {
			if (screenTransitions.contains(object)) {
				screenTransitions.remove(object);
			}
		} finally {
			writeLock.unlock();
		}
	}

	public static List<IRenderable> getObjectsOnScreen() {
		readLock.lock();
		try {
			if (GlobalPhase.getCurrentPhase() == GlobalPhase.WORLD) {
				Collections.sort(worldObjects);
				return worldObjects;
			} else if (GlobalPhase.getCurrentPhase() == GlobalPhase.FIGHT) {
				Collections.sort(fightObjects);
				return fightObjects;
			} else {
				throw new IllegalStateException("Error in getObjectsOnScreen()");
			}
		} finally {
			readLock.unlock();
		}
	}

	public static List<IRenderable> getScreenTransitions() {
		return screenTransitions;
	}

	public static void setWorldObjects(List<IRenderable> worldObjects) {
		writeLock.lock();
		try {
			IRenderableHolder.worldObjects = new CopyOnWriteArrayList<IRenderable>(worldObjects);
		} finally {
			writeLock.unlock();
		}
	}

	public static void addAllWorldObject(List<WorldObject> worldObjects) {
		writeLock.lock();
		try {
			IRenderableHolder.worldObjects.addAll(worldObjects);
		} finally {
			writeLock.unlock();
		}
	}

	public static void clearObjects() {
		writeLock.lock();
		try {
			worldObjects.clear();
		} finally {
			writeLock.unlock();
		}
	}

	public static Lock getWritelock() {
		return writeLock;
	}

	public static Lock getReadlock() {
		return readLock;
	}
}
