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

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicUtility {

	private static final String MUSIC_MAP_LOCATION = "load/music_map.txt";
	private static final String MUSIC_DIRECTORY = "music";
	private static ExecutorService musicExecutor = Executors.newSingleThreadExecutor();
	private static Map<String, Media> musicMap = new HashMap<String, Media>();
	private static MediaPlayer player;

	/**
	 * Constructor to create a simple thread pool.
	 *
	 * @param numberOfThreads
	 *            - number of threads to use media players in the map.
	 */
	public MusicUtility() {
		musicExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "MusicUtility Thread");
			}
		});
		loadMusicMap(MUSIC_MAP_LOCATION);
		System.out.println("MusicUtility Successfully Loaded.");
	}

	/**
	 * Load a sound into a map to later be played based on the id.
	 *
	 * @param id
	 *            - The identifier for a sound.
	 * @param url
	 *            - The url location of the media or audio resource. Usually in
	 *            src/main/resources directory.
	 */
	public static void loadMusic(String id, URL url) {
		Media music = new Media(url.toExternalForm());
		musicMap.put(id, music);
	}

	public static void loadMusicMap(String url) {
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(url)))) {
			Pattern pattern = Pattern.compile("(?<musicCode>[\\w_]+),\\s*(?<fileName>.+)");
			Matcher matcher;
			while (scanner.hasNextLine()) {
				matcher = pattern.matcher(scanner.nextLine());
				if (matcher.find()) {
					URL musicURL = ClassLoader.getSystemResource(MUSIC_DIRECTORY + "/" + matcher.group("fileName"));
					loadMusic(matcher.group("musicCode"), musicURL);
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
	public static void playMusic(final String id) {
		Runnable musicPlay = new Runnable() {
			@Override
			public void run() {
				if (player != null) {
					player.stop();
				}
				try {
					player = new MediaPlayer(musicMap.get(id));
					player.play();
				} catch (IndexOutOfBoundsException e) {
					System.err.println("Cannot find music id=" + id);
				}
			}
		};
		musicExecutor.execute(musicPlay);
	}

	public static void stopMusic() {
		player.stop();
	}

	/**
	 * Stop all threads and media players.
	 */
	public static void shutdown() {
		musicExecutor.shutdown();
	}

}
