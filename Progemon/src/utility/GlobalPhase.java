package utility;

public enum GlobalPhase {
	WORLD, FIGHT;

	private static GlobalPhase currentPhase;

	public static final GlobalPhase getCurrentPhase() {
		return currentPhase;
	}

	public static final void setCurrentPhase(GlobalPhase currentPhase) {
		GlobalPhase.currentPhase = currentPhase;
	}

}
