package utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import logic_fight.character.Element;
import logic_fight.character.SWTable;
import logic_fight.character.activeSkill.ActiveSkill;
import logic_fight.character.activeSkill.AreaType;
import logic_fight.character.activeSkill.GraphicType;
import logic_fight.character.activeSkill.SkillEffect;
import logic_fight.character.pokemon.LevelingRate;
import logic_fight.character.pokemon.Pokemon;
import logic_fight.character.pokemon.PokemonTemplate;
import logic_fight.character.pokemon.Status;
import logic_fight.character.pokemon.Pokemon.MoveType;
import logic_fight.terrain.FightTerrain;
import utility.exception.FileWrongFormatException;

public class FileUtility {

	private static final String DEFAULT_PATH = "load";
	private static final String DEFAULT_LOAD_POKEMON = DEFAULT_PATH + "/pokemon_list.csv";
	private static final String DEFAULT_LOAD_POKEDEX = DEFAULT_PATH + "/pokedex.csv";
	private static final String DEFAULT_LOAD_FIGHT_MAP = DEFAULT_PATH + "/fight_map.txt";
	private static final String DEFAULT_ACTIVE_SKILLS = DEFAULT_PATH + "/active_skills.csv";
	private static final String DEFAULT_SW_TABLE = DEFAULT_PATH + "/strengthWeaknessTable.csv";

	public static void loadAllDefaults() {
		loadActiveSkills();
		loadPokedex();
		loadPokemons();
		loadStrengthWeaknessTable();
	}

