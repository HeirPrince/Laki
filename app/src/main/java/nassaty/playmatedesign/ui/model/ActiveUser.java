package nassaty.playmatedesign.ui.model;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Prince on 10/21/2017.
 */

public class ActiveUser {

    private String user_phone;
    private String live_time;

    public ActiveUser() {
    }

    public ActiveUser(String user_phone, String live_time) {
        this.user_phone = user_phone;
        this.live_time = live_time;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getLive_time() {
        return live_time;
    }

    public void setLive_time(String live_time) {
        this.live_time = live_time;
    }
}
