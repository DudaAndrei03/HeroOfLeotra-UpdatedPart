package GameStates;

import Main.Game;
import userInterface.MenuButton;
import utilities.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static GameStates.GameState.QUIT;

public class Menu extends State implements stateMethods {

    private BufferedImage backgroundMenu;
    private MenuButton[] buttons = new MenuButton[3];


    public Menu(Game game) {
        super(game);
        backgroundMenu = LoadSave.GetSpriteAtlas(LoadSave.MENU_BG_IMG);
        loadButtons();
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH/2 , (int)(150 * Game.SCALE),0,GameState.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH/2 , (int)(210 * Game.SCALE),1,GameState.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH/2 , (int)(270 * Game.SCALE),2, QUIT);

    }

    @Override
    public void update() {
        for (MenuButton mb:buttons)
            mb.update();
    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundMenu,0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT,null);
        for (MenuButton mb:buttons)
            mb.draw(g);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }


    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb:buttons)
        {
            if(isIn(e,mb))
            {
                mb.setMousePressed(true);
                break;
            }
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons)
            mb.setMouseOver(false);

        for(MenuButton mb : buttons)
            if(isIn(e,mb))
            {
                mb.setMouseOver(true);
                break;
            }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        for (MenuButton mb:buttons)
        {
            if(isIn(e,mb))
            {
                if(mb.isMousePressed()) {
                    mb.ModifyGameState();
                    if(mb.getGameState() == GameState.PLAYING)
                    {
                        game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                    }
                    if(mb.getGameState() == GameState.QUIT)
                    {
                        setGameState(GameState.QUIT); // va iesi instant
                    }
                    if(mb.getGameState() == GameState.OPTIONS)
                    {
                        // will happen something when i finish the options menu
                    }
                }
                break;
            }
        }
        resetButtons();

    }

    private void resetButtons() {
        for (MenuButton mb:buttons)
            mb.resetBools();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            GameState.state = GameState.PLAYING;
        }
        //If we press "enter" we set the gamestate to playing

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
