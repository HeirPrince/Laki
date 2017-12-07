package nassaty.playmatedesign.ui.model;

/**
 * Created by Prince on 11/27/2017.
 */

public class Participant {

    private String phone;
    private String image;
    private int position;
    private int level;

    public Participant() {
    }

    public Participant(String phone, int position, int level, String image) {
        this.phone = phone;
        this.position = position;
        this.level = level;
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
