package utilities;

import Main.Game;

import java.awt.geom.Rectangle2D;

public class Help {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        //original hitbox : .      .

        //                  .      . I will modify the check into something like:

        //                  .    .    .
        //                  .         . adding 4 more points to check for the middle part of each area
        //                  .    .    .

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                if (isSolid(x + i, y + j, lvlData)) {
                    return false;
                }
            }
        }
        return true;

        /*if(!isSolid(x,y,lvlData))// left top
            if(!isSolid(x+width,y+height,lvlData)) //right bottom
                if(!isSolid(x+width,y,lvlData)) // right top
                    if(!isSolid(x,y+height,lvlData)) // left bottom
                        return true;
        return false;
         */



        /*if(!isSolid(x,y,lvlData))// left top
            if(!isSolid(x,y+(int)(height/2) + 1,lvlData)) // left middle
                if(!isSolid(x,y+height,lvlData)) // left bottom

                    if(!isSolid(x+(int)(width/2) + 1,y+height,lvlData)) // middle bottom

                        if(!isSolid(x+width,y+height,lvlData)) // right bottom
                            if(!isSolid(x+width,y+(int)(height/2) + 1,lvlData)) // right mid
                                if(!isSolid(x+width,y,lvlData)) // right top
                                    if(!isSolid(x+(int)(width/2) + 1,y,lvlData)) // top mid
                                        return true;
        return false;
         */


    }

    public static boolean isSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;

        if (x < 0 || x >= maxWidth) {
            return true;
        }
        if (y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }
        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        //getting value of the tile that our player is interacting with

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);

    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currTile = (int) (hitbox.x / Game.TILES_SIZE);
        int tileXPos = currTile * Game.TILES_SIZE;
        if (xSpeed > 0) {
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
            //right
        } else {
            return tileXPos;
            //Left
        }
    }

    public static float GetEntityYPosUnderOrAbove(Rectangle2D.Float hitbox, float airSpeed) {
        int currTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (hitbox.height >= Game.TILES_SIZE) {
            currTile++;
        }

        int tileYPos = currTile * Game.TILES_SIZE;
        if (airSpeed > 0) {
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1; // +Game.TILES_SIZE works but cant figure out why;
            //FIXED
            //Falling
        } else {
            return tileYPos;
            //Jumping
        }

    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        //Check the pixel below bottomleft and bottomright
        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;

    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if(xSpeed > 0)
            return isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
        //For Right Side because the hitbox is basically on the leftSide
        else
            return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }


    public static boolean IsAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData) {

        for (int i = 0; i <= xEnd - xStart; ++i) {
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
            if(!IsTileSolid(xStart + i, y + 1, lvlData)) //For the block under the player and enemy
                return false;
        }
        return true;
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[(int) yTile][(int) xTile];
        if (value >= 99 || value < 0 || value == 1 || value == 9 || value == 10 || value == 14 || value == 7 || value == 20 || value == 30 || value == 42 || value == 5 || value == 61) // trebuie modificat value == 31 am pus doar pentru tile-ul de grass !
        {
            return true;
        }
        /*if (value >= 56 && value <= 70) {

            //surface of the acid
            return true;
        }

         */
        return false;

    }

    public static boolean TileOfDeath(int xTile,int yTile,int [][] lvlData)
    {
        int value = lvlData[(int) yTile][(int) xTile];

        if (value >= 56 && value <= 70) {
            //surface of the acid
            return true;
        }
        return false;

    }

    public static boolean TileOfSlow(int xTile,int yTile,int [][] lvlData)
    {
        int value = lvlData[(int) yTile][(int) xTile];

        if (value >= 80 && value <= 83) {
            //surface of the acid
            return true;
        }
        return false;

    }

    public static boolean HealTile(int xTile,int yTile,int [][] lvlData)
    {
        int value = lvlData[(int) yTile][(int) xTile];

        if (value == 13) {
            //surface of the acid
            return true;
        }
        return false;
    }

    public static boolean TileOfJump(int xTile,int yTile,int [][] lvlData)
    {
        int value = lvlData[(int) yTile + 1][(int) xTile];

        if (value == 61) {
            return true;
        }
        return false;

    }


    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int tileY) {
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile;

        if(isSolid(firstHitbox.x,firstHitbox.y + firstHitbox.height, lvlData))
            secondXTile = (int)(firstHitbox.x / Game.TILES_SIZE);
        else
            secondXTile = (int) ((firstHitbox.x + firstHitbox.width)/Game.TILES_SIZE);

        if (firstXTile > secondXTile) {
            return IsAllTileWalkable(secondXTile, firstXTile, tileY, lvlData);
        }
        else {
            return IsAllTileWalkable(firstXTile, secondXTile, tileY, lvlData);
        }
    }
}