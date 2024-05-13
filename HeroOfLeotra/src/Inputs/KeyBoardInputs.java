package Inputs;

import GameStates.GameState;
import Main.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static utilities.Constants.Directions.*;

public class KeyBoardInputs implements KeyListener {
    private GamePanel gamePanel;

    public KeyBoardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        /*switch (e.getKeyCode()) {
            case KeyEvent.VK_E:
                gamePanel.getGame().getPlayer().setAttacking(true);
                break;
            case KeyEvent.VK_W:
                gamePanel.getGame().getPlayer().setUp(true);
                break;

            case KeyEvent.VK_A:
                gamePanel.getGame().getPlayer().setLeft(true);
                break;

            case KeyEvent.VK_S:
                gamePanel.getGame().getPlayer().setDown(true);
                break;

            case KeyEvent.VK_D:
                gamePanel.getGame().getPlayer().setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                gamePanel.getGame().getPlayer().setJump(true);
                break;
        }*/


        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyPressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyPressed(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().keyReleased(e);
                break;
            default:
                break;
        }
    }

}


