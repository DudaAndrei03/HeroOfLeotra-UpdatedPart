package userInterface;

import GameStates.GameState;
import GameStates.Playing;
import Levels.LevelManager;
import Main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverOverlay {
    private Playing playing;
    public GameOverOverlay(Playing playing)
    {
        this.playing = playing;
    }

    public void draw(Graphics g)
    {
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0, Game.GAME_WIDTH,Game.GAME_HEIGHT);
        g.setColor(Color.white);
        g.drawString("Game Over", Game.GAME_WIDTH / 2, 150);
        g.drawString("Press ESC to enter Main Menu!",Game.GAME_WIDTH/2, 300);

    }
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            playing.resetAll();
            playing.printGameOver(); //gameOver in momentul de fata este false
            //LevelManager.setLvlIndex(1);
            GameState.state = GameState.MENU;

        }
    }
}
