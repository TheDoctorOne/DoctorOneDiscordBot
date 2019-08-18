package DoctorOne.DoctorOne;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.core.audio.AudioSendHandler;

public class SimpleAudioHandler implements AudioSendHandler {
	  private AudioPlayer audioPlayer;
	  private AudioFrame lastFrame;

	  public void AudioPlayerSendHandler(AudioPlayer audioPlayer) {
	    this.audioPlayer = audioPlayer;
	  }

	  public boolean canProvide() {
	    lastFrame = audioPlayer.provide();
	    return lastFrame != null;
	  }

	  public byte[] provide20MsAudio() {
	    return lastFrame.getData();
	  }

	  public boolean isOpus() {
	    return true;
	  }
	}
