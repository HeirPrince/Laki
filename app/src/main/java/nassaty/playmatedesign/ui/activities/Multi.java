package nassaty.playmatedesign.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.adapters.FriendAdapter;
import nassaty.playmatedesign.ui.adapters.HeadAdapter;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.Notif;
import nassaty.playmatedesign.ui.model.Participant;
import nassaty.playmatedesign.ui.model.User;
import nassaty.playmatedesign.ui.utils.DialogHelper;
import nassaty.playmatedesign.ui.utils.ItemDecorator;
import nassaty.playmatedesign.ui.utils.PlayGround;

public class Multi extends AppCompatActivity {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.participant_list)RecyclerView participant_list;
    @BindView(R.id.friend_list)RecyclerView friend_list;
    @BindView(R.id.participants)LinearLayout participants;

    private List<User> friendList;
    public List<Participant> participantList;
    private FriendAdapter friendAdapter;
    private HeadAdapter headAdapter;
    private PlayGround playGround;
    private DialogHelper dialogHelper;

    private FirebaseDatabase database;
    private DatabaseReference users;
    private DatabaseReference friends;
    private FirebaseAuth auth;
    private FirebaseUser active;
    private ChildEventListener listener;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();;
        auth = FirebaseAuth.getInstance();
        active = auth.getCurrentUser();
        playGround = new PlayGround(this);
        dialogHelper = new DialogHelper(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        participant_list.setLayoutManager(linearLayoutManager);
        participant_list.setHasFixedSize(true);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(participant_list.getContext(), linearLayoutManager.getOrientation());
//        participant_list.addItemDecoration(itemDecoration);
        playGround.getGroup();

        if (active != null){
            users = database.getReference().child(Constants.DATABASE_PATH_USERS);
            friends = database.getReference().child(Constants.DATABASE_PATH_FRIENDS);
            friendList = new ArrayList<>();
            participantList = new ArrayList<>();
            participants.setVisibility(View.GONE);
            fetchData();
        }
    }

    public void fetchData() {
        linearLayoutManager = new LinearLayoutManager(this);
        friend_list.setLayoutManager(linearLayoutManager);
        friend_list.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new ItemDecorator(this, ItemDecorator.VERTICAL_LIST, 2);
        friend_list.addItemDecoration(itemDecoration);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    users.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null && dataSnapshot.getValue() != null){
                                try{
                                    dialog.dismiss();
                                    User user = dataSnapshot.getValue(User.class);
                                    friendList.add(user);
                                    friendAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    dialog.dismiss();
                                    Toast.makeText(Multi.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                dialog.dismiss();
                                Toast.makeText(Multi.this, "empty dataset", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                friendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                friendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                friendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        friendAdapter = new FriendAdapter(friendList, this, 3);
        friend_list.setAdapter(friendAdapter);
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void test(Participant participant){
        participantList.add(participant);
        if (participantList != null){
            headAdapter = new HeadAdapter(this, participantList);
            participant_list.setAdapter(headAdapter);
            participants.setVisibility(View.VISIBLE);
        }else {
            headAdapter.notifyDataSetChanged();
            participants.setVisibility(View.GONE);
        }
    }

    public void attachListeners() {
        friends.addChildEventListener(listener);
        EventBus.getDefault().register(this);
    }

    public void detachListeners() {
        friends.removeEventListener(listener);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        attachListeners();
    }

    @Override
    protected void onStop() {
        super.onStop();
        detachListeners();
    }

    public void addParti(View view) {
        dialogHelper.choosePos(new DialogHelper.onPositionSelected() {
            @Override
            public void selectedPosition(final int pos) {
                playGround.addGroup(participantList, new PlayGround.addGroupListener() {
                    @Override
                    public void isAdded(Boolean status) {
                        if (status){
                            Toast.makeText(Multi.this, "added", Toast.LENGTH_SHORT).show();
                            sendRequestToAll(pos);
                            Intent intent = new Intent(Multi.this, Rival.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(Multi.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void sendRequestToAll(int pos){
        for (Participant participant : participantList){
            Notif notif = new Notif();
            notif.setType(Constants.GROUP_TYPE);
            notif.setReceiver(participant.getPhone());
            notif.setSender(active.getPhoneNumber());
            notif.setSender_position(pos);
            notif.setRead(false);

            playGround.sendGroupNotification(notif, participant.getPhone(), new PlayGround.NotificationListener<Boolean>() {
                @Override
                public void isNotificationSent(Boolean status) {
                    if (status){
                        Toast.makeText(Multi.this, "delivered", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Multi.this, "no request", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


}
