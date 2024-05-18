package Entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilities.Constants.EnemyConstants.*;

public abstract class Entity {

    protected float x,y;
    protected int width,height;

    protected Rectangle2D.Float hitbox;
    public Entity(float x,float y,int height,int width)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        //initHitbox(x,y,width,height);
    }

    protected void drawHitbox(Graphics g,int lvlOffset)
    {
        // For viewing the hitbox
        g.setColor(Color.RED);
        g.drawRect((int)(hitbox.x - lvlOffset) ,(int)(hitbox.y),(int)hitbox.width,(int)(hitbox.height));
    }


    protected void initHitbox(float x,float y, int width, int height) {
        hitbox = new Rectangle2D.Float(x,y,width,height);

    }
    /*protected void updateHitbox()
    {
        hitbox.x = (int)x;
        hitbox.y = (int)y;
    }*/
    public Rectangle2D.Float getHitbox()
    {
        return hitbox;
    }

}
