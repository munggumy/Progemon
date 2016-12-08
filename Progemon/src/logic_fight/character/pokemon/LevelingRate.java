package logic_fight.character.pokemon;

public enum LevelingRate {
	FAST {
		@Override
		public int getRequiredExperience(int level) {
			return 4 * (level * level * level) / 5;
		}
	},
	MEDIUM_FAST {
		@Override
		public int getRequiredExperience(int level) {
			return level * level * level;
		}
	},
	MEDIUM_SLOW {
		@Override
		public int getRequiredExperience(int level) {
			return (6 * level * level * level / 5) - (15 * level * level) + (100 * level) - 140;
		}
	},
	SLOW {
		@Override
		public int getRequiredExperience(int level) {
			return (5 * level * level * level / 4);
		}
	};

	public abstract int getRequiredExperience(int level);
}
