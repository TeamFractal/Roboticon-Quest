package io.github.teamfractal.util;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Christian Beddows on 15/02/2017.
 */
public class GameMusic {
    private Music music;

    public GameMusic() {
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/squaredance.mp3"));
    }

    public void play() {
        music.play();
    }

    public void pause() {
        music.pause();
    }

    public void changeMusic(FileHandle filename) {
        music = Gdx.audio.newMusic(filename);}

    public void stop(){
        music.stop();
    }

    public float getPosition(){
        return music.getPosition();
    }

    public float getVolume(){
        return music.getVolume();
    }

    public void setPosition(float time){
        music.setPosition(time);
    }

    public void setVolume(float volume){
        music.setVolume(volume);
    }

    public boolean isPlaying(){
        return music.isPlaying();
    }

    public boolean isLooping(){
        return music.isLooping();
    }

    public void setLooping(boolean isLooping){
        music.setLooping(isLooping);
    }

    public void dispose(){
        music.dispose();
    }
}
