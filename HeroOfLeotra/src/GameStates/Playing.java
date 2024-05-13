package GameStates;

import Entities.EnemyManager;
import Entities.Player;
import Levels.LevelManager;
import Main.Game;
import org.w3c.dom.ls.LSOutput;
import userInterface.GameOverOverlay;
import userInterface.LevelCompletedOverlay;
import userInterface.PauseOverlay;
import utilities.Constants;
import utilities.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilities.Constants.Environment.*;
import static Levels.LevelManager.*;

public class Playing extends State implements stateMethods {
    private LevelManager levelManager;
    private Player player;
    private EnemyManager enemyManager;

    private boolean paused = false;
    private PauseOverlay pauseOverlay;

    private LevelCompletedOverlay levelCompletedOverlay;

    private boolean lvlCompleted = false;
    private int xLvlOffset;
    private int leftBorder = (int)(0.5 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.5 * Game.GAME_WIDTH);
    private int lvlTilesWide = LevelManager.getMapWidth(); //70

    private int maxTilesOffset =  lvlTilesWide - Game.TILES_IN_WIDTH;
    private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;


    private BufferedImage backgroundImg,mountainImg_front,trees_back;

    private boolean gameOver = false;

    private GameOverOverlay gameOverOverlay;

    public Playing(Game game) {
        super(game);
        initClasses();
        //lvlTilesWide = levelManager.getCurrentLevel().getLevelData()[0].length;

        /*int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;*/


        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        mountainImg_front = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_MOUNTAINS_FRONT);
        trees_back = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_TREES_BACK);

    }

    private void initClasses() {
        this.levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = Player.getInstance(100, 200, (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), this);
        pauseOverlay = new PauseOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);


        player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
    }

    public void windowFocusLost() {
        player.resetDirectionBooleans();
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update() {
        if (!gameOver && !paused && !lvlCompleted) {
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            pauseOverlay.update();
            checkCloseToBorder();
        }
        else
        {
            if(paused)
                pauseOverlay.update();
            if(lvlCompleted)
                levelCompletedOverlay.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        if(!gameOver) {
            g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
            drawTrees_Back(g);
            drawMountain_Front(g);
            levelManager.draw(g, xLvlOffset);
            player.render(g, xLvlOffset);
            enemyManager.draw(g, xLvlOffset);
            if(paused)
            {
                System.out.println(paused);
                pauseOverlay.draw(g);
            }
            if(checklvlFinished())
            {
                levelCompletedOverlay.draw(g);
            }

        }else if(gameOver)
        {
            gameOverOverlay.draw(g);
        }

    }

    private void drawTrees_Back(Graphics g) {
        for(int i = 0; i < 3; ++i)
        {
            g.drawImage(trees_back, 0 + (i * TREES_BACK_WIDTH), 75, TREES_BACK_WIDTH, TREES_BACK_HEIGHT + 100, null);

        }
    }

    private void drawMountain_Front(Graphics g) {
        for(int i = 0 ; i < 3 ; ++i) {
            g.drawImage(mountainImg_front, 0 + (int)(1.2*i) * MOUNTAIN_FRONT_WIDTH, -142, MOUNTAIN_FRONT_WIDTH, MOUNTAIN_FRONT_HEIGHT, null);
        }


    }

    private void checkCloseToBorder()
    {
        int playerX = (int) player.getHitbox().x;
        int difference = playerX - xLvlOffset;


        if(difference > rightBorder)
        {
            xLvlOffset += difference - rightBorder;
        }
        else if(difference < leftBorder)
        {
            xLvlOffset += difference - leftBorder;
        }

        if(xLvlOffset > maxLvlOffsetX)
        {
            xLvlOffset = maxLvlOffsetX;
        }
        else if (xLvlOffset < 0)
        {
            xLvlOffset = 0;
        }

        //System.out.println(xLvlOffset);

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }


    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mousePressed(e);
            else if(lvlCompleted)
            {
                levelCompletedOverlay.mousePressed(e);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(paused)
            pauseOverlay.mouseMoved(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver) {
            if (paused)
                pauseOverlay.mouseReleased(e);
            else if(lvlCompleted)
            {
                levelCompletedOverlay.mouseReleased(e);
            }
        }

    }

    public void unpauseGame()
    {
        paused = false;
    }

    public boolean checklvlFinished() {
        int maxWidth = map_WIDTH * Game.TILES_SIZE;
        System.out.println(EnemyManager.getEnemies());

        if(EnemyManager.getEnemies() <= 0 && player.getHitbox().x + 80 >= maxWidth)
        {
            lvlCompleted = true;
            return true;
        }
        return false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        } else {
            switch (e.getKeyCode()) {
            /*case KeyEvent.VK_W:
                gamePanel.getGame().getPlayer().setUp(false);
                break;*/

                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;

            /*case KeyEvent.VK_S:
                gamePanel.getGame().getPlayer().setDown(false);
                break;*/

                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_E:
                    player.setAttacking(true);
                    break;

                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_W:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;

            }

        }
    }


    public void keyReleased(KeyEvent e) {
        if (!gameOver) {
            switch (e.getKeyCode()) {
            /*case KeyEvent.VK_W:
                gamePanel.getGame().getPlayer().setUp(false);
                break;*/

                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;

            /*case KeyEvent.VK_S:
                gamePanel.getGame().getPlayer().setDown(false);
                break;*/

                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
            /*case KeyEvent.VK_E:
                gamePanel.getGame().getPlayer().setAttacking(false);
                break;
                */
                case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
                case KeyEvent.VK_W:
                    player.setJump(false);
                    break;

            }
        }
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void resetAll()
    {
        //TODO: reset everything regarding player,enemy,level!
        gameOver = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
    }
    public void checkEnemyHit(Rectangle2D.Float attackHitBox)
    {
        enemyManager.checkEnemyHit(attackHitBox);
    }

    public void setGameOver(boolean gameOver)
    {
        this.gameOver = gameOver;
    }
}
