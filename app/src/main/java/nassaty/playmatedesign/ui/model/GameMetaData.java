package nassaty.playmatedesign.ui.model;

/**
 * Created by Prince on 11/30/2017.
 */

public class GameMetaData {

    private int pos;
    private int duration;
    private int degrees;
    private int degrees_old;

    public GameMetaData() {
    }

    public GameMetaData(int pos, int duration, int degrees, int degrees_old) {
        this.pos = pos;
        this.duration = duration;
        this.degrees = degrees;
        this.degrees_old = degrees_old;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public int getDegrees_old() {
        return degrees_old;
    }

    public void setDegrees_old(int degrees_old) {
        this.degrees_old = degrees_old;
    }
}
