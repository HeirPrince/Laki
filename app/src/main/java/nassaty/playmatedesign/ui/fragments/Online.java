package nassaty.playmatedesign.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.adapters.FriendAdapter;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.User;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;

/**
 * A simple {@link Fragment} subclass.
 */
public class Online extends Fragment {

    private RecyclerView online_list;
    private DatabaseReference online;
    private DatabaseReference users;
    private FriendAdapter adapter;
    private FirebaseAgent agent;
    private List<User> friends;
    private FirebaseUser a;
    private FirebaseAuth auth;
    private LinearLayoutManager layoutManager;

    public Online() {
        // Required empty public constructor
        users = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_USERS);
        friends = new ArrayList<>();
        setRetainInstance(true);
        users.keepSynced(true);


        auth = FirebaseAuth.getInstance();
        a = auth.getCurrentUser();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_online, container, false);
        online_list = v.findViewById(R.id.online_list);


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        showList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    public void showList(){
        layoutManager = new LinearLayoutManager(getContext());
        online_list.setLayoutManager(layoutManager);
        online = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_ONLINE_PLAYERS);

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                friends.add(user);
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
                adapter.notifyDataSetChanged();
            }
        };

        users.addChildEventListener(listener);


        adapter = new FriendAdapter(friends, getContext(), 2);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendCount = adapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 || positionStart >= (friendCount - 1) && lastVisiblePosition == (positionStart - 1)){
                    Online.this.online_list.scrollToPosition(positionStart);
                }
            }
        });


        this.online_list.setAdapter(adapter);

    }

}
