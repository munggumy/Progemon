package logic_world.terrain;

public enum WildRareness {
	VERY_COMMON(10), COMMON(8.5), SEMI_RARE(6.75), RARE(3.33), VERY_RARE(1.25);

	private double rate;

	private WildRareness(double rate) {
		this.rate = rate;
	}

	public double getRate() {
		return rate;
	}

}
