package Entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilities.Constants.Directions.LEFT;
import static utilities.Constants.Directions.RIGHT;
import static utilities.Constants.EnemyConstants.*;
import static utilities.Help.*;

public class King extends Enemy{
    private Rectangle2D.Float attackHitBox;
    private int attackBoxOffsetX;


    public King(float x, float y) {
        super(x, y, KING_WIDTH, KING_HEIGHT, KING);
        initHitbox(x,y,(int)(15* Game.SCALE),(int)(28* Game.SCALE));
        initAttackBox();
    }

    private void initAttackBox()
    {
        attackHitBox = new Rectangle2D.Float(x,y,40*Game.SCALE,20*Game.SCALE);
        attackBoxOffsetX=(int)(Game.SCALE * 10);
    }
    private void updateAttackBox() {
        if(walkDir == RIGHT)
        {
            attackHitBox.x = hitbox.x + (hitbox.width) + (int)(Game.SCALE * 3);
        }
        if(walkDir == LEFT)
        {
            attackHitBox.x = hitbox.x - (hitbox.width) - (int)(Game.SCALE * 8);
        }
        attackHitBox.y = hitbox.y + (Game.SCALE * 10);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset)
    {
        g.setColor(Color.PINK);
        g.drawRect((int)attackHitBox.x - xLvlOffset, (int)attackHitBox.y, (int)attackHitBox.width,(int)attackHitBox.height);
    }

    private void updateBehaviour(int [][]lvlData,Player player)
    {
        if(firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir) {
            updateinAir(lvlData);
        }
        else {
            switch(enemyState)
            {
                case KG_IDLE:
                    newState(KG_RUNNING);
                    break;
                case KG_RUNNING:
                    if(CanSeePlayer(lvlData,player)) {
                        followPlayer(player);
                        if (isPlayerAttackable(player))
                            newState(KG_ATTACKING);
                    }

                    Move(lvlData);
                    break;
                case KG_ATTACKING:
                    if(animationIndex == 0)
                    {
                        attackChecked = false;
                    }

                    if(animationIndex == 2 && !attackChecked)
                    {
                        checkEnemyHit(attackHitBox,player);
                    }
            }
        }

    }

    public void updateHealth()
    {
        hurt(1);
    }
    public void update(int [][]lvlData,Player player) {
        updateBehaviour(lvlData,player);
        updateAnimationTick_Kings();
        updateAttackBox();
    }

}
