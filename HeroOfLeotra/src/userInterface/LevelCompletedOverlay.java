package userInterface;

import Entities.EnemyManager;
import GameStates.GameState;
import GameStates.Playing;
import Levels.LevelManager;
import Main.Game;
import utilities.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilities.Constants.UI.URM_Buttons.URM_SIZE;

public class LevelCompletedOverlay {
    private Game game;
    private Playing playing;

    private EnemyManager enemyManager;

    private BufferedImage img;

    private int backgroundX,backgroundY,backgroundWidth,backgroundHeight;

    private UrmButton next,menu;
    public LevelCompletedOverlay(Playing playing,Game game,EnemyManager enemyManager)
    {
        this.game = game;
        this.playing = playing;
        this.enemyManager = enemyManager;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int) (backgroundWidth + 250);
        int nextX = (int) (backgroundWidth + 350);
        int y = (int)(backgroundHeight + 10);
        next = new UrmButton(nextX,y,URM_SIZE,URM_SIZE,"NEXT");
        menu = new UrmButton(menuX,y,URM_SIZE,URM_SIZE,"MENU");
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
        backgroundWidth=(int)((img.getWidth() * Game.SCALE)/3.5);
        backgroundHeight=(int)((img.getHeight()*Game.SCALE/3.5));
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int) (75 * Game.SCALE);
    }

    public void draw(Graphics g)
    {
     g.drawImage(img,backgroundX,backgroundY,backgroundWidth,backgroundHeight,null);
     next.draw(g);
     menu.draw(g);
    }
    public void update()
    {
        menu.update();
        next.update();
    }
    public void mouseMoved(MouseEvent e)
    {

    }

    public void mousePressed(MouseEvent e)
    {
        if(isIn(menu,e))
            menu.setMousePressed(true);
        else if(isIn(next,e))
            next.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e)
    {
        if(isIn(menu,e)) {
            if (menu.isMousePressed())
                GameState.state = GameState.MENU;
        }

        else if(isIn(next,e)) {
            if (next.isMousePressed()) {
               //LevelManager.updateLvlIndex();
                //int lvl_ind = LevelManager.getLvlIndex();
                //System.out.println(lvl_ind);
                GameState.state = GameState.PLAYING;
                playing.resetAll();
                game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex()); // Pt schimbarea muzicii

                //playing.unpauseGame();
                System.out.println("Next LEVEL!");

            }
        }
        menu.resetBools();
        next.resetBools();
    }


    public boolean isIn(UrmButton b,MouseEvent e)
    {
        return b.getBounds().contains(e.getX(),e.getY());
    }
}

