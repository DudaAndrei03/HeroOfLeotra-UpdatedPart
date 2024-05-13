package Entities;

import GameStates.Playing;
import Main.Game;
import utilities.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilities.Constants.EnemyConstants.*;
import static utilities.Constants.Directions.*;

public class EnemyManager {

    private static int enemies = 0;

    private static int numberEnemiesLVL1;
    private BufferedImage[][] Skeleton_arr;
    private Playing playing;

    private ArrayList<Skeleton> skeletons= new ArrayList<>(); //Compozitie <=> Composite
    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        skeletons = LoadSave.GetSkeletons();
        enemies += skeletons.size();
        numberEnemiesLVL1 = enemies; // specifically for enemiesonLVL1

        System.out.println("Number of skeletons:" +  skeletons.size());
    }

    private void loadEnemyImgs() {
        Skeleton_arr = new BufferedImage[12][8];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SKELETON);

        for(int j = 0 ; j < Skeleton_arr.length; ++j)
        {
            for(int i = 0 ; i < Skeleton_arr[0].length; ++i)
            {
                Skeleton_arr[j][i] = temp.getSubimage(i * 64, j * 64,64,64);
            }
        }

    }

    public void draw(Graphics g,int xlevelOffset) {
        drawSkeletons(g,xlevelOffset);
    }

    private void drawSkeletons(Graphics g,int xlevelOffset) {
        for (Skeleton sk : skeletons) {
            if (sk.isActive()) {
                {
                    if (sk.getAnimationIndex() == GetSpriteAmount(SKELETON, sk.getEnemystate())) {
                        sk.setAnimationIndex(0);
                    }

                    if (sk.getEnemystate() != ATTACKING) {
                        if(sk.getEnemystate() == DEAD)
                        {
                            g.drawImage(Skeleton_arr[sk.getEnemystate()][sk.getAnimationIndex()], (int) (sk.hitbox.x - SKELETON_DRAWOFFSET_X) - xlevelOffset, (int) (sk.hitbox.y - SKELETON_DRAWOFFSET_Y), (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), null);
                            return;
                        }

                        if (sk.getWalkDir() == LEFT) {
                            g.drawImage(Skeleton_arr[sk.getEnemystate()][sk.getAnimationIndex()], (int) (sk.hitbox.x - SKELETON_DRAWOFFSET_X) - xlevelOffset, (int) (sk.hitbox.y - SKELETON_DRAWOFFSET_Y), (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), null);
                        } else if (sk.getWalkDir() == RIGHT) {
                            g.drawImage(Skeleton_arr[sk.getEnemystate() + 2][sk.getAnimationIndex()], (int) (sk.hitbox.x - SKELETON_DRAWOFFSET_X) - xlevelOffset, (int) (sk.hitbox.y - SKELETON_DRAWOFFSET_Y), (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), null);
                        }
                    } else if (sk.getEnemystate() == ATTACKING) {
                        if (sk.getWalkDir() == LEFT) {
                            g.drawImage(Skeleton_arr[sk.getEnemystate()][sk.getAnimationIndex()], (int) (sk.hitbox.x - SKELETON_DRAWOFFSET_X) - xlevelOffset, (int) (sk.hitbox.y - SKELETON_DRAWOFFSET_Y), (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), null);
                        } else
                            g.drawImage(Skeleton_arr[sk.getEnemystate() + 1][sk.getAnimationIndex()], (int) (sk.hitbox.x - SKELETON_DRAWOFFSET_X) - xlevelOffset, (int) (sk.hitbox.y - SKELETON_DRAWOFFSET_Y), (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), null);

                    }
                    //sk.drawHitbox(g, xlevelOffset);
                    //sk.drawAttackBox(g, xlevelOffset);

                }
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackHitBox) {
        for (Skeleton sk : skeletons) {
            if (sk.isActive()) {
                if (attackHitBox.intersects(sk.getHitbox())) {
                    //Mecanica de critica pentru player 30% sa dea o critica
                    if (sk.getCurrentHealth() > 0) {
                        double critical_hit = Math.random();

                        if (critical_hit < 0.7) // HIT NORMAL
                            sk.hurt(15);
                        else {
                            sk.hurt(30);
                            System.out.println("AI DAT O CRITICA!");
                        }
                    }
                }
            }
        }
    }

    public void update(int [][] lvlData,Player player)
    {
        for(Skeleton sk : skeletons)
        {
            if(sk.isActive()) {
                sk.update(lvlData, player);
            }
        }
    }

    public void resetAllEnemies()
    {
        for(Skeleton sk: skeletons)
        {
            sk.resetEnemy();
        }
        enemies = numberEnemiesLVL1;
    }
    public static int getEnemies()
    {
        return enemies;
    }
    public static void setEnemies(int newEnemies)
    {
        enemies = newEnemies;
    }
}
