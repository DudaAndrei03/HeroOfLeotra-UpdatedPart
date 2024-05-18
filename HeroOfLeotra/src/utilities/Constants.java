package utilities;

import Main.Game;


public class Constants {

    public static class UI{
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 140; //330
            public static final int B_HEIGHT_DEFAULT = 56; // 130
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);

        }
        public static class URM_Buttons{
            public static final int URM_DEFAULT_SIZE = 160;
            public static final int URM_SIZE = (int)(40 * Game.SCALE);
        }
    }

    public static int getMaxHealth(int enemytype)
    {
        switch(enemytype)
        {
            case EnemyConstants.SKELETON:
                return 30;
            default:
                return 0;
        }
    }

    public static int GetEnemyDMG(int enemytype)
    {
        switch(enemytype)
        {
            case EnemyConstants.SKELETON:
                return 20;
            default:
                return 0;
        }
    }


    public static class Environment{
        public static final int MOUNTAIN_FRONT_WIDTH_DEFAULT = 544;
        public static final int MOUNTAIN_FRONT_HEIGHT_DEFAULT = 160;

        public static final int MOUNTAIN_FRONT_WIDTH = (int)(MOUNTAIN_FRONT_WIDTH_DEFAULT * Game.SCALE);
        public static final int MOUNTAIN_FRONT_HEIGHT = (int)(MOUNTAIN_FRONT_WIDTH_DEFAULT * Game.SCALE);


        public static final int TREES_BACK_WIDTH_DEFAULT = 544;
        public static final int TREES_BACK_HEIGHT_DEFAULT = 160;

        public static final int TREES_BACK_WIDTH = (int)(TREES_BACK_WIDTH_DEFAULT * Game.SCALE);
        public static final int TREES_BACK_HEIGHT = (int)(TREES_BACK_HEIGHT_DEFAULT * Game.SCALE);


        //MAP 2

        public static final int CLOSE_TREES_WIDTH_DEFAULT = 592;

        public static final int CLOSE_TREES_HEIGHT_DEFAULT = 272;

        public static final int CLOSE_TREES_WIDTH = (int)(CLOSE_TREES_WIDTH_DEFAULT * Game.SCALE);

        public static final int CLOSE_TREES_HEIGHT = (int)(CLOSE_TREES_HEIGHT_DEFAULT * Game.SCALE) + 220;





    }

    public static class EnemyConstants{
        public static final int SKELETON = 4; // o valoare la care nu se ajunge in tilenum
        public static final int IDLE = 1; //1 pt stanga
        public static final int RUNNING = 5; //7 pt dreapta , 5 pt stanga
        public static final int ATTACKING = 8;
        public static final int HIT = 9; // 9 pt dreapta,  10 pt stanga

        public static final int DEAD = 10;

        public static final int SKELETON_WIDTH_DEFAULT = 16;
        public static final int SKELETON_HEIGHT_DEFAULT = 28;

        public static final int SKELETON_WIDTH = (int)(SKELETON_WIDTH_DEFAULT * Game.SCALE);
        public static final int SKELETON_HEIGHT = (int)(SKELETON_HEIGHT_DEFAULT * Game.SCALE);

        public static final int SKELETON_DRAWOFFSET_X = (int)(14 * Game.SCALE);
        public static final int SKELETON_DRAWOFFSET_Y = (int)(8 * Game.SCALE);


        public static final int KNIGHT_WIDTH_DEFAULT = 16;
        public static final int KNIGHT_HEIGHT_DEFAULT = 28;

        public static final int KNIGHT_WIDTH = (int)(KNIGHT_WIDTH_DEFAULT * Game.SCALE);
        public static final int KNIGHT_HEIGHT = (int)(KNIGHT_HEIGHT_DEFAULT * Game.SCALE);

        public static final int KNIGHT_DRAWOFFSET_X = (int)(14 * Game.SCALE);
        public static final int KNIGHT_DRAWOFFSET_Y = (int)(8 * Game.SCALE);



        public static int GetSpriteAmount(int enemyType,int enemyState)
        {
            switch(enemyType)
            {
                case SKELETON:
                    switch(enemyState)
                    {
                        case IDLE:
                            return 2;

                        case RUNNING:
                            return 8;

                        case ATTACKING:
                            return 5;

                        case DEAD:
                            return 5;

                        default:
                            return 2;

                    }
            }
            return 0;
        }




    }

    public static class Directions{
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int DOWN = 2;
        public static final int RIGHT = 3;

    }
    public static class PlayerConstants{
        public static final int RUNNING = 11;
        public static final int IDLE = 3;

        public static final int ATTACKING = 15;
        public static final int DYING = 20;


        public static int GetSpriteAmount(int player_action)
        {
            switch(player_action)
            {
                case RUNNING:
                    return 8;
                case IDLE:
                    return 2;
                    // o problema la IDLE cand ridica mana caracterul n-ar trebui
                case DYING:
                    return 5;
                case ATTACKING:
                    return 7;
                default:
                    return 1;
            }
        }

    }
}
