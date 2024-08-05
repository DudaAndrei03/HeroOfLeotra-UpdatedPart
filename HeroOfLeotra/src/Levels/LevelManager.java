package Levels;

import GameStates.GameState;
import Main.Game;
import utilities.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static Main.Game.*;


public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne,levelTwo,levelThree;

    private ArrayList<Level> levels;

    private static int lvlIndex = 3;

    static int[][] map1TileNum;
    public static final int map_WIDTH = 70; //provizoriu

    public LevelManager(Game game)
    {
        this.game = game;
        levels = new ArrayList<>();
        //levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites();
        map1TileNum = new int[TILES_IN_HEIGHT][map_WIDTH]; //TILES_IN_WIDTH ERA ORIGINAL

        setLevels();

        //System.out.println("O SINGURA DATA!");
        //map2TileNum = new int[TILES_IN_HEIGHT][map_WIDTH];
        //map3TileNum = new int[TILES_IN_HEIGHT][map_WIDTH];



        //loadMap();
    }

    public void setLevels()
    {
        levelOne = new Level(LoadSave.GetLevelData()); // LEVELUL GENERAL
        levelOne.setLocation("/Map/Map1-NOU.txt");
        loadMap(levelOne);

        levelTwo = new Level(LoadSave.GetLevelData());
        levelTwo.setLocation("/Map/Map2-NOU.txt");
        //loadMap(levelTwo);

        levelThree = new Level(LoadSave.GetLevelData());
        levelThree.setLocation("/Map/Map3-NOU.txt");
        //loadMap(levelThree);


        levels.add(levelOne);
        levels.add(levelTwo);
        levels.add(levelThree);
    }

    //Noul Nivel are 224x112 : 14x7=98
    private void importOutsideSprites() {
          BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
//        levelSprite = new BufferedImage[80];
//
//        for(int j = 0 ; j < 8 ; ++j)
//        {
//            for(int i = 0 ; i < 10;++i)
//            {
//                int index = j * 10 + i;
//                levelSprite[index] = img.getSubimage(i * 16, j * 16, 16 ,16);
//            }
//        }
        levelSprite = new BufferedImage[98];
        for(int j = 0; j < 7;++j)
        {
            for(int i = 0 ; i < 14;++i)
            {
                int index = j * 14 + i;
                levelSprite[index] = img.getSubimage(i * 16, j * 16, 16 ,16);
            }
        }

    }

    public ArrayList<Level> getArrayLevels()
    {
        return levels;
    }

    /*public void loadNextLevel()
    {
        String map="";
        updateLvlIndex();
        Level newLevel = levels.get(lvlIndex);
        switch(lvlIndex - 1)
        {
            case 1:map = "/Map/Map1-NOU.txt";
            break;
            case 2:map = "/Map/Map2-NOU.txt";
            break;
            case 3:map = "/Map/Map3-NOU.txt";
        }
        newLevel.setLocation(map);
        game.getPlaying().getEnemyManager().loadEnemies();
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
    }
    */


    public void loadMap(Level level)
    {
        //int[][] levelTileNum = new int[TILES_IN_HEIGHT][map_WIDTH];
        try {

            //InputStream is = getClass().getResourceAsStream("/Map/Map1.txt");
            //InputStream is = getClass().getResourceAsStream(levels.get(lvlIndex - 1).getLocation());
            InputStream is = getClass().getResourceAsStream(level.getLocation());



            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;

            while (col < map_WIDTH && row < TILES_IN_HEIGHT) {
                String line = br.readLine();
                while (col < map_WIDTH) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    map1TileNum[row][col] = num;
                    //levelTileNum[row][col] = num;
                    col++;
                }
                if (col == map_WIDTH) {
                    col = 0;
                    row++;
                }
            }
            level.setLevelData(map1TileNum);
            //map1TileNum=levelTileNum;

            /*if (!levels.contains(levelTwo) || !levels.contains(levelThree)) {
                switch (lvlIndex - 1) {
                    case 1:
                        levelTwo = new Level(LoadSave.GetLevelData());
                        levelTwo.setLocation("/Map/Map2-NOU.txt");
                        levels.add(levelTwo);
                    case 2:
                        levelThree = new Level(LoadSave.GetLevelData());
                        levelThree.setLocation("/Map/Map3-NOU.txt");
                        levels.add(levelThree);
                }
            }
            */

            br.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Mapa nu s-a putut obtine!");
    }

    }
    public static int[][] getMap() // mai multe mape
    {
        return map1TileNum;
    }

    public void draw(Graphics g,int lvlOffset)
    {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        //map1TileNum = levels.get(lvlIndex-1).getLevelData();

        while(col < map_WIDTH && row < TILES_IN_HEIGHT)
        {
            int tileNum = map1TileNum[row][col];
            if(tileNum != 0 && tileNum != 4) {
                if(tileNum == 14 || tileNum == 20) {
                    g.drawImage(levelSprite[tileNum],x - lvlOffset, y, Game.TILES_SIZE, Game.TILES_SIZE, null);

                }
                else {
                    g.drawImage(levelSprite[tileNum], x - lvlOffset, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
                    //This function needs correction, trying to make every tile that isn't grass or ground smaller
                    //will be changed in the future
                }
            }
                col++;
                x += TILES_SIZE;
                //Momentan pana gasesc ceva pentru background / cer

            if(col == map_WIDTH)
            {
                col = 0;
                x = 0;
                row++;
                y += TILES_SIZE;
            }
        }
        //g.drawImage(levelSprite[0],0,0,TILES_SIZE,TILES_SIZE,null);
    }
    // 26x14 - TILESIZE
    public void update()
    {
        loadMap(levels.get(lvlIndex-1));
    }

    public static void updateLvlIndex()
    {
        if(lvlIndex < 3)
        {
            lvlIndex++;
        }
        else {
            lvlIndex = 1;
        }

        //if(Game.getDataBase()!= null)
            //Game.getDataBase().update();

        //lvlIndex = Game.getDataBase().getLvlIndex();

    }
    public static int getLvlIndex()
    {
        return lvlIndex;
    }
    public static void setLvlIndex(int value)
    {
        lvlIndex = value;
    }

    public Level getCurrentLevel()
    {
        //System.out.println("Current levelul este :" + (lvlIndex-1));
        return levels.get(lvlIndex-1);
    }
    public static int getMapWidth()
    {
        return map_WIDTH;
    }

    public int getLevelIndex()
    {
        return lvlIndex;
    }
}
