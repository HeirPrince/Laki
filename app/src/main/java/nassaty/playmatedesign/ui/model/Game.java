package nassaty.playmatedesign.ui.model;

import java.util.HashMap;

/**
 * Created by Prince on 7/28/2017.
 */

public class Game {

    private Boolean playing;
    private int trigger_position;
    private int opponent_position;
    private String triggger;
    private String opponent;

    public Game() {
    }

    public Game(Boolean playing, int trigger_position, int opponent_position, String opponent, String trigger) {
        this.playing = playing;
        this.trigger_position = trigger_position;
        this.opponent_position = opponent_position;
        this.triggger = trigger;
        this.opponent = opponent;
    }

    public Boolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
    }

    public int getTrigger_position() {
        return trigger_position;
    }

    public void setTrigger_position(int trigger_position) {
        this.trigger_position = trigger_position;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public int getOpponent_position() {
        return opponent_position;
    }

    public void setOpponent_position(int opponent_position) {
        this.opponent_position = opponent_position;
    }

    public String getTriggger() {
        return triggger;
    }

    public void setTriggger(String triggger) {
        this.triggger = triggger;
    }
}
