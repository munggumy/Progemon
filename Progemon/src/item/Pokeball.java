package item;

public class Pokeball extends Item {

	private double catchBonus;

	private Pokeball(String name, int catchBonus) {
		super(name);
		this.catchBonus = catchBonus;
	}

	public Pokeball(PokeballType pt) {
		super(" ");
		switch (pt) {
		case POKEBALL:
			this.setName("Pokeball");
			this.catchBonus = 1.0;
			break;
		case GREAT_BALL:
			this.setName("Pokeball");
			this.catchBonus = 1.5;
			break;
		case ULTRA_BALL:
			this.setName("Pokeball");
			this.catchBonus = 2.0;
			break;
		case MASTER_BALL:
			this.setName("Pokeball");
			this.catchBonus = Double.POSITIVE_INFINITY;
			break;
		default:
			throw new RuntimeException("Unknown Pokeball");
		}
	}

	public double getCatchBonus() {
		return catchBonus;
	}

}