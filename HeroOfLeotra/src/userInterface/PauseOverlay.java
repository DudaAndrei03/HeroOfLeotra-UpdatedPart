package userInterface;

import GameStates.GameState;
import GameStates.Playing;
import Main.Game;
import utilities.Constants;
import utilities.LoadSave;
import GameStates.State.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import utilities.Constants.UI.URM_Buttons;

import static utilities.Constants.UI.URM_Buttons.URM_SIZE;

public class PauseOverlay {

    private Playing playing;

    private BufferedImage backgroundImg;
    private int bgX,bgY,bgW,bgH;

    private UrmButton menuB,replayB,unpauseB;
    public PauseOverlay(Playing playing)
    {
        this.playing = playing;
        loadBackGround();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = (int) (bgX + 70);
        int replayX = (int)(bgX + 170);
        int unpauseX = (int)(bgX + 270);
        int bY = bgY + 70;

        menuB = new UrmButton(menuX,bY,60,60,"MENU");
        replayB = new UrmButton(replayX,bY,60,60,"REPLAY");
        unpauseB = new UrmButton(unpauseX,bY,60,60,"UNPAUSE");

    }

    private void loadBackGround() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = ((int)(backgroundImg.getWidth() * Game.SCALE)/4);
        System.out.println(bgW);
        bgH = ((int)(backgroundImg.getHeight() * Game.SCALE)/4);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int)(100 * Game.SCALE);

    }

    public void update()
    {
        menuB.update();
        replayB.update();
        unpauseB.update();
    }

    public void draw(Graphics g)
    {
        g.drawImage(backgroundImg,bgX,bgY,bgW,bgH,null);
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
    }

    public void mouseDragged(MouseEvent e)
    {

    }


    public boolean isIn(MouseEvent e,PauseButton b)
    {
        return(b.getBounds().contains(e.getX(),e.getY()));
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e,menuB)) {
            menuB.setMousePressed(true);
            System.out.println("S-A APASAT IN JURUL MENIULUI!");
        }
        else if(isIn(e,replayB)) {
            replayB.setMousePressed(true);
            System.out.println("S-A APASAT IN JURUL REPLAY-ULUI");
        }
        else if(isIn(e,unpauseB)) {
            unpauseB.setMousePressed(true);
            System.out.println("S-A APASAT IN JURUL UNPAUSE!");
        }

    }

    public void mouseMoved(MouseEvent e) {
    }


    public void mouseReleased(MouseEvent e) {
        if(isIn(e,menuB)) {
            if (menuB.isMousePressed())
                GameState.state = GameState.MENU;
        }

        else if(isIn(e,replayB)) {
                if (replayB.isMousePressed()) {
                    playing.resetAll();
                    GameState.state = GameState.PLAYING;
                    playing.unpauseGame();
                    //System.out.println("S-a incercat REPLAY!");
                }
            }
        else if(isIn(e,unpauseB))
            {
                playing.unpauseGame();
            }

        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

}
