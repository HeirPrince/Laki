package nassaty.playmatedesign.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.singh.daman.proprogressviews.CircleArcProgress;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.helpers.SessionManager;
import nassaty.playmatedesign.ui.model.Game;
import nassaty.playmatedesign.ui.model.Notif;
import nassaty.playmatedesign.ui.model.User;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;
import nassaty.playmatedesign.ui.utils.PlayGround;

public class Prepare extends AppCompatActivity {


    @BindView(R.id.trigger_profile)
    CircleImageView trigger_profile;
    @BindView(R.id.opponent_profile)
    CircleImageView opponent_profile;
    @BindView(R.id.trigger_phone)
    TextView trigger_phone;
    @BindView(R.id.opponent_phone)
    TextView opponent_phone;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.status)
    TextView stat;
    @BindView(R.id.progress)
    CircleArcProgress progress;
    @BindView(R.id.accept)
    FloatingActionButton accept;
    @BindView(R.id.status_trigger)
    TextView status_trigger;
    @BindView(R.id.status_opponent)
    TextView status_opponent;
    private SessionManager sessions;

    private FirebaseAuth auth;
    private FirebaseAgent agent;
    private PlayGround playGround;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference users;
    private DatabaseReference online;
    private DatabaseReference notifications;
    private DatabaseReference games;
    private StorageReference profiles;
    public int tposition;
    public int oposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Preparation");

        database = FirebaseDatabase.getInstance();
        agent = new FirebaseAgent(this);
        playGround = new PlayGround(this);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        users = database.getReference().child(Constants.DATABASE_PATH_USERS);
        online = database.getReference().child(Constants.DATABASE_PATH_ONLINE_PLAYERS);
        notifications = database.getReference(Constants.DATABASE_PATH_NOTIFICATIONS);
        games = database.getReference().child(Constants.DATABASE_PATH_GAME_SESSIONS);
        profiles = storage.getReferenceFromUrl(Constants.STORAGE_PATH_URL).child(Constants.STORAGE_PATH_USERS);

        if (auth.getCurrentUser() != null) {
            handleIntent();
            turn_ON_views();
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Prepare.this, "i am alive", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "invalid user", Toast.LENGTH_SHORT).show();
        }

    }

    public void handleIntent(){
        String opponent = getIntent().getStringExtra("ophone");
        String image = getIntent().getStringExtra("image");
        tposition = getIntent().getIntExtra("tposition", 0);
        oposition = getIntent().getIntExtra("oposition", 0);

        Toast.makeText(this, String.valueOf(tposition), Toast.LENGTH_SHORT).show();

        Boolean isOpponent = getIntent().getBooleanExtra("isOpponent", false);
        String notif_key = getIntent().getStringExtra("notif_key");


        if (isOpponent){
            setupHeader(notif_key, isOpponent, auth.getCurrentUser().getPhoneNumber(), image, oposition, opponent);
            Toast.makeText(this, "opponent side", Toast.LENGTH_SHORT).show();
        }else{
            setupHeader(notif_key, isOpponent, auth.getCurrentUser().getPhoneNumber(), image, tposition, opponent);
            Toast.makeText(this, "trigger side", Toast.LENGTH_SHORT).show();
        }

    }

    private void setupHeader(final String key, Boolean isOpponent, final String trigger, final String image, final int pos, final String opponent) {
        if (auth.getCurrentUser().getPhoneNumber() != null) {
            if (isOpponent){
                if (key != null) {
                    notifications.child(Constants.FRIEND_TYPE).child(auth.getCurrentUser().getPhoneNumber()).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Notif notification = dataSnapshot.getValue(Notif.class);
                            //trigger
                            users.child(notification.getSender()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    trigger_phone.setText(user.getPhone_number());
                                    StorageReference rs = profiles.child(user.getImage());
                                    Glide.with(getApplicationContext())
                                            .load(rs)
                                            .crossFade()
                                            .centerCrop()
                                            .into(trigger_profile);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(Prepare.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            //recipient
                            users.child(notification.getReceiver()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    opponent_phone.setText(user.getPhone_number());
                                    StorageReference rs = profiles.child(user.getImage());
                                    Glide.with(getApplicationContext())
                                            .load(rs)
                                            .crossFade()
                                            .centerCrop()
                                            .into(opponent_profile);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(Prepare.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            agent.isF2FReady(new FirebaseAgent.F2FListener<Boolean>() {
                                @Override
                                public void isOpponentOnline(Boolean online) {
                                    if (online) {
                                        status_opponent.setText("ONLINE");
                                    } else {
                                        status_opponent.setText("OFFLINE");
                                    }
                                }

                                @Override
                                public void isTriggerOnline(Boolean online) {
                                    if (online) {
                                        status_trigger.setText("ONLINE");
                                    } else {
                                        status_trigger.setText("OFFLINE");
                                    }
                                }

                                @Override
                                public void isAllOnline(Boolean t) {
                                    if (t) {
                                        accept.setEnabled(true);

                                        addGameToDB(notification, pos, new isSavingDone<Boolean>() {
                                            @Override
                                            public void isDone(Boolean status) {
                                                if (status){
                                                    Toast.makeText(Prepare.this, "game saved successfully", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(Prepare.this, "game not created", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void getKey(final String key) {
                                                if (key != null){
                                                    playGround.saveGameData(notification.getReceiver(), key, new PlayGround.playGroundComplete<Boolean>() {
                                                        @Override
                                                        public void playgroundDone(Boolean isDone) {
                                                            if (isDone){
                                                                Toast.makeText(Prepare.this, "cool", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void getKey(String key) {
                                                            Intent i = new Intent(Prepare.this, Rival.class);
                                                            i.putExtra("trigger", notification.getSender());
                                                            i.putExtra("key", key);
                                                            startActivity(i);
                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText(Prepare.this, "empty_key", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {
                                        accept.setEnabled(false);
                                        Toast.makeText(Prepare.this, "not ready", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, notification.getSender(), notification.getReceiver());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(this, "empty notification", Toast.LENGTH_SHORT).show();
                }
            }else{
                users.child(opponent).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        opponent_phone.setText(user.getPhone_number());
                        StorageReference rs = profiles.child(user.getImage());
                        Glide.with(Prepare.this).load(rs).into(opponent_profile);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Prepare.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                users.child(trigger).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        trigger_phone.setText(user.getPhone_number());
                        StorageReference rs = profiles.child(user.getImage());
                        Glide.with(Prepare.this).load(rs).into(trigger_profile);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Prepare.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                agent.isF2FReady(new FirebaseAgent.F2FListener<Boolean>() {
                    @Override
                    public void isOpponentOnline(Boolean online) {
                        if (online) {
                            status_opponent.setText("ONLINE");
                        } else {
                            status_opponent.setText("OFFLINE");
                        }
                    }

                    @Override
                    public void isTriggerOnline(Boolean online) {
                        if (online) {
                            status_trigger.setText("ONLINE");
                        } else {
                            status_trigger.setText("OFFLINE");
                        }
                    }

                    @Override
                    public void isAllOnline(Boolean t) {
                        if (t) {
                            accept.setEnabled(true);
                            Notif notification = new Notif();
                            notification.setRead(false);
                            notification.setType(Constants.FRIEND_TYPE);
                            notification.setSender(auth.getCurrentUser().getPhoneNumber());
                            notification.setReceiver(opponent);
                            notification.setSender_position(pos);

                            playGround.sendGameNotification(notification, opponent, new PlayGround.NotificationListener<Boolean>() {
                                @Override
                                public void isNotificationSent(Boolean status) {
                                    if (status){
                                        Toast.makeText(Prepare.this, "notification sent successfully", Toast.LENGTH_SHORT).show();
                                        //waiting for approval
                                    }else{
                                        Toast.makeText(Prepare.this, "notification not sent", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            accept.setEnabled(false);
                            Toast.makeText(Prepare.this, "not ready", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, trigger, opponent);
            }

        }
    }

    public void countdown(int seconds, final String message, final isSavingDone<Boolean> state) {
        stat.setText("start");
        CountDownTimer countDown = new CountDownTimer(seconds, 1000) {

            @Override
            public void onTick(long l) {
                stat.setText(message+" "+String.valueOf(l / 1000));
            }

            @Override
            public void onFinish() {
                state.isDone(true);
                turn_OFF_views();
            }
        };
        countDown.start();
    }

    public void addGameToDB(Notif notification, int position, @NonNull final isSavingDone<Boolean> save){
        Game game = new Game();
        game.setOpponent(notification.getReceiver());
        game.setTriggger(notification.getSender());
        game.setTrigger_position(notification.getSender_position());
        game.setOpponent_position(position);

        final String key = games.child(Constants.FRIEND_TYPE).child(notification.getSender()).push().getKey();
        games.child(Constants.FRIEND_TYPE).child(notification.getSender()).child(key).setValue(game).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    save.isDone(task.isSuccessful());
                    save.getKey(key);
                    Toast.makeText(Prepare.this, "game created successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface isSavingDone<T>{
        void isDone(T status);
        void getKey(String key);
    }

    public void turn_ON_views() {
        stat.setVisibility(View.VISIBLE);
        accept.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
    }

    public void turn_OFF_views() {
        accept.setEnabled(true);
        stat.setVisibility(View.GONE);
        accept.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }
}
