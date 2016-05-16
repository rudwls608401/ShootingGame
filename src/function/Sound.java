package function;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	private static Clip clip;

	public Sound(String file, boolean Loop) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			if (Loop)
				clip.loop(-1);
			// Loop값이 true면 사운드 재생을 무한반복 
			// false면 한번만 재생 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void musicStop() {
		clip.stop();
	}
}