	/**
	 * Usage name/ID attack defense speed hp moveRange attackRange moveType
	 * 
	 * @throws IOException
	 */
	public static void loadPokemons(String filePath) {
		String primaryDelimiter = ",\\s*", secondaryDelimiter = "/";
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filePath)))) {
			Pattern pattern = Pattern.compile(String.join(primaryDelimiter, "(\\d+|\\w+)", "(?<attack>\\d+(\\.\\d*)?)",
					"(?<defense>\\d+(\\.\\d*)?)", "(?<speed>\\d+(\\.\\d*)?)", "(?<hp>\\d+(\\.\\d*)?)",
					"(?<moveRange>\\d+)", "(?<attackRange>\\d+)", "(?<moveType>\\w+)",
					"(?<attackSkills>[a-zA-z" + secondaryDelimiter + " ]+)",
					"(?<elements>[a-zA-Z" + secondaryDelimiter + " ]+)", "(?<levelingType>[\\w_]+)",
					"(?<expYield>\\d+)"));
			Matcher matcher;
			while (scanner.hasNextLine()) {
				matcher = pattern.matcher(scanner.nextLine());
				if (!matcher.find()) {
					System.err.println(pattern.toString());
					System.err.println(scanner.nextLine());
					throw new FileWrongFormatException("Needs at least 8 parameters per pokemon!");
				}
				String[] args = { matcher.group(1), matcher.group("attack"), matcher.group("defense"),
						matcher.group("speed"), matcher.group("hp"), matcher.group("moveRange"),
						matcher.group("attackRange"), matcher.group("moveType"), matcher.group("levelingType"),
						matcher.group("expYield") };
				if (matcher.group(1).matches("\\d+")) {
					loadPokemonByID(args, matcher.group("attackSkills").split(secondaryDelimiter),
							matcher.group("elements").split(secondaryDelimiter));
				} else if (matcher.group(1).matches("\\w+")) {
					args[0] = Integer.toString(Pokedex.getPokemonID(matcher.group(1)));
					loadPokemonByID(args, matcher.group("attackSkills").split(secondaryDelimiter),
							matcher.group("elements").split(secondaryDelimiter));
				} else {
					throw new FileWrongFormatException("Unknown File Format");
				}
			}
		} catch (FileNotFoundException | FileWrongFormatException e) {
			e.printStackTrace();
		}
	}

	public static void loadPokemons() {
		loadPokemons(DEFAULT_LOAD_POKEMON);
	}

	private static void loadPokemonByID(String[] args, String[] activeSkills, String[] elements) {
		if (activeSkills.length == 0 || activeSkills.length > Pokemon.MAX_ACTIVE_SKILLS || elements.length == 0
				|| elements.length > 2) {
			throw new IllegalArgumentException("Illegal Argument in loadPokemonByID() : activeSkills.length="
					+ activeSkills.length + ", elements.length=" + elements.length);
		}
		PokemonTemplate np = new PokemonTemplate();
		np.setID(Integer.parseInt(args[0]));
		np.setBaseAttack(Integer.parseInt(args[1]));
		np.setBaseDefense(Integer.parseInt(args[2]));
		np.setBaseSpeed(Integer.parseInt(args[3]));
		np.setBaseHP(Integer.parseInt(args[4]));
		np.setMoveRange(Integer.parseInt(args[5]));
		np.setAttackRange(Integer.parseInt(args[6]));
		np.setMoveType(MoveType.valueOf(args[7]));
		np.setLevelingRate(LevelingRate.valueOf(args[8]));
		np.setExpYield(Integer.parseInt(args[9]));
		np.setPrimaryElement(Element.valueOf(elements[0].trim()));
		if (elements.length == 2) {
			np.setSecondaryElement(Element.valueOf(elements[1].trim()));
		} else {
			np.setSecondaryElement(null);
		}

		Pokemon new_pokemon = new Pokemon(np, 5);
		Stream.of(activeSkills).filter(as -> !as.isEmpty()).forEachOrdered(as -> new_pokemon.addActiveSkill(as.trim()));
		new_pokemon.loadImage(new_pokemon.getName() + ".png");
		Pokedex.addPokemonToList(new_pokemon);
		System.out.println("added pokemon " + new_pokemon.getName());
	}

	// private static void loadPokemonByName(String[] args, String[]
	// activeSkills, String[] elements) {
	// ArrayList<String> temp = new ArrayList<String>();
	// temp.add(Integer.toString(Pokedex.getPokemonID(args[0])));
	// temp.addAll(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
	// String[] args_to_loadPokemonByID = new String[7];
	// temp.toArray(args_to_loadPokemonByID);
	// loadPokemonByID(args_to_loadPokemonByID, activeSkills, elements);
	// }

	/**
	 * Usage : index name
	 * 
	 * @throws IOException
	 */
	public static void loadPokedex(String filePath) {
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filePath)))) {
			Pattern pattern = Pattern.compile("(\\d+),([\\w\\s]+)");
			Matcher matcher;
			int temp_id;
			String temp_name;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					temp_id = Integer.parseInt(matcher.group(1));
					temp_name = matcher.group(2);
					Pokedex.addPokemonToPokedex(temp_id, temp_name);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void loadPokedex() {
		loadPokedex(DEFAULT_LOAD_POKEDEX);
	}

	// Load Fight Map
	/** Fightmap fm = new Fightmap(loadFightMap()); */
	public static FightTerrain[][] loadFightMap(String filePath) {
		ArrayList<FightTerrain[]> temp_map = new ArrayList<FightTerrain[]>();
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filePath)))) {
			int widthInBlocks = scanner.nextInt();
			int heightInBlocks = scanner.nextInt();
			for (short y = 0; y < heightInBlocks; y++) {
				temp_map.add(loadFightMapLine(widthInBlocks, y, scanner));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (temp_map.isEmpty()) {
			return null;
		} else {
			return toFightTerrain2DArray(temp_map);
		}

	}

	public static FightTerrain[][] loadFightMap() {
		return loadFightMap(DEFAULT_LOAD_FIGHT_MAP);
	}

	// Load Fight Map Private Methods

	private static FightTerrain[] loadFightMapLine(int width, short y, Scanner scanner) {
		ArrayList<FightTerrain> temp_map_line = new ArrayList<FightTerrain>();
		for (short x = 0; x < width; x++) {
			temp_map_line.add(new FightTerrain(x, y, FightTerrain.toFightTerrainType(scanner.next())));
		}
		return toFightTerrainArray(temp_map_line);
	}

	private static FightTerrain[] toFightTerrainArray(List<FightTerrain> fightTerrains) {
		FightTerrain[] out = new FightTerrain[fightTerrains.size()];
		for (int i = 0; i < out.length; i++) {
			out[i] = fightTerrains.get(i);
		}
		return out;
	}

	private static FightTerrain[][] toFightTerrain2DArray(List<FightTerrain[]> fightTerrains2D) {
		FightTerrain[][] out = new FightTerrain[fightTerrains2D.size()][fightTerrains2D.get(0).length];
		for (int i = 0; i < out.length; i++) {
			out[i] = fightTerrains2D.get(i);
		}
		return out;
	}

	// Load Active Skills

	public static void loadActiveSkills(String filePath) {
		String delimiter = "\\s*,\\s*";
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filePath)))) {
			Pattern pattern = Pattern.compile(String.join(delimiter, "(?<skillName>[\\w\\s]+)", "(?<power>\\d+)",
					"(?<element>\\w*)", "(?<graphicType>\\w*)", "(?<areaType>\\w*)", "(?<skillEffect>.*)"));
			Matcher matcher;
			String skillName;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.matches("^#+.*")) { // comment line ##hey hey heys
					continue;
				}
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					skillName = matcher.group(1);
					ActiveSkill activeSkill = ActiveSkill.getActiveSkill(skillName, Integer.parseInt(matcher.group(2)),
							false);
					try {
						activeSkill.setElement(Element.valueOf(matcher.group(3).toUpperCase()));
					} catch (IllegalArgumentException e) {
						System.err.println(skillName + " : Element not set");
						activeSkill.setElement(Element.NORMAL);
					}
					try {
						activeSkill.setGraphicType(GraphicType.valueOf(matcher.group(4).toUpperCase()));
						activeSkill.setAreaType(AreaType.valueOf(matcher.group(5).toUpperCase()));
					} catch (IllegalArgumentException e) {
						System.err.println(skillName + " : Properties not set");
						activeSkill.setGraphicType(null);
						activeSkill.setAreaType(null);
					}
					activeSkill.setOnAttack(decodeSkillEffect(matcher.group(6)).orElse(SkillEffect.normal));
				} else {
					System.err.println("Unmatched Pattern \"" + line + "\" in loadActiveSkills().");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// paralyzed(10%)/freeze(10%)
	private static Optional<SkillEffect> decodeSkillEffect(String input) {
		String delimiter = "/";
		String[] encodedSkills = input.split(delimiter);
		Pattern pattern = Pattern.compile("(?<effectName>\\w+)\\((?<percentChance>\\d+)%\\)");
		Matcher matcher;
		SkillEffect out = null, temp;
		for (String encodedSkill : encodedSkills) {
			matcher = pattern.matcher(encodedSkill);
			if (matcher.find()) {
				temp = effectDictionary.get(matcher.group("effectName"));
				int percent = Integer.parseInt(matcher.group("percentChance"));
				if (percent < 0 || percent > 100) {
					System.err.println("decodeSkillEffect() : invalid percent = " + percent);
					continue;
				}
				temp = SkillEffect.modifyChance(temp, percent);
				if (out == null) {
					out = temp;
				} else {
					out = out.andThen(temp);
				}
			}
		}
		return Optional.ofNullable(out);
	}

	public static Map<String, SkillEffect> effectDictionary = new HashMap<String, SkillEffect>();
	static {
		effectDictionary.put("", SkillEffect.normal);
		effectDictionary.put("paralysis", SkillEffect.status(Status.PARALYZED));
		effectDictionary.put("freeze", SkillEffect.status(Status.FREEZE));
		effectDictionary.put("burn", SkillEffect.status(Status.BURN));
		effectDictionary.put("poison", SkillEffect.status(Status.POISON));
	}

	public static void loadActiveSkills() {
		loadActiveSkills(DEFAULT_ACTIVE_SKILLS);
	}

	public static void loadStrengthWeaknessTable(String filePath) {
		final int NUM_ELEMENTS = Element.values().length;
		// Element.SWFactor = new SW[NUM_ELEMENTS][NUM_ELEMENTS];
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filePath)))) {
			for (Element attacker : Element.values()) {
				String[] line = scanner.nextLine().split("\\s*,\\s*");
				int j = 0;
				for (Element attacked : Element.values()) {
					if (line[j].equals("")) {
						SWTable.instance.setSW(attacker, attacked, Element.SW.N);
					} else {
						SWTable.instance.setSW(attacker, attacked, Element.SW.valueOf(line[j]));
					}
					j++;
				}
			}
		} catch (FileNotFoundException | NullPointerException e) {
			e.printStackTrace();
		}
		System.out.println("::SWTable::");
		System.out.println(SWTable.instance.getFactor(Element.WATER, Element.FIRE));
	}

	public static void loadStrengthWeaknessTable() {
		loadStrengthWeaknessTable(DEFAULT_SW_TABLE);
	}

}
