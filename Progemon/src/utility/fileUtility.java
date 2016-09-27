package utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class fileUtility {
	
	private static final String DEFAULT_PATH = "/res";
	private static final String DEFAULT_LOAD_POKEMON = "res/pokemon_list.txt";
	private static final String DEFAULT_LOAD_POKEDEX = "res/pokedex.txt";
	static FileReader reader;
	static FileWriter writer;
	static Scanner scanner;
	
	/** Usage id attack defence  */
	public static void loadPokemon(String filePath){
		try {
			reader = new FileReader(filePath);
			// TODO
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadPokemon(){
		loadPokemon(DEFAULT_LOAD_POKEMON);
	}
	
	/** Usage : index name 
	 * @throws IOException */
	public static void loadPokedex(String filePath) throws IOException{
		try {
			reader = new FileReader(filePath);
			scanner = new Scanner(reader);
			int temp_id;
			String temp_name;
			while (scanner.hasNextLine()) {
				if (scanner.hasNextInt()) {
					temp_id = scanner.nextInt();
					if (scanner.hasNextLine()) {
						temp_name = scanner.nextLine().replaceAll(" ", "");
						utility.Pokedex.addPokemonToPokedex(temp_id, temp_name);
					} 
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(scanner != null){
				scanner.close();
			}
			if(reader != null){
				reader.close();
			}
		}
	}
	
	public static void loadPokedex() throws IOException{
		loadPokedex(DEFAULT_LOAD_POKEDEX);
	}

}
