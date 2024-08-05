package Entities;

import GameStates.Playing;
import Main.Game;
import utilities.Constants;
import utilities.LoadSave;
import static utilities.Help.CanMoveHere;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilities.Constants.Directions.*;
import static utilities.Constants.PlayerConstants.*;
import static utilities.Help.*;
import static utilities.Constants.Directions.*;

public class Player extends Entity{

    private static Player instance;

    public enum Direction{
        RIGHT,LEFT;
    }
    Direction direction = null;


    private static int Score = 0;
    private int health_potion = 15;
    private BufferedImage[][] Animations;
    private int animationTick = 0,animationIndex = 0,animationSpeed = 30;
    private int playerAction = IDLE;
    private boolean moving = false,attacking = false;

    private boolean firstUpdate = true;

    private boolean left,up,right,down,jump;
    private float playerSpeed = 0.66f * Game.SCALE;

    private int [][] lvlData;
    private float xDrawOffset = 16 * Game.SCALE;
    private float yDrawOffset = 8 * Game.SCALE;

    //Jump and Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.75f * Game.SCALE;

    private float superjumpSpeed = -4.05f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    private boolean attackChecked = false;
    private Playing playing;


    //StatusBarUI
    //600x500

    private BufferedImage statusBarImg;

    private int healthBarWidth = (int)(150 * Game.SCALE);
    private int healthBarHeight = (int)(25 * Game.SCALE);
    private int healthBarX = (int)(10 * Game.SCALE);
    private int healthBarY = (int)(20 * Game.SCALE);

    private int maxHealth = 100;
    private int currentHealth = 100;
    private int healthWidth = currentHealth;

    //AttackHitBox
    private Rectangle2D.Float attackHitBox;


    private Player(float x, float y,int height,int width,Playing playing) {
        super(x, y,height,width);
        this.height=height;
        this.width=width;
        this.playing = playing;
        loadAnimations();
        initHitbox(x,y,(int)(16* Game.SCALE),(int)(28* Game.SCALE));
        initAttackBox();
        //The hitbox height has been reduced from original 40*G.Scale to 28*G.Scale based on making the contact with the grass(block of 32x32)
        //as well as interacting well with the blocks that are 16x16 -> transformed into 32x32 in the actual map.The edge
        //of the hitbox cant go through any tile now.

        //Still have a problem with collisions if the height of the initHitbox is not the same as the tileSize
        //
    }

    private void initAttackBox() {
        attackHitBox = new Rectangle2D.Float(x ,y,(int)(35*Game.SCALE),(int) (20*Game.SCALE));
    }

    public static Player getInstance(float x, float y, int height, int width,Playing playing) {
        if (instance == null) {
            // Create a new instance if it doesn't exist yet
            //Allows for only one to be created!
            instance = new Player(x, y, height, width,playing);
        }
        return instance;
    }

    public void update()
    {
        if(currentHealth <= 0)
        {
            playing.setGameOver(true);
            return;
        }

        updateHealthBar();
        updateAttackBox();
        updatePos();
        if(attacking)
        {
            checkAttack();
        }
        //updateHitbox();
        updateAnimationTick();
        setAnimation();
    }

