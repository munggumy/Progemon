package audio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaException;

public class SFXUtility {

	private static final String SFX_MAP_LOCATION = "load/sfx_map.csv";
	private static final String SFX_DIRECTORY = "sfx";
	private static final String FILE_TYPE = ".wav";

	private static ExecutorService soundPool;
	public static Map<String, AudioClip> soundEffectsMap = new HashMap<>();
	private static AtomicInteger numberOfPlaying = new AtomicInteger(0);
	private static int MAX_PLAYING;

	/**
	 * Constructor to create a simple thread pool.
	 *
	 * @param numberOfThreads
	 *            - number of threads to use media players in the map.
	 */
	public SFXUtility(int numberOfThreads) {
		soundPool = Executors.newFixedThreadPool(numberOfThreads, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "SFX Thread");
			}
		});
		MAX_PLAYING = numberOfThreads;
		// loadSoundEffect("walk_obstructed",
		// ClassLoader.getSystemResource("sfx/emerald_0007.wav"));
		// loadSoundEffect("change_place",
		// ClassLoader.getSystemResource("sfx/emerald_0009.wav"));
		// loadSoundEffect("attack_super_effective",
		// ClassLoader.getSystemResource("sfx/emerald_000E.wav"));
		// loadSoundEffect("change_place",
		// ClassLoader.getSystemResource("sfx/emerald_000C.wav"));
		loadSoundEffectsMap(SFX_MAP_LOCATION);
		System.out.println("SFX Loaded");
	}

	/**
	 * Load a sound into a map to later be played based on the id.
	 *
	 * @param id
	 *            - The identifier for a sound.
	 * @param url
	 *            - The url location of the media or audio resource. Usually in
	 *            src/main/resources directory.
	 * @throws MediaException
	 */
	public static void loadSoundEffect(String id, URL url) {
		AudioClip sound = new AudioClip(url.toExternalForm());
		soundEffectsMap.put(id, sound);
	}

	public static void loadSoundEffectsMap(String url) {
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(url)))) {
			Pattern pattern = Pattern.compile("(?<soundEffectCode>[\\w_]+)\\s*,\\s*(?<fileName>.+)");
			Matcher matcher;
			while (scanner.hasNextLine()) {
				matcher = pattern.matcher(scanner.nextLine());

				if (matcher.find()) {
					URL sfxURL = ClassLoader
							.getSystemResource(SFX_DIRECTORY + "/" + matcher.group("fileName") + FILE_TYPE);
					loadSoundEffect(matcher.group("soundEffectCode"), sfxURL);
				}
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Lookup a name resource to play sound based on the id.
	 *
	 * @param id
	 *            identifier for a sound to be played.
	 */
	public static void playSound(final String id) {
		Runnable soundPlay = new Runnable() {
			@Override
			public void run() {
				soundEffectsMap.get(id).play();
			}
		};

		soundPool.execute(soundPlay);
	}

	/**
	 * Stop all threads and media players.
	 */
	public static void shutdown() {
		soundPool.shutdown();
	}
}
