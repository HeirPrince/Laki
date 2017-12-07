package nassaty.playmatedesign.ui.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Prince on 5/28/2017.
 */

public class SessionManager {

    private static SessionManager instance = null;
    public static final String PREF_NAME = "login";
    public static final String PHONE = "phone";
    private static String TAG = SessionManager.class.getSimpleName();
    static SharedPreferences pref;
    static SharedPreferences.Editor mEditor;
    Context ctx;
    static int PRIVATE_MODE = 0;

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager();
            pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            mEditor = pref.edit();
        }
        return instance;
    }

    public String getPhone() {
        return pref.getString(PHONE, "");
    }
}
