package utilities;
import Entities.Skeleton;
import Levels.LevelManager;
import Main.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import utilities.Constants.EnemyConstants.*;

public class LoadSave {
    public static final String PLAYER_ATLAS = "Player/warrior.EDIT3_universal.png";
    //public static final String LEVEL_ATLAS = "Map/TileMap.png";
    public static final String LEVEL_ATLAS = "Map/Munte_NIVEL1.png";


    public static final String PLAYING_BG_IMG ="Map/sunset.png";
    public static final String PLAYING_BG_TREES_FRONT ="Map/trees_front.png";
    public static final String PLAYING_BG_TREES_BACK ="Map/trees_back.png";

    public static final String PLAYING_BG_MOUNTAINS_FRONT ="Map/mountains.png";
    public static final String PLAYING_BG_MOUNTAINS_BACK ="Map/mountains.png";

    public static final String MENU_BG_IMG = "Menu/foggy.png";

    public static final String MENU_BUTTON = "Menu/Menu1.png";
    public static final String SKELETON = "Enemies/Skeleton.png";

    public static final String HEALTH_BAR = "StatusPlayer/HealthBar.png";

    public static final String COMPLETED_IMG ="Menu/CompletedLevel.png";

    public static final String URM_BUTTONS = "Menu/Buttons_UPDATED.png";

    public static final String PAUSE_BACKGROUND = "Menu/PauseOverlay.png";

    private static int [][]lvlData;

    //int [][] lvlData = new int [Game.TILES_IN_HEIGHT][70];
    public static BufferedImage GetSpriteAtlas(String fileName)
    {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

        try {
            img = ImageIO.read(is);
        }catch (IOException e){
            e.printStackTrace();
        }
        finally
        {
            try {
                is.close();
            } catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return img;
    }


    public static int[][] GetLevelData() {
        lvlData = new int [Game.TILES_IN_HEIGHT][70]; // Game.TILES_IN_WIDTH
        //BufferedImage img = GetSpriteAtlas(LEVEL_ATLAS);

        lvlData = LevelManager.getMap(1); //luam lvlData pentru prima mapa momentan

        return lvlData;
    }

    public static int getWidth()
    {
        return lvlData.length;
    }



    public static ArrayList<Skeleton> GetSkeletons()
    {
        int value;
        ArrayList<Skeleton> list = new ArrayList<Skeleton>();

    for(int j = 0 ; j < Game.TILES_IN_HEIGHT; j++)
        for(int i = 0; i < 70; ++i)
        {
            value = lvlData[j][i];
            if(value == Constants.EnemyConstants.SKELETON)
            {
            list.add(new Skeleton(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        }
    return list;

    }

}
