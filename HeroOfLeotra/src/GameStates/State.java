package GameStates;

import Audio.AudioPlayer;
import Main.Game;
import userInterface.MenuButton;

import java.awt.event.MouseEvent;

import static java.lang.System.exit;

public class State {
    protected Game game;
    public State(Game game)
    {
        this.game = game;
    }
    public Game getGame()
    {
        return this.game;
    }
    public boolean isIn(MouseEvent e, MenuButton mb)
    {
        return mb.getBounds().contains(e.getX(),e.getY());
    }

    public void setGameState(GameState state)
    {
        switch(state)
        {
            case MENU:
                game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING:
                game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
            case QUIT:
                exit(1);
        }
        GameState.state = state;
    }
}
//State Design Pattern