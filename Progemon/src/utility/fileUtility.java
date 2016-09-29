package utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logic.character.ActiveSkill;
import logic.character.Pokemon;
import logic.terrain.FightTerrain;

public class fileUtility {

	private static final String DEFAULT_PATH = "load";
	private static final String DEFAULT_LOAD_POKEMON = DEFAULT_PATH + "/pokemon_list.txt";
	private static final String DEFAULT_LOAD_POKEDEX = DEFAULT_PATH + "/pokedex.txt";
	private static final String DEFAULT_LOAD_FIGHT_MAP = DEFAULT_PATH + "/fight_map.txt";
	private static final String DEFAULT_ACTIVE_SKILLS = DEFAULT_PATH + "/active_skills.txt";
	private static FileReader reader;
	private static Scanner scanner;

	/**
	 * Usage name attack defense moveRange speed hp moveType
	 * 
	 * @throws IOException
	 */
	public static void loadPokemon(String filePath) throws IOException {
		try {
			reader = new FileReader(filePath);
			scanner = new Scanner(reader);
			String line;
			String[] args;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				args = line.split(" ");
				if (args.length != 7) {
					System.out.println("Needs 7 parameters per pokemon!");
					break;
				}
				if (args[0].matches("\\d+")) {
					loadPokemonByID(args);
				} else {
					loadPokemonByName(args);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	public static void loadPokemons() throws IOException {
		loadPokemon(DEFAULT_LOAD_POKEMON);
	}

	private static void loadPokemonByID(String[] args) {
		Pokemon new_pokemon = new Pokemon(args);
		Pokedex.addPokemonToList(new_pokemon);
	}

	private static void loadPokemonByName(String[] args) {
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(Integer.toString(Pokedex.getPokemonID(args[0])));
		temp.addAll(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
		String[] args_to_loadPokemonByID = new String[7];
		temp.toArray(args_to_loadPokemonByID);
		loadPokemonByID(args_to_loadPokemonByID);
	}

	/**
	 * Usage : index name
	 * 
	 * @throws IOException
	 */
	public static void loadPokedex(String filePath) throws IOException {
		try {
			reader = new FileReader(filePath);
			scanner = new Scanner(reader);
			Pattern pattern = Pattern.compile("(\\d+)\\s([\\w\\s]+)");
			Matcher matcher;
			int temp_id;
			String temp_name;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					temp_id = Integer.parseInt(matcher.group(1));
					temp_name = matcher.group(2);
					utility.Pokedex.addPokemonToPokedex(temp_id, temp_name);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}

	public static void loadPokedex() throws IOException {
		loadPokedex(DEFAULT_LOAD_POKEDEX);
	}

	// Load Fight Map
	/** Fightmap fm = new Fightmap(loadFightMap()); */
	public static FightTerrain[][] loadFightMap(String filePath) throws IOException {
		ArrayList<FightTerrain[]> temp_map = new ArrayList<FightTerrain[]>();
		try {
			reader = new FileReader(filePath);
			scanner = new Scanner(reader);
			int widthInBlocks = scanner.nextInt();
			int heightInBlocks = scanner.nextInt();
			for (int y = 0; y < heightInBlocks; y++) {
				temp_map.add(loadFightMapLine(widthInBlocks, y));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (scanner != null) {
				scanner.close();
			}
		}

		if (temp_map.isEmpty()) {
			return null;
		} else {
			return toFightTerrain2DArray(temp_map);
		}

	}

	public static FightTerrain[][] loadFightMap() throws IOException {
		return loadFightMap(DEFAULT_LOAD_FIGHT_MAP);
	}

	// Load Fight Map Private Methods

	private static FightTerrain[] loadFightMapLine(int width, int y) {
		ArrayList<FightTerrain> temp_map_line = new ArrayList<FightTerrain>();
		for (int x = 0; x < width; x++) {
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

	public static void loadActiveSkills(String filepath) throws IOException {
		try {
			reader = new FileReader(filepath);
			scanner = new Scanner(reader);
			Pattern pattern = Pattern.compile("([\\w\\s]+)\\s(\\d+)");
			Matcher matcher;
			String skillName;
			int skillPower;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				matcher = pattern.matcher(line);
				matcher.matches();
				skillName = matcher.group(1);
				skillPower = Integer.parseInt(matcher.group(2));
				ActiveSkill.getActiveSkill(skillName, skillPower);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	public static void loadActiveSkills() throws IOException {
		loadActiveSkills(DEFAULT_ACTIVE_SKILLS);
	}

}
