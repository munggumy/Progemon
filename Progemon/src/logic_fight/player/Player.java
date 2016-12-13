package logic_fight.player;

import java.util.ArrayList;
import java.util.Optional;

import audio.SFXUtility;
import graphic.DialogBox;
import graphic.FightHUD;
import item.Bag;
import item.Item;
import item.Items;
import item.Pokeball;
import item.PokeballType;
import javafx.scene.paint.Color;
import logic_fight.CaptureUtility;
import logic_fight.FightPhase;
import logic_fight.character.activeSkill.ActiveSkill;
import logic_fight.character.pokemon.Pokemon;
import logic_fight.filters.AttackFilter;
import logic_fight.filters.MoveFilter;
import logic_fight.terrain.Path;
import manager.GUIFightGameManager;
import utility.Clock;
import utility.exception.AbnormalPhaseOrderException;
import utility.exception.UnknownPhaseException;

public abstract class Player {

	private static final int NO_OF_CAPTURE_WIGGLES = 3;
	private String name;
	private Color color;
	private ArrayList<Pokemon> pokemons;
	private boolean godlike;
	private Bag bag = new Bag();

	protected Optional<Pokemon> nextAttackedPokemon;
	protected Optional<ActiveSkill> nextAttackSkill;
	protected Pokemon captureTarget;
	protected Item itemToUse;
	protected Pokemon itemTargetPokemon;
	protected Optional<Path> nextPath;
	protected boolean isRun;

	private int moveCounter = 1;
	private int moveDelay = 5, moveDelayCounter = 0;
	private int captureCounter = 0;
	private int captureDelay = 60, captureDelayCounter = 0;
	private int tokenx, tokeny;

	private GUIFightGameManager currentFightManager;

	// Constructor

	protected Player(String name) {
		this.name = name;
		color = Color.BLACK; // default
		pokemons = new ArrayList<Pokemon>();
	}

	protected Player(String name, Color color) {
		this.name = name;
		this.color = color;
		pokemons = new ArrayList<Pokemon>();
	}

	protected Player(String name, Pokemon starter_pokemon, Color color) {
		this(name, color);
		starter_pokemon.setOwner(this);
		pokemons.add(starter_pokemon);
	}

	protected Player(String name, Pokemon[] pokemon_set, Color color) {
		this(name, color);
		for (Pokemon pokemon : pokemon_set) {
			pokemon.setOwner(this);
			pokemons.add(pokemon);
		}
	}

	// Run turn

