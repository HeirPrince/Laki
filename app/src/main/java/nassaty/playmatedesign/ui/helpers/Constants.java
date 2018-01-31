package nassaty.playmatedesign.ui.helpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Prince on 7/26/2017.
 */

public class Constants {

    //firebase auth
    private FirebaseAuth auth;
    private FirebaseUser user;

    public String getActiveUser(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null){

            return user.getPhoneNumber();

        }else {
            return null;
        }

    }

    //mysql
    public static final String MYSQL_BASE_URL = "https://localhost/vipibet/";
    //firebase

    public static long TIME_TO_REFRESH = 10 * 1000;
    public static final long TIME_TO_OFFLINE = 2 * 60 * 1000;
    public static final String EMAIL_PATTERN = "/^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$/";

    public static final String USERS = "users";
    public static final String GROUPS = "groups";
    public static final String GAMES = "game";
    public static final String ACTIONS = "actions";
    public static final String GROUP_NOTIFICATIONS = "group_notifications";
    public static final String FRIEND_NOTIFICATIONS = "friend_notifications";
    public static final String APP_NOTIFICATIONS = "app_notifications";
    public static final int GALLERY_CODE = 1;
    public static final int PICK_IMAGE = 2;

    //firebase storage
    public static final String STORAGE_PATH_URL = "gs://roulette-1077e.appspot.com";
    public static final String STORAGE_PATH_USERS = "users/";
    public static final String STORAGE_PATH_POSTS = "posts/";
    //firebase database
    public static final String DATABASE_PATH_USERS = "users";
    public static final String DATABASE_PATH_GROUPS = "groups";
    public static final String DATABASE_PATH_NEARBY = "nearby";
    public static final String DATABASE_PATH_GAME_SESSIONS = "game_sessions";
    public static final String DATABASE_PATH_PARTICIPANTS = "participants";
    public static final String DATABASE_PATH_PLAYGROUND = "playground";
    public static final String DATABASE_PATH_ONLINE_PLAYERS = "online";
    public static final String DATABASE_PATH_FRIENDS = "friendship";
    public static final String DATABASE_PATH_NOTIFICATIONS = "notifications";
    public static final String DATABASE_PATH_CREDIT_CARDS = "credit_cards";
    public static final String DATABASE_PATH_COINS = "tokens";
    public static final String DATABASE_PATH_BETS = "bet";
    public static final String DATABASE_PATH_ACCOUNT = "account";
    public static final String DATABASE_PATH_RESERVATIONS = "reservations";
    public static final String DATABASE_PATH_ROOM = "rooms";
    public static final String DATABASE_PATH_REPLAY_REQUEST = "replay";
    public static final String PLAYING = "playing";
    public static final String PARTICIPANTS = "participants";
    public static final String FRIEND_TYPE = "f2f";
    public static final String GROUP_TYPE = "group";
    public static final String NEARBY_TYPE = "nearby";


    public static final String F2F_MESSAGE = "Would You like to play with me";
    public static final String GROUP_MESSAGE = "Would like You to join them";
    public static final String NEARBY = "A nearby friend would like to play with You";


    public static final int COUNTDOWN_TIMES = 5;



}
