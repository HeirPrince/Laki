package nassaty.playmatedesign.ui.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

import nassaty.playmatedesign.ui.activities.Rival;
import nassaty.playmatedesign.ui.games.F2fGame;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.ActiveUser;
import nassaty.playmatedesign.ui.model.Game;
import nassaty.playmatedesign.ui.model.GroupNotif;
import nassaty.playmatedesign.ui.model.Notif;
import nassaty.playmatedesign.ui.model.Participant;
import nassaty.playmatedesign.ui.model.Wheel;

/**
 * Created by Giovanni Blood on 8/17/2017.
 */

public class PlayGround {

    private static Context ctx;

    private static FirebaseDatabase database;
    private static DatabaseReference game_sessions;
    private static DatabaseReference users;
    private static DatabaseReference playground;
    private static DatabaseReference active;
    private static DatabaseReference notifications;
    private static DatabaseReference game_participants;
    private static DatabaseReference room;
    public static Rival rival;
    private static FirebaseAuth auth;

    static Random r;
    static int degrees = 0;
    static int degrees_old = 0;
    private static final float FACTOR = 4.84f;

    public PlayGround(Context ctx) {
        this.ctx = ctx;
        database = FirebaseDatabase.getInstance();
        notifications = database.getReference().child(Constants.DATABASE_PATH_NOTIFICATIONS);
        game_sessions = database.getReference().child(Constants.DATABASE_PATH_GAME_SESSIONS);
        rival = new Rival();
        playground = database.getReference().child(Constants.DATABASE_PATH_PLAYGROUND);
        active = database.getReference().child(Constants.DATABASE_PATH_ONLINE_PLAYERS);
        room = database.getReference().child(Constants.DATABASE_PATH_ROOM);

        auth = FirebaseAuth.getInstance();
        r = new Random();

    }

    public static void sendGroupNotification(final Notif notif, String receiverPhone, @NonNull final NotificationListener<Boolean> listener){
        notifications.child(receiverPhone).push().setValue(notif).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    listener.isNotificationSent(true);
                }else {
                    listener.isNotificationSent(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void sendGameNotification(final Notif notif, String receiverPhone, @NonNull final NotificationListener<Boolean> n) {
        notifications.child(receiverPhone).push().setValue(notif).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    n.isNotificationSent(true);
                }else{
                    n.isNotificationSent(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx, "opponent notified successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface addGroupListener{
        void isAdded(Boolean status);
    }

    public void addGroup(List<Participant> participants, @NonNull final addGroupListener listener){
            playground.setValue(participants).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    listener.isAdded(task.isComplete());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.isAdded(false);
                }
            });

    }

