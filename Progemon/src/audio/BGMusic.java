package audio;

import javafx.scene.media.Media;

public class BGMusic {
	private Media music;

	BGMusic(Media music) {
		this.music = music;
	}

	public Media getMusic() {
		return music;
	}
}
