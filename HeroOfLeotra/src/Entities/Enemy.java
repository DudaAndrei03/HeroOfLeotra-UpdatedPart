package Entities;
import Main.Game;

import javax.swing.*;
import java.awt.geom.Rectangle2D;

import static utilities.Constants.EnemyConstants.*;
import static utilities.Constants.GetEnemyDMG;
import static utilities.Constants.getMaxHealth;
import static utilities.Help.*;
import static utilities.Constants.Directions.*;

public abstract class Enemy extends Entity {

    protected boolean inAir;

    protected float fallspeed;

    protected float walkSpeed = 0.55f * Game.SCALE;

    protected int walkDir = LEFT;

    protected float gravity = 0.04f * Game.SCALE;

    protected int animationIndex, enemyState = 1, enemyType;
    protected int animationTick, animationSpeed = 25, tileY;

    protected float attackDistance = Game.TILES_SIZE;

    protected boolean firstUpdate = true;

    protected int maxHealth, currentHealth;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int height, int width, int enemyType) {
        super(x, y, height, width);
        this.enemyType = enemyType;
        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    protected void updateinAir(int [][] lvlData)
    {
        if (CanMoveHere(hitbox.x, hitbox.y + fallspeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += fallspeed;
            fallspeed += gravity;
        }
        else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderOrAbove(hitbox, fallspeed);
            tileY = (int)(hitbox.y/Game.TILES_SIZE);
        }
    }
    protected void Move(int [][] lvlData)
    {
        float xSpeed = 0;
        if(walkDir == LEFT)
        {
            xSpeed = -walkSpeed;
        }
        else {
            xSpeed = walkSpeed;
        }
        if(CanMoveHere(hitbox.x + xSpeed,hitbox.y,hitbox.width,hitbox.height,lvlData)) {
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        }
        changeWalkDir();
    }
    protected void updateAnimationTick()
    {
        if(enemyState == DEAD)
        {
            animationSpeed = 35;
        }
        animationTick++;
        if(animationTick >=animationSpeed)
        {
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteAmount(enemyType,enemyState))
            {
                //System.out.println(GetSpriteAmount((enemyType),animationIndex));
                animationIndex = 0;
                switch(enemyState)
                {
                    case ATTACKING,HIT:
                        enemyState = IDLE;
                    break;

                    case DEAD:
                        active = false;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void update(int [][]lvlData) {
        updateMove(lvlData);
        updateAnimationTick();
    }

    private void updateMove(int [][]lvlData)
    {
        if(firstUpdate) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
                firstUpdate = false;
            }
            if (inAir) {
                if (CanMoveHere(hitbox.x, hitbox.y + fallspeed, hitbox.width, hitbox.height, lvlData)) {
                    hitbox.y += fallspeed;
                    fallspeed += gravity;
                }
            else {
                inAir = false;
                hitbox.y = GetEntityYPosUnderOrAbove(hitbox, fallspeed);
            }
        }
            else {
            switch(enemyState)
            {
                case IDLE:
                    enemyState = RUNNING;
                    break;
                case RUNNING:
                    float xSpeed = 0;
                    if(walkDir == LEFT)
                    {
                        xSpeed = -walkSpeed;
                    }
                    else {
                        xSpeed = walkSpeed;
                    }
                    if(CanMoveHere(hitbox.x + xSpeed,hitbox.y,hitbox.width,hitbox.height,lvlData)) {
                        if (IsFloor(hitbox, xSpeed, lvlData)) {
                            hitbox.x += xSpeed;
                            return;
                        }
                    }
                    changeWalkDir();
                    break;
            }
        }

    }
    public void hurt(int amount)
    {
        currentHealth -= amount;
        if(currentHealth <=0)
        {
            newState(DEAD);
            int enemies = EnemyManager.getEnemies();
            EnemyManager.setEnemies(--enemies);
        }
        else {
            //newState(HIT);
        }
    }
    public int getCurrentHealth()
    {
        return currentHealth;
    }
    protected void checkEnemyHit(Rectangle2D.Float attackHitBox,Player player)
    {
        if(attackHitBox.intersects(player.hitbox))
        {
            double critical_hit = Math.random();
            if(critical_hit > 0.85) {
                switch (enemyType) {
                    case SKELETON:
                        player.changeHealth((int) (-GetEnemyDMG(enemyType) * 1.5));
                        System.out.println("INAMICUL A DAT O LOVITURA CRITICA!");
                        break;
                    default:
                        player.changeHealth((int)(-GetEnemyDMG(enemyType) * 1.25));
                }
            }
            else
                player.changeHealth((-GetEnemyDMG(enemyType)));
            attackChecked = true;
        }
    }

    protected void changeWalkDir() {
        if(walkDir == LEFT)
        {
            walkDir = RIGHT;
        }
        else {
            walkDir = LEFT;
        }
    }
    protected int getWalkDir()
    {
        return walkDir;
    }

    public int getAnimationIndex()
    {
        return animationIndex;
    }
    public int getEnemystate()
    {
        return enemyState;
    }
    public void setAnimationIndex(int value)
    {
        this.animationIndex = value;
    }

    protected void newState(int enemyState)
    {
        this.enemyState = enemyState;
        animationTick = 0;
        animationIndex = 0;
        //Fresh animation whenever changing states
    }

    protected boolean CanSeePlayer(int [][]lvlData,Player player)
    {
        int playerTileY = (int)player.getHitbox().y / Game.TILES_SIZE;
        if(playerTileY == tileY)
        {
            if(isPlayerNearby(player))
            {
                if(IsSightClear(lvlData,hitbox,player.hitbox,tileY))
                {
                    //System.out.println("True");
                    return true;
                }
            }
        }
        return false;
    }


    protected boolean isPlayerNearby(Player player) {
        int rangeConstant = 3;
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * rangeConstant;

    }

    protected boolean isPlayerAttackable(Player player)
    {
        int attackConstant = 1;
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * attackConstant;
    }
    protected void followPlayer(Player player)
    {
        if(player.hitbox.x > hitbox.x)
        {
            walkDir = RIGHT;
        }
        else
            walkDir = LEFT;
    }

    public boolean isActive()
    {
        return active;
    }

    public void resetEnemy()
    {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        fallspeed = 0;
    }


}
