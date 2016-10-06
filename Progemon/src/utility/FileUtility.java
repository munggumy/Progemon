package utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

public class FileUtility {

	private static final String DEFAULT_PATH = "load";
	private static final String DEFAULT_LOAD_POKEMON = DEFAULT_PATH + "/pokemon_list.txt";
	private static final String DEFAULT_LOAD_POKEDEX = DEFAULT_PATH + "/pokedex.txt";
	private static final String DEFAULT_LOAD_FIGHT_MAP = DEFAULT_PATH + "/fight_map.txt";
	private static final String DEFAULT_ACTIVE_SKILLS = DEFAULT_PATH + "/active_skills.txt";
	private static FileReader reader;
	private static BufferedReader bufReader;
	private static Scanner scanner;

	public static void loadAllDefaults() throws IOException {
		loadActiveSkills();
		loadPokedex();
		loadPokemons();
	}

	/**
	 * Usage name/ID attack defense speed hp moveRange attackRange moveType
	 * 
	 * @throws IOException
	 */
	public static void loadPokemons(String filePath) throws IOException {
		try {
			reader = new FileReader(filePath);
			bufReader = new BufferedReader(reader);
			scanner = new Scanner(bufReader);
			Pattern pattern = Pattern.compile(
					/*
					 * id/name attack defence speed hp mRange aRange mType
					 * attackMoves
					 */
					"(\\d+|\\w+)\\s(\\d+(\\.\\d*)?)\\s(\\d+(\\.\\d*)?)\\s(\\d+(\\.\\d*)?)\\s(\\d+(\\.\\d*)?)\\s(\\d+)\\s(\\d+)\\s(\\w+)\\s?([\\w ,]*)");
			Matcher matcher = null;
			while (scanner.hasNextLine()) {
				matcher = pattern.matcher(scanner.nextLine());
				if (matcher.find()) {
					String[] args = { matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(6),
							matcher.group(8), matcher.group(10), matcher.group(11), matcher.group(12) };
					if (matcher.group(1).matches("\\d+")) {
						loadPokemonByID(args, matcher.group(13).split(",[ ]?"));
					} else if (matcher.group(1).matches("\\w+")) {
						loadPokemonByName(args, matcher.group(13).split(",[ ]?"));
					} else {
						System.err.println("FileUtility.loadPokemons() : Unknown Format");
					}

				} else {
					System.err.println("FileUtility.loadPokemons() : Needs at least 8 Parameters per pokemon!");
				}
				// args = line.split(" ");
				// if (args.length != 7) {
				// System.out.println("Needs 7 parameters per pokemon!");
				// break;
				// }
				// if (args[0].matches("\\d+")) {
				// loadPokemonByID(args);
				// } else {
				// loadPokemonByName(args);
				// }
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (scanner != null) {
				scanner.close();
			}
			if (bufReader != null){
				bufReader.close();
			}
		}
	}

	public static void loadPokemons() throws IOException {
		loadPokemons(DEFAULT_LOAD_POKEMON);
	}

	private static void loadPokemonByID(String[] args, String[] activeSkills) {
		Pokemon new_pokemon = new Pokemon(args);
		for (String activeSkillName : activeSkills) {
			if (!activeSkillName.isEmpty()) {
				new_pokemon.addActiveSkill(activeSkillName);
			}
		}
		Pokedex.addPokemonToList(new_pokemon);
	}

	private static void loadPokemonByName(String[] args, String[] activeSkills) {
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(Integer.toString(Pokedex.getPokemonID(args[0])));
		temp.addAll(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
		String[] args_to_loadPokemonByID = new String[7];
		temp.toArray(args_to_loadPokemonByID);
		loadPokemonByID(args_to_loadPokemonByID, activeSkills);
	}

	/**
	 * Usage : index name
	 * 
	 * @throws IOException
	 */
	public static void loadPokedex(String filePath) throws IOException {
		try {
			reader = new FileReader(filePath);
			bufReader = new BufferedReader(reader);
			scanner = new Scanner(bufReader);
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
			if (bufReader != null){
				bufReader.close();
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
			bufReader = new BufferedReader(reader);
			scanner = new Scanner(bufReader);
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
			if (bufReader != null){
				bufReader.close();
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

	public static void loadActiveSkills(String filePath) throws IOException {
		try {
			reader = new FileReader(filePath);
			bufReader = new BufferedReader(reader);
			scanner = new Scanner(bufReader);
			Pattern pattern = Pattern.compile("([\\w\\s]+)\\s(\\d+)");
			Matcher matcher;
			String skillName;
			int skillPower;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.matches("^#+.*")) {
					continue;
				}
				matcher = pattern.matcher(line);
				if (matcher.find()) {
					skillName = matcher.group(1);
					skillPower = Integer.parseInt(matcher.group(2));
					ActiveSkill.getActiveSkill(skillName, skillPower, false);
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
			if (bufReader != null){
				bufReader.close();
			}
		}
	}

	public static void loadActiveSkills() throws IOException {
		loadActiveSkills(DEFAULT_ACTIVE_SKILLS);
	}

}
