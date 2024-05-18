package Levels;

import Entities.Skeleton;
import utilities.LoadSave;

import java.util.ArrayList;

import static utilities.LoadSave.GetLevelData;
import static utilities.LoadSave.GetSkeletons;

public class Level {
    private int [][] lvlData;
    private ArrayList<Skeleton> skeletons;

    protected String location;

    public Level()
    {

    }

    public Level(int [][] lvlData)
    {
        this.lvlData = lvlData;
        //createEnemies();
    }

    private void createEnemies() {
        //skeletons = GetSkeletons();
    }

    public int numberOfEnemies()
    {
       return skeletons.size();
    }


    public int getSpriteIndex(int x,int y)
    {
        return lvlData[y][x];
    }
    public int [][] getLevelData()
    {
        return this.lvlData;
    }

    public void setLevelData(int [][]lvlData)
    {
        this.lvlData = lvlData;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }
}