	/** Each turn calls this. Finite-State Machine */
	public final void runTurn(Pokemon pokemon) {
		boolean phaseIsFinished = false;
		try {
			while (currentFightManager.getCurrentPhase() != FightPhase.endPhase) {
				try {
					FightPhase currentPhase = currentFightManager.getCurrentPhase();
					// System.out.println("currentPhase = " + currentPhase);
					switch (currentPhase) {
					case initialPhase:
						nextPath = null;
						nextAttackedPokemon = Optional.empty();
						nextAttackSkill = Optional.empty();
						captureTarget = null;
						itemToUse = null;
						phaseIsFinished = true;
						break;

					case preMovePhase:
						pokemon.getCurrentFightMap().unshadowAllBlocks();
						pokemon.findBlocksAround(pokemon.getMoveRange(), new MoveFilter());
						pokemon.sortPaths();
						pokemon.shadowBlocks();
						tokenx = pokemon.getCurrentFightTerrain().getX();
						tokeny = pokemon.getCurrentFightTerrain().getY();
						phaseIsFinished = true;
						break;
					case inputMovePhase:
						phaseIsFinished = inputNextPath(pokemon);
						break;
					case movePhase:
						phaseIsFinished = move(pokemon);
						break;
					case postMovePhase:
						pokemon.getCurrentFightMap().unshadowAllBlocks();
						phaseIsFinished = true;
						break;
					case preAttackPhase:
						FightHUD.setCurrentPokemon(pokemon);
						pokemon.findBlocksAround(pokemon.getAttackRange(), new AttackFilter());
						pokemon.sortPaths();
						pokemon.shadowBlocks();
						pokemon.getCurrentFightMap().getPokemonsOnMap().stream()
								.filter((Pokemon other) -> !pokemon.getOwner().equals(other.getOwner()))
								.filter((Pokemon other) -> pokemon.getAvaliableFightTerrains()
										.contains(other.getCurrentFightTerrain()))
								.forEach((Pokemon other) -> other.getCurrentFightTerrain().setHighlight(true));
						phaseIsFinished = true;
						break;
					case inputAttackPhase:
						boolean check1 = inputAttackPokemon(pokemon);
						boolean check2 = inputAttackActiveSkill(pokemon);
						phaseIsFinished = check1 && check2;
						break;
					case attackPhase:
						phaseIsFinished = attack(pokemon, nextAttackedPokemon, nextAttackSkill);
						break;
					case postAttackPhase:
						pokemon.getCurrentFightMap().unshadowAllBlocks();
						phaseIsFinished = true;
						break;
					case endPhase:
						phaseIsFinished = true;
						break;
					case preCapturePhase:
						pokemon.getCurrentFightMap().unshadowAllBlocks();
						pokemon.getCurrentFightMap().shadowAllBlocks();
						pokemon.getCurrentFightMap().getPokemonsOnMap().stream()
								.filter(p -> !p.getOwner().equals(pokemon.getOwner()))
								.map(Pokemon::getCurrentFightTerrain).forEach(ft -> ft.setHighlight(true));
						phaseIsFinished = true;
						break;
					case inputCapturePhase:
						phaseIsFinished = inputCapturePokemon(pokemon);
						break;
					case capturePhase:
						phaseIsFinished = capture(pokemon);
						break;
					case postCapturePhase:
						pokemon.getCurrentFightMap().unshadowAllBlocks();
						phaseIsFinished = true;
						break;
					case preItemPhase:
						pokemon.getCurrentFightMap().unshadowAllBlocks();
						pokemon.getCurrentFightMap().getPokemonsOnMap().stream()
								.filter(p -> p.getOwner().equals(pokemon.getOwner()))
								.map(Pokemon::getCurrentFightTerrain).forEach(ft -> ft.setHighlight(true));
						phaseIsFinished = true;
						break;
					case inputItemPhase:
						phaseIsFinished = inputUseItem(pokemon);
						if (itemToUse instanceof Pokeball) {
							phaseIsFinished = false;
							itemToUse = null;
							throw new AbnormalPhaseOrderException(FightPhase.preCapturePhase);
						}
						break;
					case inputItemPokemonPhase:
						phaseIsFinished = inputUseItemPokemon(pokemon);
						break;
					case useItemPhase:
						itemTargetPokemon.useItem(itemToUse);
						phaseIsFinished = true;
						break;
					case postItemPhase:
						pokemon.getCurrentFightMap().unshadowAllBlocks();
						phaseIsFinished = true;
						break;
					case runPhase:
						setRun(true);
						phaseIsFinished = true;
						break;
					default:
						throw new UnknownPhaseException("Unknown Phase in Player.java[" + currentPhase + "].");
					}
					if (phaseIsFinished) {
						currentFightManager.setNextPhase(currentPhase.nextPhase());
					} else {
						currentFightManager.setNextPhase(currentPhase);
					}
				} catch (AbnormalPhaseOrderException ex) {
					currentFightManager.setNextPhase(ex.getNextPhase());
				}
				currentFightManager.nextPhase();
				Clock.tick();
			}
		} catch (UnknownPhaseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			nextAttackedPokemon = null;
			nextAttackSkill = null;
		}

		// pokemonMove(pokemon);
		// pokemonAttack(pokemon);
	}

	protected abstract boolean inputNextPath(Pokemon pokemon) throws AbnormalPhaseOrderException;

	protected final boolean move(Pokemon pokemon) {
		if (moveCounter == nextPath.get().size()) {
			System.out.println("Pokemon " + pokemon.getName() + " moved from (" + tokenx + ", " + tokeny + ") to ("
					+ pokemon.getCurrentFightTerrain().getX() + ", " + pokemon.getCurrentFightTerrain().getY() + ").");
			moveCounter = 1;
			moveDelayCounter = 0;
			return true;
		} else if (moveDelay == moveDelayCounter) {
			if (nextPath.get().get(moveCounter) != pokemon.getCurrentFightTerrain()) {
				pokemon.move(nextPath.get().get(moveCounter));
			} else {
				moveCounter++;
			}
			moveDelayCounter = 0;
			return false;
		} else {
			moveDelayCounter++;
			return false;
		}
	}

