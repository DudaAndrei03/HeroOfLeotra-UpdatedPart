package Levels;

import Main.Game;
import utilities.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static Main.Game.*;


public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    private int lvlIndex = 1;

    static int[][] map1TileNum;
    public static final int map_WIDTH = 70; //provizoriu

    public LevelManager(Game game)
    {
        this.game = game;
        //levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        importOutsideSprites();
        map1TileNum = new int[TILES_IN_HEIGHT][map_WIDTH]; //TILES_IN_WIDTH ERA ORIGINAL
        levelOne = new Level(LoadSave.GetLevelData());
        loadMap();
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

    public void loadMap()
    {//Citim mapa din fisier
        try
        {
            //InputStream is = getClass().getResourceAsStream("/Map/Map1.txt");
            InputStream is = getClass().getResourceAsStream("/Map/Map-NOU.txt");

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;

            while(col < map_WIDTH && row < TILES_IN_HEIGHT)
            {
                String line = br.readLine();
                while(col < map_WIDTH)
                {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    map1TileNum[row][col] = num;
                    col++;
                }
                if(col == map_WIDTH)
                {
                    col = 0;
                    row++;
                }
            }
            br.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Mapa nu s-a putut obtine!");
    }

    }
    public static int[][] getMap(int whichMap) // mai multe mape
    {
        switch(whichMap)
        {
            case 1:
                return map1TileNum;
                //more cases in the future
            default:
                return map1TileNum;
        }
    }



    public void draw(Graphics g,int lvlOffset)
    {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

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
        if(lvlIndex <= 3)
        {
            lvlIndex ++;
        }
        else {
            lvlIndex = 1;// or 0 , this should mark an error
        }
    }

    public Level getCurrentLevel()
    {
        return levelOne;
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
