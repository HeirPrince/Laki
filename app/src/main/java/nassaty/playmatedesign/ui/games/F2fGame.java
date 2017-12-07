package nassaty.playmatedesign.ui.games;

/**
 * Created by Prince on 10/1/2017.
 */

public class F2fGame {

    String trigger;
    String opponent;
    int tpos;
    int opos;
    int end_position;
    int duration;
    int degress;


    public F2fGame() {
    }

    public int getDegress() {
        return degress;
    }

    public void setDegress(int degress) {
        this.degress = degress;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public int getTpos() {
        return tpos;
    }

    public void setTpos(int tpos) {
        this.tpos = tpos;
    }

    public int getOpos() {
        return opos;
    }

    public void setOpos(int opos) {
        this.opos = opos;
    }

    public int getEnd_position() {
        return end_position;
    }

    public void setEnd_position(int end_position) {
        this.end_position = end_position;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
