package nassaty.playmatedesign.ui.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import nassaty.playmatedesign.ui.utils.PlayGround;

public class chooseFriend extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.userList)
    RecyclerView userList;

    //firebase
    private DatabaseReference users;
    private DatabaseReference friendship;
    private PlayGround playGround;
    private FirebaseAuth auth;
    private FirebaseAgent agent;
    private FriendAdapter adapter;
    private List<User> user_list;
    private ChildEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Choose a friend");

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            playGround = new PlayGround(this);
            user_list = new ArrayList<>();

            //firebase
            users = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_USERS);
            friendship = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_FRIENDS);
            fetchData();
        }
    }

    public void fetchData() {
        LinearLayoutManager linear = new LinearLayoutManager(this);
        userList.setLayoutManager(linear);
        userList.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new ItemDecorator(this, ItemDecorator.VERTICAL_LIST, 2);
        userList.addItemDecoration(itemDecoration);
        agent = new FirebaseAgent(this);

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
                                    user_list.add(user);
                                    adapter.notifyDataSetChanged();
                                }catch (Exception e){
                                    dialog.dismiss();
                                    Toast.makeText(chooseFriend.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                dialog.dismiss();
                                Toast.makeText(chooseFriend.this, "empty dataset", Toast.LENGTH_SHORT).show();
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

            }
        };

        adapter = new FriendAdapter(user_list, this, 2);
        userList.setAdapter(adapter);
    }

    public void attachListeners() {
        friendship.addChildEventListener(listener);
    }

    public void detachListeners() {
        friendship.removeEventListener(listener);
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
}
