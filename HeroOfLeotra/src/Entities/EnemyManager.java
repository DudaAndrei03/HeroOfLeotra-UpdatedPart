package Entities;

import GameStates.Playing;
import Levels.LevelManager;
import Main.Game;
import utilities.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.sql.SQLOutput;
import java.util.ArrayList;

import static utilities.Constants.EnemyConstants.*;
import static utilities.Constants.Directions.*;

public class EnemyManager {

    private static int enemies = 0;

    private static int numberEnemies;
    private BufferedImage[][] Skeleton_arr;

    private BufferedImage [][] Knight_arr;

    private BufferedImage [][] King_arr;
    private Playing playing;

    private ArrayList<Skeleton> skeletons= new ArrayList<>(); //Compozitie <=> Composite

    private ArrayList<GoldenKnight> knights = new ArrayList<>();

    private ArrayList<King> kings = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        loadEnemies();
    }

    public void loadEnemies() {
        clearEnemies();

        loadSkeletons();
        loadKnights();
        loadKings();
        //System.out.println(skeletons.size());
        //System.out.println("LOAD ENEMIES APELAT!");

        enemies += skeletons.size() + knights.size() + kings.size();
        numberEnemies = enemies; // specifically for enemies onLVL1

        //System.out.println("Number of skeletons:" +  skeletons.size());
    }


    public void clearEnemies()
    {
        skeletons.clear();
        knights.clear();
        kings.clear();
    }

    private void loadKings() {
        kings = LoadSave.GetKings(playing.getLevelManager().getCurrentLevel().getLevelData());

    }
    public void loadSkeletons()
    {
        skeletons = LoadSave.GetSkeletons(playing.getLevelManager().getCurrentLevel().getLevelData());
    }
    private void loadKnights() {
        knights = LoadSave.GetKnights(playing.getLevelManager().getCurrentLevel().getLevelData());
    }

    /*public void updateEnemies()
    {
        skeletons = LoadSave.GetSkeletons();
        enemies += skeletons.size();
        numberEnemiesLVL1 = enemies;
    }*/

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

        BufferedImage temp2 = LoadSave.GetSpriteAtlas(LoadSave.KNIGHT);
        Knight_arr = new BufferedImage[10][8];

        for(int j = 0; j < Knight_arr.length; ++j)
        {
            for(int i = 0; i < Knight_arr[0].length; ++i)
            {
                Knight_arr[j][i] = temp2.getSubimage(i*64,j*64,64,64);
            }
        }

        BufferedImage temp3 = LoadSave.GetSpriteAtlas(LoadSave.KING);
        King_arr = new BufferedImage[9][8];

        for(int j = 0; j < King_arr.length;++j)
        {
            for(int i = 0; i < King_arr[0].length; ++i)
            {
                King_arr[j][i] = temp3.getSubimage(i*64,j*64,64,64);
            }
        }

    }

    public void draw(Graphics g,int xlevelOffset) {
        drawKnights(g,xlevelOffset);
        drawSkeletons(g,xlevelOffset);
        drawKings(g,xlevelOffset);
    }


    private void drawSkeletons(Graphics g,int xlevelOffset) {
        for (Skeleton sk : skeletons) {
            if (sk.isActive()) {
                {
                    if (sk.getAnimationIndex() == GetSpriteAmount(SKELETON, sk.getEnemystate())) {
                        sk.setAnimationIndex(0);
                    }

                    if (sk.getEnemystate() != SK_ATTACKING) {
                        if(sk.getEnemystate() == SK_DEAD)
                        {
                            g.drawImage(Skeleton_arr[sk.getEnemystate()][sk.getAnimationIndex()], (int) (sk.hitbox.x - SKELETON_DRAWOFFSET_X) - xlevelOffset, (int) (sk.hitbox.y - SKELETON_DRAWOFFSET_Y), (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), null);
                        }
                        else {
                            if (sk.getWalkDir() == LEFT) {
                                g.drawImage(Skeleton_arr[sk.getEnemystate()][sk.getAnimationIndex()], (int) (sk.hitbox.x - SKELETON_DRAWOFFSET_X) - xlevelOffset, (int) (sk.hitbox.y - SKELETON_DRAWOFFSET_Y), (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), null);
                            } else if (sk.getWalkDir() == RIGHT) {
                                g.drawImage(Skeleton_arr[sk.getEnemystate() + 2][sk.getAnimationIndex()], (int) (sk.hitbox.x - SKELETON_DRAWOFFSET_X) - xlevelOffset, (int) (sk.hitbox.y - SKELETON_DRAWOFFSET_Y), (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), null);
                            }
                        }
                    } else if (sk.getEnemystate() == SK_ATTACKING) {
                        if (sk.getWalkDir() == LEFT) {
                            g.drawImage(Skeleton_arr[sk.getEnemystate()][sk.getAnimationIndex()], (int) (sk.hitbox.x - SKELETON_DRAWOFFSET_X) - xlevelOffset, (int) (sk.hitbox.y - SKELETON_DRAWOFFSET_Y), (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), null);
                        } else
                            g.drawImage(Skeleton_arr[sk.getEnemystate() + 1][sk.getAnimationIndex()], (int) (sk.hitbox.x - SKELETON_DRAWOFFSET_X) - xlevelOffset, (int) (sk.hitbox.y - SKELETON_DRAWOFFSET_Y), (int) (Game.SCALE * 48), (int) (Game.SCALE * 48), null);

                    }
                    //sk.drawHitbox(g, xlevelOffset);
                    //sk.drawAttackBox(g, xlevelOffset);
                    sk.drawUI(g,xlevelOffset);
                }
            }
        }
    }

    private void drawKnights(Graphics g,int xlevelOffset) {
        for (GoldenKnight kn : knights) {
            if (kn.isActive()) {
                {
                    if (kn.getAnimationIndex() == GetSpriteAmount(KNIGHT, kn.getEnemystate())) {
                        kn.setAnimationIndex(0);
                    }

                    if (kn.getEnemystate() != KN_ATTACKING) {
                        if(kn.getEnemystate() == KN_DEAD)
                        {
                            g.drawImage(Knight_arr[kn.getEnemystate()][kn.getAnimationIndex()], (int) (kn.hitbox.x - KNIGHT_DRAWOFFSET_X) - xlevelOffset, (int) (kn.hitbox.y - KNIGHT_DRAWOFFSET_Y), (int) (Game.SCALE * KNIGHT_WIDTH), (int) (Game.SCALE * KNIGHT_HEIGHT), null);
                        }
                        else {

                            if (kn.getWalkDir() == LEFT) {
                                g.drawImage(Knight_arr[kn.getEnemystate()][kn.getAnimationIndex()], (int) (kn.hitbox.x - KNIGHT_DRAWOFFSET_X) - xlevelOffset, (int) (kn.hitbox.y - KNIGHT_DRAWOFFSET_Y), (int) (Game.SCALE * KNIGHT_WIDTH), (int) (Game.SCALE * KNIGHT_HEIGHT), null);
                            } else if (kn.getWalkDir() == RIGHT) {
                                g.drawImage(Knight_arr[kn.getEnemystate() + 2][kn.getAnimationIndex()], (int) (kn.hitbox.x - KNIGHT_DRAWOFFSET_X) - xlevelOffset, (int) (kn.hitbox.y - KNIGHT_DRAWOFFSET_Y), (int) (Game.SCALE * KNIGHT_WIDTH), (int) (Game.SCALE * KNIGHT_HEIGHT), null);
                            }
                        }

                    } else if (kn.getEnemystate() == KN_ATTACKING) {
                        if (kn.getWalkDir() == LEFT) {
                            g.drawImage(Knight_arr[kn.getEnemystate()][kn.getAnimationIndex()], (int) (kn.hitbox.x - KNIGHT_DRAWOFFSET_X) - xlevelOffset, (int) (kn.hitbox.y - KNIGHT_DRAWOFFSET_Y), (int) (Game.SCALE * KNIGHT_WIDTH), (int) (Game.SCALE * KNIGHT_HEIGHT), null);
                        } else
                            g.drawImage(Knight_arr[kn.getEnemystate() + 1][kn.getAnimationIndex()], (int) (kn.hitbox.x - KNIGHT_DRAWOFFSET_X) - xlevelOffset, (int) (kn.hitbox.y - KNIGHT_DRAWOFFSET_Y), (int) (Game.SCALE * KNIGHT_WIDTH), (int) (Game.SCALE * KNIGHT_HEIGHT), null);

                    }
                    kn.drawUI(g,xlevelOffset);
                    //kn.drawHitbox(g, xlevelOffset);
                    //kn.drawAttackBox(g, xlevelOffset);

                }
            }
        }
    }

    private void drawKings(Graphics g, int xlevelOffset) {

        for (King kg : kings) {
            if (kg.isActive()) {
                {
                    if (kg.getAnimationIndex() == GetSpriteAmount(KING, kg.getEnemystate())) {
                        kg.setAnimationIndex(0);
                    }

                    if (kg.getEnemystate() != KG_ATTACKING) {
                        if(kg.getEnemystate() == KG_DEAD)
                        {
                            g.drawImage(King_arr[kg.getEnemystate()][kg.getAnimationIndex()], (int) (kg.hitbox.x - KING_DRAWOFFSET_X) - xlevelOffset, (int) (kg.hitbox.y - KING_DRAWOFFSET_Y), (int) (Game.SCALE * KING_WIDTH), (int) (Game.SCALE * KING_HEIGHT), null);
                        }
                        else {
                                if (kg.getWalkDir() == LEFT) {
                                    g.drawImage(King_arr[kg.getEnemystate()][kg.getAnimationIndex()], (int) (kg.hitbox.x - KING_DRAWOFFSET_X) - xlevelOffset, (int) (kg.hitbox.y - KING_DRAWOFFSET_Y), (int) (Game.SCALE * KING_WIDTH), (int) (Game.SCALE * KING_HEIGHT), null);
                                } else if (kg.getWalkDir() == RIGHT) {
                                    g.drawImage(King_arr[kg.getEnemystate() + 1][kg.getAnimationIndex()], (int) (kg.hitbox.x - KING_DRAWOFFSET_X) - xlevelOffset, (int) (kg.hitbox.y - KING_DRAWOFFSET_Y), (int) (Game.SCALE * KING_WIDTH), (int) (Game.SCALE * KING_HEIGHT), null);
                                }
                        }

                    } else if (kg.getEnemystate() == KG_ATTACKING) {
                        if (kg.getWalkDir() == LEFT) {
                            g.drawImage(King_arr[kg.getEnemystate()][kg.getAnimationIndex()], (int) (kg.hitbox.x - KING_DRAWOFFSET_X) - xlevelOffset, (int) (kg.hitbox.y - KING_DRAWOFFSET_Y), (int) (Game.SCALE * KING_WIDTH), (int) (Game.SCALE * KING_HEIGHT), null);
                        } else
                            g.drawImage(King_arr[kg.getEnemystate() + 1][kg.getAnimationIndex()], (int) (kg.hitbox.x - KING_DRAWOFFSET_X) - xlevelOffset, (int) (kg.hitbox.y - KING_DRAWOFFSET_Y), (int) (Game.SCALE * KING_WIDTH), (int) (Game.SCALE * KING_HEIGHT), null);

                    }
                    kg.drawUI(g,xlevelOffset);
                    //kg.drawHitbox(g, xlevelOffset);
                    //kg.drawAttackBox(g, xlevelOffset);

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

        for (GoldenKnight kn : knights) {
            if (kn.isActive()) {
                if (attackHitBox.intersects(kn.getHitbox())) {
                    //Mecanica de critica pentru player 30% sa dea o critica
                    if (kn.getCurrentHealth() > 0) {
                        double critical_hit = Math.random();

                        if (critical_hit < 0.7) // HIT NORMAL
                            kn.hurt(15);
                        else {
                            kn.hurt(25);
                            System.out.println("AI DAT O CRITICA!");
                        }
                    }
                }
            }
        }


        for (King kg : kings) {
            if (kg.isActive()) {
                if (attackHitBox.intersects(kg.getHitbox())) {
                    //Mecanica de critica pentru player 30% sa dea o critica
                    if (kg.getCurrentHealth() > 0) {
                        double critical_hit = Math.random();

                        if (critical_hit < 0.7) // HIT NORMAL
                            kg.hurt(20);
                        else {
                            kg.hurt(30);
                            System.out.println("AI DAT O CRITICA!");
                        }
                    }
                }
            }
        }
    }



    public void update(int [][] lvlData,Player player)
    {
        boolean isAnyActive = false;
        for(Skeleton sk : skeletons)
        {
            if(sk.isActive()) {
                sk.update(lvlData, player);
                isAnyActive = true;
            }
        }
        for(GoldenKnight kn : knights)
        {
            if(kn.isActive()) {
                kn.update(lvlData, player);
                isAnyActive = true;
            }
        }
        for(King kg : kings)
        {
            if(kg.isActive())
            {
                kg.update(lvlData,player);
                isAnyActive = true;
            }
        }

        if(!isAnyActive)
        {
            enemies = 0;
        }
        //addEnemies();
    }

    public void resetAllEnemies()
    {
        for(Skeleton sk: skeletons)
        {
            sk.resetEnemy();
        }
        for(GoldenKnight kn: knights)
        {
            kn.resetEnemy();
        }
        for(King kg: kings)
        {
            kg.resetEnemy();
        }
        enemies = numberEnemies;
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
