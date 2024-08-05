package Entities;
import Main.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilities.Constants.EnemyConstants.*;
import static utilities.Constants.EnemyConstants.KN_IDLE;
import static utilities.Constants.GetEnemyDMG;
import static utilities.Constants.getMaxHealth;
import static utilities.Help.*;
import static utilities.Constants.Directions.*;

public abstract class Enemy extends Entity {

    protected boolean inAir;

    protected float fallspeed;

    protected float healthBarWidth;

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

        this.healthBarWidth = maxHealth;

        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
    }
    public void drawUI(Graphics g,int xLevelOffset)
    {
        int moreOffsetX = 0;
        int moreOffsetY = 0;
        updateHealthBar();
        if(enemyType == SKELETON)
            g.setColor(Color.blue);
        else if(enemyType == KNIGHT)
            g.setColor(Color.yellow);
        else if(enemyType == KING) {
            g.setColor(Color.black);
            moreOffsetX = 40;
            moreOffsetY = 10;
        }

        g.fillRect((int)hitbox.x-xLevelOffset - moreOffsetX,(int)hitbox.y - 15 - moreOffsetY,(int)healthBarWidth,5);

        g.setColor(Color.black);
        g.drawRect((int)hitbox.x - xLevelOffset - moreOffsetX, (int)hitbox.y - 15 - moreOffsetY, (int)healthBarWidth, 5);

    }
    public void updateHealthBar()
    {
        healthBarWidth = currentHealth;
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
        float enemy_speed;

        switch(enemyType)
        {
            case SKELETON: enemy_speed = walkSpeed * 1;
            break;
            case KNIGHT: enemy_speed = (float) (walkSpeed * 2);
            break;
            case KING: enemy_speed = (float)(walkSpeed * 1.5);
            default:
                enemy_speed = walkSpeed * 1;
        }

        if(walkDir == LEFT)
        {
            xSpeed = -enemy_speed;
        }
        else {
            xSpeed = enemy_speed;
        }
        if(CanMoveHere(hitbox.x + xSpeed,hitbox.y,hitbox.width,hitbox.height,lvlData)) {
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        }
        changeWalkDir();
    }
    protected void updateAnimationTick_Skeletons()
    {
        if(enemyState == SK_DEAD)
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
                    case SK_ATTACKING,SK_HIT:
                        enemyState = SK_IDLE;
                    break;

                    case SK_DEAD:
                        active = false;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    protected void updateAnimationTick_Knights()
    {
        int animationSpeed_enemy = 20; //animation speed for every enemy;

        if(enemyState == KN_DEAD)
        {
            animationSpeed_enemy = 35;
        }
        animationTick++;
        if(animationTick >=animationSpeed_enemy)
        {
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteAmount(enemyType,enemyState))
            {
                //System.out.println(GetSpriteAmount((enemyType),animationIndex));
                animationIndex = 0;
                switch(enemyState)
                {
                    case KN_ATTACKING:
                        enemyState = KN_IDLE;
                        break;

                    case KN_DEAD:
                        active = false;
                        break;
                    default:
                        break;
                }
            }
        }
    }


    protected void updateAnimationTick_Kings()
    {
        int animationSpeed_enemy = 30; //animation speed for every enemy;

        if(enemyState == KG_DEAD)
        {
            animationSpeed_enemy = 35;
        }
        animationTick++;
        if(animationTick >=animationSpeed_enemy)
        {
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteAmount(enemyType,enemyState))
            {
                //System.out.println(GetSpriteAmount((enemyType),animationIndex));
                animationIndex = 0;
                switch(enemyState)
                {
                    case KG_ATTACKING:
                        enemyState = KG_IDLE;
                        break;

                    case KG_DEAD:
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
        updateAnimationTick_Skeletons();
        updateAnimationTick_Knights();
        updateAnimationTick_Kings();
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
                if (enemyType == SKELETON) {
            switch(enemyState) {
                    case SK_IDLE:
                        enemyState = SK_RUNNING;
                        break;
                    case SK_RUNNING:
                        float xSpeed = 0;
                        if (walkDir == LEFT) {
                            xSpeed = -walkSpeed;
                        } else {
                            xSpeed = walkSpeed;
                        }
                        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
                            if (IsFloor(hitbox, xSpeed, lvlData)) {
                                hitbox.x += xSpeed;
                                return;
                            }
                        }
                        changeWalkDir();
                        break;
                }
            }
                else if(enemyType == KNIGHT)
                {
                    switch(enemyState) {
                        case KN_IDLE:
                            enemyState = KN_RUNNING;
                            break;
                        case KN_RUNNING:
                            float xSpeed = 0;
                            if (walkDir == LEFT) {
                                xSpeed = -walkSpeed;
                            } else {
                                xSpeed = walkSpeed;
                            }
                            if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
                                if (IsFloor(hitbox, xSpeed, lvlData)) {
                                    hitbox.x += xSpeed;
                                    return;
                                }
                            }
                            changeWalkDir();
                            break;
                    }
                }

                else if(enemyType == KING)
                {
                    switch(enemyState) {
                        case KG_IDLE:
                            enemyState = KG_RUNNING;
                            break;
                        case KG_RUNNING:
                            float xSpeed = 0;
                            if (walkDir == LEFT) {
                                xSpeed = -walkSpeed;
                            } else {
                                xSpeed = walkSpeed;
                            }
                            if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
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

    }
    public void hurt(int amount)
    {
        currentHealth -= amount;
        if(currentHealth <=0)
        {

            if(enemyType == SKELETON) {
                newState(SK_DEAD);
                Player.updateScore(10);
            }
            else if(enemyType == KNIGHT) {
                newState(KN_DEAD);
                Player.updateScore(25);
            }
            else if(enemyType == KING)
            {
                newState(KG_DEAD);
                Player.updateScore(300);
            }

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
                    case KNIGHT:
                        player.changeHealth((int)(-GetEnemyDMG(enemyType) * 1.5));
                    default:
                        player.changeHealth((int)(-GetEnemyDMG(enemyType) * 1.25));
                }
            }
            else
                player.changeHealth((-GetEnemyDMG(enemyType)));
            //Lovitura normala;

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
        if(enemyType == SKELETON)
            newState(SK_IDLE);
        else if(enemyType == KNIGHT)
            newState(KN_IDLE);
        else if(enemyType == KING)
            newState(KG_IDLE);

        active = true;
        fallspeed = 0;
    }


}