    public void getGroup(){
        playground.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Participant participant = ds.getValue(Participant.class);
                    Toast.makeText(ctx, participant.getPhone(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void createGame(final Game game, final View view) {
        //friend to friend
        game_participants.child(game.getTriggger()).child(game.getOpponent()).setValue(game).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snackbar.make(view, "Game created successfully", Snackbar.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }


    public int overK() {
        int duration;
        int max = 10000;
        int min = 3000;

        duration = min + r.nextInt(max);

        return duration;
    }

    public interface NotificationListener<T>{
        void isNotificationSent(T status);
    }

    public static int getNextEndPosition(int deg) {
        return currentPosition(360 - (deg % 360));
    }

    public static void makeMeActive(String phone) {

        ActiveUser user = new ActiveUser();
        user.setUser_phone(phone);

        active.child(phone).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ctx, "now online :)", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx, "check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void checkMeOnline(final String phone) {
        active.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isOnline = Boolean.valueOf(dataSnapshot.getValue().toString());

                if (isOnline) {
                    Toast.makeText(ctx, "you are already online", Toast.LENGTH_SHORT).show();
                } else {
                    makeMeActive(phone);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //TODO END 1/10/2017
    public static void checkMeOut(final String phone) {
        active.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isOnline = dataSnapshot.hasChildren();
                ActiveUser u = dataSnapshot.getValue(ActiveUser.class);

                if (isOnline) {
                    active.child(u.getUser_phone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ctx, "user data changed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ctx, "not yet online", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(ctx, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void checkOnline(String phone) {
        active.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isOnlne = Boolean.valueOf(dataSnapshot.getValue().toString());

                if (isOnlne) {
                    Toast.makeText(ctx, "member online", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ctx, "member offline", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface playGroundComplete<T>{
        void playgroundDone(T isDone);
        void getKey(String key);
    }

    public void saveGameData(final String trigger, final String key, @NonNull final playGroundComplete<Boolean> playgroundL){
        Toast.makeText(ctx, key+trigger, Toast.LENGTH_SHORT).show();
        game_participants.child(trigger).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Game game = dataSnapshot.getValue(Game.class);

                if (game != null) {
                    final String key = playground.child(Constants.FRIEND_TYPE).child(trigger).push().getKey();

                    //variables
                    int duration = overK();
                    degrees = r.nextInt(3400) + 720;
                    int end_positon = getNextEndPosition(degrees);
                    int trigger_position = game.getTrigger_position();
                    int opponent_position = game.getOpponent_position();
                    String trigger = game.getTriggger();
                    String opponent = game.getOpponent();

                    //initialization
                    F2fGame f2f = new F2fGame();
                    f2f.setDuration(duration);
                    f2f.setOpos(opponent_position);
                    f2f.setTpos(trigger_position);
                    f2f.setOpponent(opponent);
                    f2f.setTrigger(trigger);
                    f2f.setEnd_position(end_positon);
                    f2f.setDegress(degrees);

                    playground.child(Constants.FRIEND_TYPE).child(trigger).child(key).setValue(f2f).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ctx, "game session created", Toast.LENGTH_SHORT).show();
                            playgroundL.playgroundDone(task.isSuccessful());
                            playgroundL.getKey(key);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            playgroundL.playgroundDone(false);
                            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    playgroundL.playgroundDone(false);
                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static int currentPosition(int degrees) {
        int pos = 0;

        if (degrees >= (FACTOR * 1) && degrees < (FACTOR * 3)) {
            pos = 32;
        }
        if (degrees >= (FACTOR * 3) && degrees < (FACTOR * 5)) {
            pos = 15;
        }
        if (degrees >= (FACTOR * 5) && degrees < (FACTOR * 7)) {
            pos = 19;
        }
        if (degrees >= (FACTOR * 7) && degrees < (FACTOR * 9)) {
            pos = 4;
        }
        if (degrees >= (FACTOR * 9) && degrees < (FACTOR * 11)) {
            pos = 21;
        }
        if (degrees >= (FACTOR * 11) && degrees < (FACTOR * 13)) {
            pos = 2;
        }
        if (degrees >= (FACTOR * 13) && degrees < (FACTOR * 15)) {
            pos = 25;
        }
        if (degrees >= (FACTOR * 15) && degrees < (FACTOR * 17)) {
            pos = 17;
        }
        if (degrees >= (FACTOR * 17) && degrees < (FACTOR * 19)) {
            pos = 34;
        }
        if (degrees >= (FACTOR * 19) && degrees < (FACTOR * 21)) {
            pos = 6;
        }
        if (degrees >= (FACTOR * 21) && degrees < (FACTOR * 23)) {
            pos = 27;
        }
        if (degrees >= (FACTOR * 23) && degrees < (FACTOR * 25)) {
            pos = 13;
        }
        if (degrees >= (FACTOR * 25) && degrees < (FACTOR * 27)) {
            pos = 36;
        }
        if (degrees >= (FACTOR * 27) && degrees < (FACTOR * 29)) {
            pos = 11;
        }
        if (degrees >= (FACTOR * 29) && degrees < (FACTOR * 31)) {
            pos = 30;
        }
        if (degrees >= (FACTOR * 31) && degrees < (FACTOR * 33)) {
            pos = 8;
        }
        if (degrees >= (FACTOR * 33) && degrees < (FACTOR * 35)) {
            pos = 23;
        }
        if (degrees >= (FACTOR * 35) && degrees < (FACTOR * 37)) {
            pos = 35;
        }
        if (degrees >= (FACTOR * 37) && degrees < (FACTOR * 39)) {
            pos = 5;
        }
        if (degrees >= (FACTOR * 39) && degrees < (FACTOR * 41)) {
            pos = 24;
        }
        if (degrees >= (FACTOR * 41) && degrees < (FACTOR * 43)) {
            pos = 16;
        }
        if (degrees >= (FACTOR * 43) && degrees < (FACTOR * 45)) {
            pos = 33;
        }
        if (degrees >= (FACTOR * 45) && degrees < (FACTOR * 47)) {
            pos = 1;
        }
        if (degrees >= (FACTOR * 47) && degrees < (FACTOR * 49)) {
            pos = 20;
        }
        if (degrees >= (FACTOR * 49) && degrees < (FACTOR * 51)) {
            pos = 14;
        }
        if (degrees >= (FACTOR * 51) && degrees < (FACTOR * 53)) {
            pos = 31;
        }
        if (degrees >= (FACTOR * 53) && degrees < (FACTOR * 55)) {
            pos = 9;
        }
        if (degrees >= (FACTOR * 55) && degrees < (FACTOR * 57)) {
            pos = 22;
        }
        if (degrees >= (FACTOR * 57) && degrees < (FACTOR * 59)) {
            pos = 16;
        }
        if (degrees >= (FACTOR * 59) && degrees < (FACTOR * 61)) {
            pos = 29;
        }
        if (degrees >= (FACTOR * 61) && degrees < (FACTOR * 63)) {
            pos = 7;
        }
        if (degrees >= (FACTOR * 63) && degrees < (FACTOR * 65)) {
            pos = 28;
        }
        if (degrees >= (FACTOR * 65) && degrees < (FACTOR * 67)) {
            pos = 12;
        }
        if (degrees >= (FACTOR * 67) && degrees < (FACTOR * 69)) {
            pos = 35;
        }
        if (degrees >= (FACTOR * 69) && degrees < (FACTOR * 71)) {
            pos = 3;
        }
        if (degrees >= (FACTOR * 71) && degrees < (FACTOR * 73)) {
            pos = 26;
        }
        if (degrees >= (FACTOR * 73) && degrees < 360 || degrees >= 0 && degrees < (FACTOR * 1)) {
            pos = 0;
        }

        return pos;
    }

    public interface dotheCountDown<T> {
        void isCountdown(long tick);

        void isCountdownDone();
    }

    public void countDown(int time, @NonNull final dotheCountDown<Boolean> countdown) {
        CountDownTimer countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {
                countdown.isCountdown(l);
            }

            @Override
            public void onFinish() {
                countdown.isCountdownDone();
            }
        };
        countDownTimer.start();
    }

    public interface isGameAprooved<T> {
        void isAprooved(T status);
    }

    public void approoveGame(final String key, final String trigger, int trigger_position, final int opponent_position, final isGameAprooved<Boolean> isGameAprooved) {
        final Wheel wheel = new Wheel();
        int d = r.nextInt(3400) + 720;
        int degrees_old = d % 360;
        int degrees = d;
        int duration = overK();

        wheel.setDegrees(degrees);
        wheel.setDegrees_old(degrees_old);
        wheel.setDuration(duration);
        wheel.setEndPosition(currentPosition(360 - (degrees % 360)));
        wheel.setTriggerPosition(trigger_position);
        wheel.setOpponentPosition(opponent_position);
    }

    public void fetchGameData(String trigger, String key, final View wheel, final getResults<Boolean> results){
        playground.child(Constants.FRIEND_TYPE).child(trigger).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                F2fGame f2f = dataSnapshot.getValue(F2fGame.class);
                final int end_position = f2f.getEnd_position();
                int duration = f2f.getDuration();
                String trigger = f2f.getTrigger();
                String opponent = f2f.getOpponent();
                final int tpos = f2f.getTpos();
                final int opos = f2f.getOpos();
                final int degrees = f2f.getDegress();
                int degrees_old = degrees % 360;

                RotateAnimation rotate = new RotateAnimation(degrees_old, degrees, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(duration);
                rotate.setFillAfter(true);
                rotate.setInterpolator(new DecelerateInterpolator());
                rotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        results.final_position(getNextEndPosition(degrees), tpos, opos);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                wheel.startAnimation(rotate);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface getGameResults{
        void results(int finalPos);
    }

    public void startGame(final View wheel, final getGameResults callback){
        int duration = overK();
        int d = r.nextInt(3400) + 720;
        int degrees_old = d % 360;
        final int degrees = d;

        RotateAnimation rotate = new RotateAnimation(degrees_old, degrees, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(duration);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new DecelerateInterpolator());
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callback.results(getNextEndPosition(degrees));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        wheel.startAnimation(rotate);
    }


    public interface getResults<T>{
        void final_position(int pos, int trigger, int opponent);
    }

    public interface groupNotifications{
        void groups(GroupNotif notif);
    }

    public void getGNotifs(String phone, final groupNotifications callback){
        notifications.child(phone).child(Constants.GROUP_NOTIFICATIONS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        GroupNotif notif = ds.getValue(GroupNotif.class);
                        if (notif == null) {
                            callback.groups(null);
                        }else {
                            callback.groups(notif);
                        }
                    }
                }else {
                    callback.groups(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}