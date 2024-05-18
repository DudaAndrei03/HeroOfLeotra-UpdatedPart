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

    private boolean first_time_loading = true;
    private int xLvlOffset;
    private int leftBorder = (int)(0.5 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.5 * Game.GAME_WIDTH);
    private int lvlTilesWide = LevelManager.getMapWidth(); //70

    private int maxTilesOffset =  lvlTilesWide - Game.TILES_IN_WIDTH;
    private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;


    private BufferedImage backgroundImg,mountainImg_front,trees_back;

    private BufferedImage backgroundImg2,closeTrees2,midTrees2;

    private BufferedImage backgroundImg3;

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

        backgroundImg2 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND2);
        closeTrees2 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_CLOSETREES2);
        midTrees2 = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_MIDTREES2);



    }

    private void initClasses() {
        this.levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = Player.getInstance(100, 200, (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), this);
        pauseOverlay = new PauseOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this,game,enemyManager);
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

        if(first_time_loading)
        {
            levelManager.update();
            player.update();
            enemyManager.loadEnemies();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            first_time_loading = false;
        } // to make sure that we can select any level that we want to the first time;
            // so that if we decide to start with level2 we would update everything from player to the map

        if (!gameOver && !paused && !lvlCompleted) {
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            pauseOverlay.update();
            checkCloseToBorder();

            if(checklvlFinished()) {
                levelCompletedOverlay.update();
                LevelManager.updateLvlIndex();
                levelManager.update();
                enemyManager.loadEnemies();
                lvlCompleted = true;
            }
        }
        else
        {
            if(paused)
                pauseOverlay.update();
        }
        //enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
        //levelManager.update();
        //System.out.println(lvlCompleted);
    }

    @Override
    public void draw(Graphics g) {

        if(!gameOver && !lvlCompleted) {

            int lvlIndex = levelManager.getLevelIndex();
            //System.out.println(lvlIndex);

            if(levelManager.getCurrentLevel() == levelManager.getArrayLevels().get(0)) { // daca e primul nivel incarcam asta
                g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
                drawTrees_Back(g);
                drawMountain_Front(g);
            }

            if(levelManager.getCurrentLevel() == levelManager.getArrayLevels().get(1)) // daca e al doilea nivel incarcam asta
            {
                g.drawImage(backgroundImg2,0,0,Game.GAME_WIDTH,Game.GAME_HEIGHT,null);
                drawMidTrees(g);
                drawCloseTrees(g);
            }

            levelManager.draw(g, xLvlOffset);
            player.render(g, xLvlOffset);
            enemyManager.draw(g, xLvlOffset);
            if(paused)
            {
                //System.out.println(paused);
                pauseOverlay.draw(g);
            }

        }
        else if(gameOver)
        {
                gameOverOverlay.draw(g);
        }

        else if(lvlCompleted)
        {
            levelCompletedOverlay.draw(g);
            //levelManager.update();//lvlCompleted = false;
            //enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
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

    private void drawCloseTrees(Graphics g) {
        for(int i = 0; i < 4; ++i) {

            g.drawImage(closeTrees2, 0 + (int) (i) * CLOSE_TREES_WIDTH, 0, CLOSE_TREES_WIDTH, CLOSE_TREES_HEIGHT + 300, null);
        }
    }
    private void drawMidTrees(Graphics g) {
        for (int i = 0; i < 4; ++i) {
            g.drawImage(midTrees2, 0 + (int) (i) * CLOSE_TREES_WIDTH, 0, CLOSE_TREES_WIDTH, CLOSE_TREES_HEIGHT + 300, null);
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
        //System.out.println(EnemyManager.getEnemies());

        if(EnemyManager.getEnemies() <= 0 && player.getHitbox().x + 80 >= maxWidth)
        //if(EnemyManager.getEnemies() <= 3 || player.getHitbox().x + 80 >= maxWidth)
        {
            //Ca sa updatam indexul
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
        paused = false;
        gameOver = false;
        lvlCompleted = false;
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

    public EnemyManager getEnemyManager()
    {
        return enemyManager;
    }
}
