package GameStates;

import Entities.Player;
import Levels.LevelManager;
import Main.Game;
import userInterface.MenuButton;
import utilities.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static GameStates.GameState.*;

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
        buttons[1] = new MenuButton(Game.GAME_WIDTH/2 , (int)(210 * Game.SCALE),1,GameState.LOAD);
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
                        //game.getPlaying().getPlayer().printCurrentHealth();
                        game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                        /*int health = game.getPlaying().getPlayer().getCurrentHealth();
                        System.out.println(health);
                        game.getPlaying().getPlayer().setCurrentHealth(100);
                        System.out.println(game.getPlaying().getPlayer().getCurrentHealth());
                         */
                        //Adaugata noua in caz de da erori trebuie scoasa(Dedesubt)
                        game.getPlaying().getLevelManager().update();

                    }
                    if(mb.getGameState() == GameState.QUIT)
                    {
                        setGameState(GameState.QUIT); // va iesi instant
                    }
                    if(mb.getGameState() == GameState.LOAD)
                    {
                        //System.out.println("AM AJUNS AICI!!!!");
                        double playerX = Game.getDataBase().getPlayerX();
                        double playerY = Game.getDataBase().getPlayerY();
                        int Health = Game.getDataBase().getHealth();
                        int Score = Game.getDataBase().getScore();
                        int lvlIndex = Game.getDataBase().getLvlIndex();

                        GameState.state = PLAYING;
                        game.getPlaying().resetAll();//resetam tot ce tine de playing
                        game.getPlaying().getPlayer().resetAllButSetPositionForPlayer(playerX,playerY);//resetam inca o data tot dar punem pozitiile jucatorului din baza de date
                        LevelManager.setLvlIndex(lvlIndex);//setam levelindex din baza de date
                        game.getPlaying().getPlayer().setCurrentHealth(Health);//setam viata jucatorului din baza de date
                        game.getPlaying().getLevelManager().update();//updatam mapa pe care suntem
                        game.getPlaying().getEnemyManager().loadEnemies();//incarcam corespunzator mapei in care suntem pozitiile inamicilor

                        Player.updateScore(Score);

                        game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                        //game.getPlaying().getLevelManager().update();
                        game.getPlaying().getPlayer().setInAir(true);
                        //Il setam initial in aer daca e ceva sa cada nu sa ramana pana la apasarea urmatoarei taste

                        //setGameState(GameState.PLAYING);
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
