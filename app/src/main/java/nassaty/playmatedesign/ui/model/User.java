package nassaty.playmatedesign.ui.model;

/**
 * Created by Prince on 7/25/2017.
 */

public class User {

    private String username;
    private String phone_number;
    private String uniq_id;
    private String image;
    private String image_url;

    public User() {
    }

    public User(String username, String phone_number, String uniq_id, String image, String image_url) {
        this.username = username;
        this.phone_number = phone_number;
        this.uniq_id = uniq_id;
        this.image = image;
        this.image_url = image_url;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUniq_id() {
        return uniq_id;
    }

    public void setUniq_id(String uniq_id) {
        this.uniq_id = uniq_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
