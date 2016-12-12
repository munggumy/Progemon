package logic_fight.character;

public enum Element {
	NORMAL, FIRE, WATER, GRASS, ELECTRIC, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, DRAGON, DARK, STEEL;

	
	
	/** SW = StrengthWeakness. N = Normal, S = Strong, W = Weak, Z = Zero */
	public enum SW {
		N(1), S(2), W(0.5), Z(0);
		private double factor;

		SW(double factor) {
			
			this.factor = factor;
		}

		public double getFactor() {
			return factor;
		}
	}
	
}
