package nassaty.playmatedesign.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.adapters.FriendAdapter;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.User;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;
import nassaty.playmatedesign.ui.utils.ItemDecorator;

public class Friends extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.friend_list)
    RecyclerView friend_list;
    @BindView(R.id.progress)
    ProgressBar bar;
    private DatabaseReference users;
    private DatabaseReference online;
    private DatabaseReference friendship;
    private FirebaseStorage storage;
    private FirebaseAgent agent;
    private StorageReference profiles;
    FirebaseAuth auth;
    private List<User> friends;
    private FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add a new Friend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        agent = new FirebaseAgent(this);
        friends = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        online = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_ONLINE_PLAYERS);
        friendship = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_FRIENDS);

        checkDbConnection();
        initList();

    }

    public void checkDbConnection() {
        if (agent.isConnected()) {
            if (auth.getCurrentUser() != null) {
                FirebaseUser user = auth.getCurrentUser();
                online.child(auth.getCurrentUser().getPhoneNumber()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //true
                        Toast.makeText(Friends.this, "connection established", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Friends.this, "connection to db failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void initList() {

        bar.setVisibility(View.VISIBLE);
        LinearLayoutManager linear = new LinearLayoutManager(this);
        friend_list.setLayoutManager(linear);
        friend_list.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new ItemDecorator(this, ItemDecorator.VERTICAL_LIST, 2);
        friend_list.addItemDecoration(itemDecoration);

        //firebase
        users = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_USERS);
        storage = FirebaseStorage.getInstance();
        profiles = storage.getReferenceFromUrl(Constants.STORAGE_PATH_URL);

        users.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                bar.setVisibility(View.GONE);
                User user = dataSnapshot.getValue(User.class);
                friends.add(user);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                bar.setVisibility(View.GONE);
            }
        });
        adapter = new FriendAdapter(friends, this, 1);
        friend_list.setAdapter(adapter);
    }

    public void addnewFriend(View view) {
        startActivity(new Intent(Friends.this, AddFriend.class));
    }
}
