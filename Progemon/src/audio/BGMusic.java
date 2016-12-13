package audio;

import javafx.scene.media.Media;
import javafx.util.Duration;

public class BGMusic {
	private Media music;
	private Duration startTime, endTime;

	public BGMusic(Media music) {
		this.music = music;
		startTime = Duration.ZERO;
		endTime = music.getDuration();
	}

	public BGMusic(Media music, Duration startTime, Duration endTime) {
		this.music = music;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Media getMusic() {
		return music;
	}

	public final Duration getStartTime() {
		return startTime;
	}

	public final Duration getEndTime() {
		return endTime;
	}

	public final void setStartTime(Duration startTime) {
		this.startTime = startTime;
	}

	public final void setEndTime(Duration endTime) {
		this.endTime = endTime;
	}

}
