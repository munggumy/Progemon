package logic_fight.character.pokemon;

public enum NonVolatileStatus {
	NORMAL {
		@Override
		public void effect(Pokemon p) {

		}
	},
	FREEZE {
		@Override
		public void effect(Pokemon p) {

		}
	},
	BURN {
		@Override
		public void effect(Pokemon p) {

		}
	},
	POISON {
		@Override
		public void effect(Pokemon p) {

		}
	},
	PARALYZED {
		@Override
		public void effect(Pokemon p) {

		}
	};

	public abstract void effect(Pokemon p);

}
