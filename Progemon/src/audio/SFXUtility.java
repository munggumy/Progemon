package audio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaException;

public class SFXUtility {

	private static final double DEFAULT_SFX_VOLUME = 1.00;
	private static final String SFX_MAP_LOCATION = "load/sfx_map.csv";
	private static final String SFX_DIRECTORY = "sfx";
	private static final String FILE_TYPE = ".wav";

	private static ExecutorService soundPool;
	private static Map<String, AudioClip> soundEffectsMap = new HashMap<>();

	public SFXUtility(int numberOfThreads) throws AudioException {
		soundPool = Executors.newFixedThreadPool(numberOfThreads, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "SFX Thread");
			}
		});
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
		sound.setVolume(DEFAULT_SFX_VOLUME);
		soundEffectsMap.put(id, sound);
	}

	public static void loadSoundEffectsMap(String url) throws AudioException {
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
			throw new AudioException("sfx_map file not found", ex);
		} catch (Exception ex) {
			throw new AudioException(ex);
		}
	}

	public static void playSound(final String id) {
		Runnable soundPlay = new Runnable() {
			@Override
			public void run() {
				try {
					soundEffectsMap.get(id).play();
				} catch (Exception ex) {
					System.err.println("Error playing SFX id=" + id);
					ex.printStackTrace();
				}
			}
		};

		soundPool.execute(soundPlay);
	}

	public static void stopSound(final String id) {
		Runnable soundStop = new Runnable() {
			@Override
			public void run() {
				try {
					soundEffectsMap.get(id).stop();
				} catch (Exception ex) {
					System.err.println("Error stoping SFX id=" + id);
					ex.printStackTrace();
				}
			}
		};

		soundPool.execute(soundStop);
	}

	public static void shutdown() {
		soundPool.shutdown();
	}
}
