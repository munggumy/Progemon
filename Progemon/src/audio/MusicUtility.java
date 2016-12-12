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

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public final class MusicUtility {

	private static final Duration DEFAULT_FADE_DURATION = Duration.millis(1000);
	private static final double DEFAULT_VOLUME = 0.7;
	private static final String MUSIC_MAP_LOCATION = "load/music_map.txt";
	private static final String MUSIC_DIRECTORY = "music";
	private static ExecutorService musicExecutor = Executors.newFixedThreadPool(1);
	private static Map<String, BGMusic> musicMap = new HashMap<String, BGMusic>();
	private static MediaPlayer player;
	private static URL emptyAudioURL;

	/**
	 * Constructor to create a simple thread pool.
	 *
	 * @param numberOfThreads
	 *            - number of threads to use media players in the map.
	 */
	public MusicUtility() {
		musicExecutor = Executors.newFixedThreadPool(1, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "MusicUtility Thread");
			}
		});
		emptyAudioURL = ClassLoader.getSystemResource(MUSIC_DIRECTORY + "/empty.mp3");
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

	public static void loadMusicMap(String url) {
		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(url)))) {
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
			System.err.println("Music Map file not found.");
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
	public static void playMusic(final String id, boolean useFadeOut) {
		Thread playMusic = new Thread(() -> {
			try {
				if (player != null) {
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

		Thread checkLoop = new Thread(() -> {
			try {
				while (true) {
					if (player == null) {
						continue;
					} else {
						if (player.getCurrentTime().greaterThanOrEqualTo(Duration.millis(61485))) {
							player.seek(Duration.millis(8000));
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
				Timeline timeline = new Timeline(
						new KeyFrame(DEFAULT_FADE_DURATION, new KeyValue(player.volumeProperty(), 0)));
				timeline.play();
				try {
					Thread.sleep((long) DEFAULT_FADE_DURATION.toMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
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
				Timeline timeline = new Timeline(
						new KeyFrame(DEFAULT_FADE_DURATION, new KeyValue(player.volumeProperty(), DEFAULT_VOLUME)));
				timeline.play();
				try {
					Thread.sleep((long) DEFAULT_FADE_DURATION.toMillis());
				} catch (InterruptedException e) {
					e.printStackTrace();
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
		player.setRate(rate);
	}

	public static void setVolume(double volume) {
		player.setVolume(volume);
	}

	/**
	 * Stop all threads and media players.
	 */
	public static void shutdown() {
		musicExecutor.shutdown();
	}

}
