/*
 * While the clip-player handles the playback of individual clips,
 * the music manager does the work of tracking vote-skip thresholds,
 * opening & closing individual playback streams, and beginning
 * playback at the proper point in time for new users
 */

package Pomodoro;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicManager {
	// Local music library.
	List<ClipPlayer> musicLibrary = Collections.synchronizedList(new ArrayList<ClipPlayer>());
	
	int currentSong;
	int skipVotes;
	
	// Fill the music library. All files should be local to the project.
	public MusicManager() {
		skipVotes = 0;
		currentSong = 0;
		try {
			musicLibrary.add(new ClipPlayer("BEAT_Smoothieenlaplaya_DRAFT.aiff"));
			musicLibrary.add(new ClipPlayer("SG_BlendItUpSmooth_Loop.aiff"));
		} catch (LineUnavailableException LUE) {
			System.out.println("Error instantiating media library:" + LUE.getMessage());
		} catch (IOException IOE) {
			System.out.println("I/O error:" + IOE.getMessage());
		} catch (UnsupportedAudioFileException UAFE) {
			System.out.println("Unsupported audio file:" + UAFE.getMessage());
		}
	}
	
	// Starts music library playback
	public void Play() {
		musicLibrary.get(currentSong).play();
	}
	
	// Transitions to the nextSong in the playback loop
	public void nextSong() {
		// Stops current clip playback and resets it for future playback
		musicLibrary.get(currentSong).clip.stop();
		musicLibrary.get(currentSong).clip.setFramePosition(0);
		currentSong++;
		if (currentSong == musicLibrary.size()) {
			currentSong = 0;
		}
		musicLibrary.get(currentSong).play();
	}
	
	public boolean playbackEnded() {
		return musicLibrary.get(currentSong).clip.getLongFramePosition() >= musicLibrary.get(currentSong).clip.getFrameLength();
	}
	
	// Check the current voting threshold
	public int Threshold() {
		// TODO replace this with a SQL call to get the current number of online users divided by 2
		return 3;
	}
	
	// Adds a skipVote. If the current addition would cause the number to reach the threshold, makes
	// a call to nextSong()
	public void VoteSkip() {
		skipVotes++;
		if (skipVotes > Threshold()) {
			nextSong();
		}
	}
	
	public void CloseAllStreams() {
		// TODO I'm not sure if we'll need this if the audio files are played from the server,
		// but if they're played locally on each individual system then yeah we'll have to do it this way.
	}
	
	// Private class ClipPlayer opens and starts the audio streams for playback.
	private class ClipPlayer {
		// Audio clip to be played
		Clip clip;
	   	
		AudioInputStream audioSource;
		String filePath;
		
		public ClipPlayer(String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
			filePath = filename;
			
			// Create AudioInputStream object
			audioSource = 
					AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
	       
			// Make a clip from the audioSource
			clip = AudioSystem.getClip();
	       
			// Open the audioSource stream
			clip.open(audioSource);
			
			// Set the clip to constantly be available to play
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}

		// Play the audio clip
		public void play() 
		{
			clip.start();
		}
	}
}