	protected abstract boolean inputAttackPokemon(Pokemon pokemon);

	protected abstract boolean inputAttackActiveSkill(Pokemon attackingPokemon) throws AbnormalPhaseOrderException;

	protected final boolean attack(Pokemon attackingPokemon, Optional<Pokemon> other,
			Optional<ActiveSkill> activeSkill) {
		if (other.isPresent() && activeSkill.isPresent()) {
			System.out.println("attacking... ");
			attackingPokemon.attack(other.get(), activeSkill.get());
		}
		return true;
	}

	protected boolean inputCapturePokemon(Pokemon pokemon) throws AbnormalPhaseOrderException {
		return true;
	}

	protected final boolean capture(Pokemon attackingPokemon) {
		if (captureTarget == null) {
			System.err.println("Capture Target not Set!");
			throw new IllegalStateException("captureTarget not set before capture()");
		}
		attackingPokemon.getCurrentFightMap().unshadowAllBlocks();
		captureTarget.getCurrentFightTerrain().setHighlight(true);

		if (captureCounter == 0 && captureDelayCounter == 0) {
			SFXUtility.playSound("pokeball_throw");
			captureDelayCounter++;
			return false;
		} else if (captureCounter == 1 && captureDelayCounter == 0) {
			SFXUtility.playSound("pokeball_suck");
			captureDelayCounter++;
			return false;
		} else if (captureDelay == captureDelayCounter) {
			if (captureCounter <= 1) {
				captureCounter++;
				captureDelayCounter = 0;
				return false;
			}
			if (captureCounter >= NO_OF_CAPTURE_WIGGLES + 2) {
				SFXUtility.playSound("pokeball_captured");
				captureCounter = 0;
				captureDelayCounter = 0;
				attackingPokemon.getCurrentFightMap().removePokemonFromMap(captureTarget);
				captureTarget.getOwner().removePokemon(captureTarget);
				attackingPokemon.getOwner().addPokemon(captureTarget);
				System.out.println("(Player.java:145) CAPTURED!");
				return true;
			}
			captureDelayCounter = 0;
			double modifiedCatchRate = CaptureUtility.getModifiedCatchRate(captureTarget,
					new Pokeball(PokeballType.POKEBALL));
			if (CaptureUtility.isShakeSuccessful(modifiedCatchRate)) {
				SFXUtility.playSound("pokeball_wiggles");
				captureCounter++;
				System.out.println("Player.java Pokeball Wiggles");
				return false;
			} else {
				SFXUtility.playSound("pokemon_out_of_ball");
				captureCounter = 0;
				System.out.println("(Player.java:147) NOT CAPTURED!");
				return true;
			}

		} else {
			System.out.print(".");
			captureDelayCounter++;
			return false;
		}
	}

	protected boolean inputUseItem(Pokemon pokemon) throws AbnormalPhaseOrderException {
		return true;
	}

	protected boolean inputUseItemPokemon(Pokemon pokemon) throws AbnormalPhaseOrderException {
		return true;
	}

	/** Checks if this player loses (All pokemons are dead) */
	public boolean isLose() {
		return pokemons.stream().allMatch(p -> p.isDead()) || pokemons.isEmpty();
	}

	// Getters

	public final String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public final ArrayList<Pokemon> getPokemons() {
		return pokemons;
	}

	public void addPokemon(Pokemon pokemon) {
		pokemon.setOwner(this);
		pokemons.add(pokemon);
	}

	public void removePokemon(Pokemon pokemon) {
		pokemon.setOwner(null);
		pokemons.remove(pokemon);
	}

	public final Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public final boolean isGodlike() {
		return godlike;
	}

	public final void setGodlike(boolean godlike) {
		this.godlike = godlike;
	}

	public final GUIFightGameManager getCurrentFightManager() {
		return currentFightManager;
	}

	public final void setCurrentFightManager(GUIFightGameManager currentFightManager) {
		this.currentFightManager = currentFightManager;
	}

	public final Bag getBag() {
		return bag;
	}

	public void setBag(Bag bag) {
		this.bag = bag;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	public boolean isRun() {
		return isRun;
	}
}
