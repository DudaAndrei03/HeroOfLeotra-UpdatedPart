package userInterface;

import utilities.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static utilities.Constants.UI.URM_Buttons.*;

public class UrmButton extends PauseButton{
    private int Unpause = 0,Replay = 1,Menu = 4,Next = 3;

    private boolean mousePressed,mouseOver,mouseReleased;


    private BufferedImage[] imgs;
    private int index = 0;

    public UrmButton(int x, int y, int width, int height,String typeofButton) {
        super(x, y, width, height);
        loadImgs();
        switch(typeofButton)
        {
            case "UNPAUSE": index = Unpause;
            break;

            case "REPLAY": index = Replay;
            break;

            case "MENU": index = Menu;
            break;

            case "NEXT":index = Next;
            break;
        }
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        imgs = new BufferedImage[7];

        for(int i = 0 ; i < imgs.length;i++)
        {
            imgs[i] = temp.getSubimage(64 + i * URM_DEFAULT_SIZE,25,URM_DEFAULT_SIZE,URM_DEFAULT_SIZE);
        }

    }

    public void update()
    {

    }

    public void draw(Graphics g)
    {
        g.drawImage(imgs[index],x,y,URM_SIZE,URM_SIZE,null);
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMouseReleased() {
        return mouseReleased;
    }

    public void setMouseReleased(boolean mouseReleased) {
        this.mouseReleased = mouseReleased;
    }
    public void resetBools()
    {
        mouseOver = false;
        mousePressed = false;
        mouseReleased = false;
    }
}
