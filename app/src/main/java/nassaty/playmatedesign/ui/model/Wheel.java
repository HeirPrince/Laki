package nassaty.playmatedesign.ui.model;

/**
 * Created by Prince on 10/15/2017.
 */

public class Wheel {

    public int endPosition;
    public int triggerPosition;
    public int opponentPosition;
    public int degrees;
    public int degrees_old;
    public int duration;

    public Wheel() {
    }

    public Wheel(int endPosition, int triggerPosition, int opponentPosition, int degrees, int degrees_old, int duration) {
        this.endPosition = endPosition;
        this.triggerPosition = triggerPosition;
        this.opponentPosition = opponentPosition;
        this.degrees = degrees;
        this.degrees_old = degrees_old;
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

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public int getTriggerPosition() {
        return triggerPosition;
    }

    public void setTriggerPosition(int triggerPosition) {
        this.triggerPosition = triggerPosition;
    }

    public int getOpponentPosition() {
        return opponentPosition;
    }

    public void setOpponentPosition(int opponentPosition) {
        this.opponentPosition = opponentPosition;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
