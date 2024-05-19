package Main;

import Audio.AudioPlayer;
import Entities.Player;
import GameStates.GameState;
import GameStates.Playing;
import Levels.LevelManager;
import GameStates.Menu;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;

public class Game implements Runnable{

    private static DataBase db;
    private static Game instance;
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1.5f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;

    public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    private Playing playing;
    private Menu menu;

    //private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;

    private LevelManager levelManager;
    private Player player;
    public Game(){
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop();
    }
    public static Game getInstance()
    {
        if(instance == null)
        {
            instance = new Game();
        }
        return instance;
    }

    private void initClasses(){
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        playing = new Playing(this);
        db = new DataBase();
    }

    public void update()
    {
        switch(GameState.state)
        {
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            default:
                break;
        }
    }

    public void render(Graphics g)
    {
        switch(GameState.state)
        {
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            default:
                break;
        }
    }

    private void startGameLoop()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0/ UPS_SET;
        long previousTime = System.nanoTime();
        long currentTime;
        int Frames = 0;
        int updates = 0;

        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            currentTime = System.nanoTime();

            deltaU+=  (currentTime - previousTime) / timePerUpdate;
            //it passed enough time to update the game
            deltaF+=  (currentTime - previousTime) / timePerFrame;
            //it passed enough time to update the frame
            previousTime = currentTime;

            if(deltaU >=1)
            {
                update();
                updates++;
                deltaU--;
            }
            if(deltaF >=1)
            {
                gamePanel.repaint();
                Frames++;
                deltaF--;
            }

                if (System.currentTimeMillis() - lastCheck >= 1000) {
                    lastCheck = System.currentTimeMillis();
                    //System.out.println("FPS: " + Frames + "| UPS : " + updates);
                    Frames = 0;
                    updates = 0;
                }
            }
        }


    public void windowFocusLost()
    {
        if(GameState.state == GameState.PLAYING)
        {
            playing.getPlayer().resetDirectionBooleans();
        }

    }
    public Menu getMenu()
    {
        return menu;
    }
    public Playing getPlaying()
    {
        return playing;
    }

    public AudioPlayer getAudioPlayer()
    {
        return audioPlayer;
    }

    public static DataBase getDataBase()
    {
        return db;
    }
}