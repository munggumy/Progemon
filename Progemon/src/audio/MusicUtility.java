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
import javafx.util.Duration;

public final class MusicUtility {

	private static final int FADE_RESOLUTION = 1000;
	private static final Duration DEFAULT_FADE_DURATION = Duration.millis(1000);
	private static final double DEFAULT_VOLUME = 0.7;
	private static final String MUSIC_MAP_LOCATION = "load/music_map.txt";
	private static final String MUSIC_DIRECTORY = "music";

	private static ExecutorService musicExecutor = Executors.newFixedThreadPool(1);
	private static Map<String, BGMusic> musicMap = new HashMap<String, BGMusic>();
	private static MediaPlayer player;

	/**
	 * Constructor to create a simple thread pool.
	 *
	 * @param numberOfThreads
	 *            - number of threads to use media players in the map.
	 * @throws AudioException 
	 */
	public MusicUtility() throws AudioException {
		musicExecutor = Executors.newFixedThreadPool(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "MusicUtility Thread");
			}
		});
		loadMusicMap(MUSIC_MAP_LOCATION);
		System.out.println("Music successfully loaded.");
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
		Media media = new Media(url.toExternalForm());
		BGMusic music = new BGMusic(media);
		musicMap.put(id, music);
	}

	public static void loadMusic(String id, URL url, Duration startTime, Duration endTime) {
		Media media = new Media(url.toExternalForm());
		BGMusic music = new BGMusic(media, startTime, endTime);
		musicMap.put(id, music);
	}

	public static void loadMusicMap(String mapFileURL) throws AudioException {
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(mapFileURL)))) {
			String delimiter = "\\s*,\\s*";
			String fileNameRegex = "[\\d\\w\\s!.()_]+";
			Pattern pattern = Pattern.compile(String.join(delimiter, "(?<musicCode>[\\w_]+)",
					"(?<fileName>" + fileNameRegex + ")(?<startTime>", "\\d+\\w+)?(?<endTime>", "\\d+[ ]?\\w+)?"));
			Matcher matcher;
			while (scanner.hasNextLine()) {
				matcher = pattern.matcher(scanner.nextLine());
				if (matcher.find()) {
					URL musicURL = ClassLoader.getSystemResource(MUSIC_DIRECTORY + "/" + matcher.group("fileName"));
					if (matcher.group("startTime") != null && matcher.group("endTime") != null) {
						Duration startTime = Duration.valueOf(matcher.group("startTime"));
						Duration endTime = Duration.valueOf(matcher.group("endTime"));
						loadMusic(matcher.group("musicCode"), musicURL, startTime, endTime);
					} else {
						loadMusic(matcher.group("musicCode"), musicURL);
					}
				} else {
					System.err.println("Unmatched pattern in MusicUtil.loadMusic()");
				}
			}
		} catch (FileNotFoundException ex) {
			throw new AudioException("Music Map file not found.", ex);
		} catch (Exception ex) {
			throw new AudioException("Exception while loading music map.", ex);
		}
	}

	public static void playMusic(final String id, boolean useFadeOut) {
		Thread playMusic = new Thread(() -> {
			try {

				if (player != null) {
					if (player.getMedia().equals(musicMap.get(id).getMusic())) {
						return;
					}
					if (useFadeOut) {
						fadeOut().join();
					}
					player.stop();
				}
				player = new MediaPlayer(musicMap.get(id).getMusic());
				// player.setStopTime(Duration.millis(61485));
				player.setVolume(DEFAULT_VOLUME);
				player.setCycleCount(MediaPlayer.INDEFINITE);
				// player.setOnRepeat(() -> {
				// System.out.println("before" + System.currentTimeMillis());
				// player.seek(Duration.millis(8000));
				// System.out.println("after" + System.currentTimeMillis());
				// });
				player.play();

			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		});

		//TODO checkLoop stops?
		Thread checkLoop = new Thread(() -> {
			try {
				while (true) {
					if (player == null) {
						continue;
					} else {
						if (player.getCurrentTime().greaterThanOrEqualTo(Duration.millis(61485))) {
							player.seek(Duration.millis(8000));// TODO
						}
					}
					Thread.sleep(10);
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

		}, "checkLoop");

		checkLoop.start();

		musicExecutor.execute(playMusic);

	}

	public static void playMusic(final String id) {
		playMusic(id, true);
	}

	public static Thread fadeOut() {
		Thread t = new Thread(() -> {
			if (player != null) {
				double currentVolume = player.getVolume();
				double decrement = currentVolume / FADE_RESOLUTION;
				try {
					while (currentVolume > 0) {
						Thread.sleep((long) DEFAULT_FADE_DURATION.toMillis() / FADE_RESOLUTION);
						player.setVolume((currentVolume -= decrement));
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println("Finished Fade Out, player=" + player);
		});
		t.start();
		return t;
	}

	public static Thread fadeIn() {
		Thread t = new Thread(() -> {
			if (player != null) {
				double currentVolume = player.getVolume();
				double increment = currentVolume / FADE_RESOLUTION;
				try {
					while (currentVolume < DEFAULT_VOLUME) {
						Thread.sleep((long) DEFAULT_FADE_DURATION.toMillis() / FADE_RESOLUTION);
						player.setVolume((currentVolume += increment));
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println("Finished Fade In, player=" + player);
		});
		t.start();
		return t;
	}

	public static void stopMusic() {
		player.stop();
	}

	public static void setPlayerRate(double rate) {
		player.setRate(rate); //TODO setPlayerRate
	}

	public static void setVolume(double volume) {
		player.setVolume(volume);
	}

	public static void shutdown() {
		stopMusic();
		musicExecutor.shutdown();
	}

}