    private void checkAttack() {
        if(attackChecked || animationIndex != 3)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackHitBox);
    }

    private void updateAttackBox() {
        if(right)
        {
            attackHitBox.x = hitbox.x + hitbox.width/2 + (Game.SCALE * 2);
        }
        if(left)
        {
            attackHitBox.x = hitbox.x - hitbox.width/2 -  (Game.SCALE * 10);
        }
        attackHitBox.y = hitbox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth /(float)(maxHealth)) * (healthBarWidth - 52));
    }

    protected void changeHealth(int value)
    {
        currentHealth += value;
        if(currentHealth<=0)
        {
            currentHealth = 0;
            //gameOver();
        }
        else if(currentHealth >= maxHealth)
        {
            currentHealth = maxHealth;
        }
    }

    public void render(Graphics g,int lvlOffset)
    {
        //subImg = img.getSubimage(0 * 64,3 * 64,64,64);
        /*if(GetSpriteAmount(playerAction) <= animationIndex) {
            animationIndex = 0;
        }*/
        //cumva ajunge animationIndexu mai mare decat GetSpriteAmount
        // [11][6] -> [3][6] in loc sa puna animationIndexu pe 0 mai intai si sa faca [3][0]



            if (direction == Direction.LEFT) {
                g.drawImage(Animations[playerAction - 2][animationIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
                //playerAction pentru left este pe 9, playerAction pentru Right este pe 7
            } else {
                g.drawImage(Animations[playerAction][animationIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
            }

        /*
        else if(playerAction == ATTACKING)
        {

            if (direction == Direction.LEFT) {
                g.drawImage(Animations[playerAction - 2][(animationIndex + 7)%12], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
                //playerAction pentru left este pe 9, playerAction pentru Right este pe 7
            } else {
                g.drawImage(Animations[playerAction][(animationIndex + 7)%12], (int) (hitbox.x - xDrawOffset) - lvlOffset, (int) (hitbox.y - yDrawOffset), width, height, null);
            }
        }
         */



        //drawHitbox(g,lvlOffset);
            //System.out.println("[" + playerAction + "]" + "[" + animationIndex + "]");
        drawUI(g);
        drawScore(g);
        //drawAttackHitBox(g,lvlOffset);
    }

    private void drawAttackHitBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.green);
        g.drawRect((int)attackHitBox.x - lvlOffsetX,(int)(attackHitBox.y),(int)(attackHitBox.width),(int)(attackHitBox.height));
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg,healthBarX,healthBarY,healthBarWidth,healthBarHeight,null);
        g.setColor(Color.red);
        g.fillRect(healthBarX+47,healthBarY+8,healthWidth,healthBarHeight-16);
        //Dreptunghiul pentru portiunea rosie
    }
    private void drawScore(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.gray);
        g2d.drawRect(Game.GAME_WIDTH - 200, 50, 120, 30); // un dreptunghi gri


        // Setează fontul pentru text (BOLD pentru "SCOR")
        Font fontBold = new Font("Serif", Font.BOLD, 18);
        g2d.setFont(fontBold);
        g2d.setColor(Color.white); // Culoarea textului

        // Desenează textul "SCOR"
        g2d.drawString("SCOR:"+ Score, Game.GAME_WIDTH - 180, 70);

        // Setează fontul pentru scor (regular)
        Font fontRegular = new Font("Serif", Font.PLAIN, 18);
        g2d.setFont(fontRegular);

    }


    private void updateAnimationTick() {
        animationTick++;
        if(playerAction == ATTACKING)
        {
            animationSpeed=15; //crestem animatia doar pe attack pentru a se efectua mai repede

        }
        else
        {
            animationSpeed=30;
        }

        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= GetSpriteAmount(playerAction)) {
                animationIndex = 0;
                attacking=false;
                attackChecked = false;
            }
        }
    }

    private void updatePos() {
        if (firstUpdate) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
            firstUpdate = false;
            if (inAir) {
                if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                    hitbox.y += airSpeed;
                    airSpeed += gravity;
                } else {
                    inAir = false;
                    hitbox.y = GetEntityYPosUnderOrAbove(hitbox, airSpeed);
                }
            }
        }
        moving = false;
        if(jump)
        {
            jump(0);
        }
        //Checking if we are not pressing anything at all
        if (!left && !right && !inAir)
            return;
        float xSpeed = 0;

        /*if(up && right || up && left)
        {
            if(right)
            {
                x+=playerSpeed*(1.41); // pitagora
            }
            if(left)
            {
                x-=playerSpeed*(1.41);
            }
            y-=playerSpeed*(1.41);
        }

        else if(down && right || down && left)
        {
            if(right)
            {
                x+=playerSpeed*(1.41); // pitagora
            }
            if(left)
            {
                x-=playerSpeed*(1.41);
            }
            y+=playerSpeed*(1.41);
        }
        */

            if (left) {
                xSpeed -= playerSpeed;
                direction = Direction.LEFT;
            }
            else if (right) {
                xSpeed += playerSpeed;
                direction = Direction.RIGHT;
            }
            if(!inAir)
            {
                if(!IsEntityOnFloor(hitbox,lvlData))
                {
                    inAir = true;
                }
            }
            if(inAir) {
                if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                    hitbox.y += airSpeed;
                    airSpeed += gravity;
                    updateXPos(xSpeed);
                } else {
                    hitbox.y = GetEntityYPosUnderOrAbove(hitbox, airSpeed);
                    if (airSpeed > 0) {
                        resetInAir();
                    } else {
                        airSpeed = fallSpeedAfterCollision;
                        updateXPos(xSpeed);
                    }
                }
            }

            else{
                updateXPos(xSpeed);
            }
            moving = true;

            /*if (up && !down) {
                ySpeed -= playerSpeed;
                moving = true;
            } else if (down && !up) {
                ySpeed += playerSpeed;
                moving = true;
            }*/

            /*if(CanMoveHere(hitbox.x+xSpeed,hitbox.y+ySpeed,hitbox.width,hitbox.height,lvlData))
            {
                hitbox.x += xSpeed;
                hitbox.y += ySpeed;
                moving = true;
            }*/


            //we use xSpeed , ySpeed to store the speed received from keyboard inputs and before
            //we store it in the x and y values of the player we check if the area that we want to go in is solid or not
        }


    private void jump(int superPower) {
        if(inAir)
            return;
        inAir = true;
        if(superPower == 0)
            airSpeed = jumpSpeed;
        else
            airSpeed = superjumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x+xSpeed,hitbox.y,hitbox.width,hitbox.height,lvlData))
            {
                hitbox.x += xSpeed;

                float xIndex = hitbox.x / Game.TILES_SIZE;
                float yIndex = hitbox.y / Game.TILES_SIZE;
                if(TileOfDeath((int) xIndex, (int) yIndex, lvlData))
                {
                    changeHealth(-200);
                }

                else if(TileOfSlow((int)xIndex,(int)yIndex,lvlData))
                {
                    if(playerAction == LEFT)
                    {
                        hitbox.x += xSpeed / 3;
                    }
                    else {
                        hitbox.x -= xSpeed / 3;
                    }
                }
                else if(HealTile((int)xIndex,(int)yIndex,lvlData))
                {
                    if(currentHealth != maxHealth) {
                        changeHealth(health_potion);
                        lvlData[(int) yIndex][(int) xIndex] = 0; //sa dispara dupa ce am luat inima
                        Player.updateScore(5);
                    }
                }

                else if(TileOfJump((int)xIndex,(int)yIndex,lvlData))
                {
                    jump(1);
                }

            }
        else
            {
            hitbox.x = GetEntityXPosNextToWall(hitbox,xSpeed);
            }

    }


    private void setAnimation() {
        int startAnimation = playerAction;
        if(moving)
        {
            playerAction = RUNNING;
        }
        else {
            playerAction = IDLE;
        }
        if(attacking) {
            playerAction = ATTACKING;

            if (startAnimation != ATTACKING) {
                animationIndex = 1;
                animationTick = 0;
                return;
            }
        }

        if(startAnimation != playerAction)
        {
            resetAnyTick();
        }
    }

    private void resetAnyTick() {
        animationIndex = 0;
        animationTick = 0;
    }

    private void loadAnimations() {
            BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

            Animations = new BufferedImage[17][12];

            for (int j = 0; j < Animations.length; ++j) {
                for (int i = 0; i < Animations[j].length; ++i) {
                    Animations[j][i] = img.getSubimage(i * 64, j * 64, 64, 64);
                }

            }
            statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.HEALTH_BAR);

    }

    public void loadLvlData(int [][] lvlData)
    {
        this.lvlData = lvlData;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void resetDirectionBooleans() {
        left=false;
        right=false;
        up=false;
        down=false;
    }

    public void setAttacking(boolean attacking)
    {
        this.attacking=attacking;
    }
    public void setJump(boolean jump)
    {
        this.jump = jump;
    }

    public void resetAll(){
        resetDirectionBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;
        hitbox.x = x;
        hitbox.y = y;
        Score = 0;

        if(!IsEntityOnFloor(hitbox,lvlData))
        {
            inAir = true;
        }
    }

    public void resetHealth()
    {
        currentHealth = maxHealth;
    }

    public void resetAllButSetPositionForPlayer(double player_X,double player_Y){
        resetDirectionBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;
        hitbox.x = (float)player_X;
        hitbox.y = (float)player_Y;
        Score = 0;

        if(!IsEntityOnFloor(hitbox,lvlData))
        {
            inAir = true;
        }
    }
    public static void updateScore(int value)
    {
        Score += value;
        //if(Game.getDataBase()!= null)
            //Game.getDataBase().update();

        //Score = Game.getDataBase().getScore(); // Score initial va fi variabila care va fi incarcata in DataBase,iar dupa pentru
                                                // a vedea clar ca baza de date merge vom incarca valoarea din baza de date
    }
    public static int getScore()
    {
        return Score;
    }
    public float getPlayerX()
    {
        return this.x;
    }
    public float getPlayerY()
    {
        return this.y;
    }

    public int getCurrentHealth()
    {
        return currentHealth;
    }
    public void setCurrentHealth(int health)
    {
        this.currentHealth = health;
    }
    public void printCurrentHealth()
    {
        System.out.println(currentHealth);
    }

    public void setInAir(boolean air)
    {
        this.inAir = air;
    }
}
