package Audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.stream.IntStream;

public class AudioPlayer {

    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;
    public static int LEVEL_3 = 3;


    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;

    public static int ATTACK_1 = 4;
    public static int ATTACK_2 = 5;
    public static int ATTACK_3 = 6;
    // sounds for attack just to not make it sound repetitive each time our player attacks

    private Clip[] songs, effects;
    private int currentSongId; //for multiple songs
    private float volume = 0.75f;
    private boolean songMute,effectMute;
    private Random r = new Random();

    public AudioPlayer(){
        loadSongs();
        //loadEffects();
        playSong(MENU_1);
    }
    private void loadSongs(){
        String[] names = {"menu","level1","level2","level3"};
        songs = new Clip[names.length];
        for(int i = 0; i < songs.length; ++i)
        {
            songs[i] = getClip(names[i]);
        }
    }
    private void loadEffects() {
        String[] effectNames = {"die","jump","lvlCompleted","attack1","attack2"};
        effects = new Clip[effectNames.length];
        for(int i = 0; i < effectNames.length; ++i)
        {
            effects[i] = getClip(effectNames[i]);
        }
        updateEffectsVolume();
    }
    private Clip getClip(String name) // name of File
    {
        URL url = getClass().getResource("/Audio/" + name + ".wav");
        AudioInputStream audio;
        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException e)
        {
            e.printStackTrace();
        }

        return null;
    }
    private void updateSongVolume()
    {
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        //we gain control of songs
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum(); //adding to minimum the volume * range
        gainControl.setValue(gain);

    }
    private void updateEffectsVolume()
    {
        for(Clip c: effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            //we gain control of songs
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum(); //adding to minimum the volume * range
            gainControl.setValue(gain);
        }
    }
    public void toggleSongMute()
    {
        this.songMute = !songMute;
        for(Clip c:songs)
        {
            BooleanControl boolean_ctrl = (BooleanControl)c.getControl(BooleanControl.Type.MUTE);
            boolean_ctrl.setValue(songMute);
        }
    }
    public void toggleEffectMute()
    {
        this.songMute = !songMute;
        for(Clip c:effects)
        {
            BooleanControl boolean_ctrl = (BooleanControl)c.getControl(BooleanControl.Type.MUTE);
            boolean_ctrl.setValue(songMute);
        }

    }
    public void playEffect(int effect)
    {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }
    public void playSong(int songIndex)
    {
        stopSong();
        //check if there is a song playing and then we stop it
        currentSongId = songIndex;
        updateSongVolume();

        //we set the song that we want to hear to position 0 at the beginning
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
        //we start the song
    }
    public void playAttackSound()
    {
        int start = 4;
        int end = 6;
        start += r.nextInt(3);
        playEffect(start);
    }
    public void setvolume(float volume)
    {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }
    public void stopSong()
    {
        songs[currentSongId].isActive();
        songs[currentSongId].stop();
    }
    public void setLevelSong(int lvlIndex) {
        switch(lvlIndex)
        {
            case 1:
                playSong(1);
                break;
            case 2:
                playSong(2);
                break;
            case 3:
                playSong(3);
                break;
        }
    }
    public void levelCompleted()
    {
        stopSong();
        playEffect(LVL_COMPLETED);

    }
}
