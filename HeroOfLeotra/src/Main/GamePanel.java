package Main;

import Inputs.KeyBoardInputs;
import Inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static Main.Game.GAME_HEIGHT;
import static Main.Game.GAME_WIDTH;
import static utilities.Constants.PlayerConstants.*;
import static utilities.Constants.Directions.*;
import Main.Game;

public class GamePanel extends JPanel {
    private MouseInputs mouseInputs;
    private Game game;
    public GamePanel(Game game)
    {

        mouseInputs = new MouseInputs(this);
        this.game = game;
        addKeyListener(new KeyBoardInputs(this));
        setPanelSize();

        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void updateGame()
    {

    }


    private void setPanelSize()
    {
        Dimension size = new Dimension(GAME_WIDTH,GAME_HEIGHT);
        //setMinimumSize(size);
        setPreferredSize(size);
        //setMaximumSize(size);
        System.out.println("size: " + GAME_WIDTH +"x" + GAME_HEIGHT);
    }



    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        /*subImg = img.getSubimage(0 * 64,3 * 64,64,64);
        if(GetSpriteAmount(playerAction) < animationIndex) {
            animationIndex = 0;
        }*///cumva ajunge animationIndexu mai mare decat GetSpriteAmount
        // [11][6] -> [3][6] in loc sa puna animationIndexu pe 0 mai intai si sa faca [3][0]

        //System.out.println("[" + playerAction + "]" + "[" + animationIndex + "]");

        //g.dispose();

        game.render(g);
    }

    public Game getGame() {
        return game;
    }


}
