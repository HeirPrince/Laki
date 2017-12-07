package nassaty.playmatedesign.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.adapters.NotificationAdapter;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.Notif;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;
import nassaty.playmatedesign.ui.utils.GameServiceHelper;
import nassaty.playmatedesign.ui.utils.ItemDecorator;
import nassaty.playmatedesign.ui.utils.PlayGround;

/**
 * A simple {@link Fragment} subclass.
 */
public class Active extends Fragment {

    private RecyclerView notif_list;

    //firebase
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference profiles;
    private DatabaseReference online;
    private DatabaseReference users;
    private DatabaseReference notifications;
    private DatabaseReference pushKeys;
    private FirebaseAuth auth;
    private FirebaseUser active_user;
    private PlayGround playGround;
    private FirebaseAgent agent;
    private GameServiceHelper helper;

    private List<Notif> notifList;
    private NotificationAdapter adapter;

    Boolean isGrided;

    public Active() {
        // Required empty public constructor
        isGrided = false;
        playGround = new PlayGround(getContext());
        agent = new FirebaseAgent(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v;
        v = inflater.inflate(R.layout.fragment_active, container, false);
        notif_list = v.findViewById(R.id.active_list);
        setRetainInstance(true);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        active_user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        notifList = new ArrayList<>();

        LinearLayoutManager linear = new LinearLayoutManager(getContext());
        notif_list.setLayoutManager(linear);
        notif_list.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new ItemDecorator(getContext(), ItemDecorator.VERTICAL_LIST, 2);
        notif_list.addItemDecoration(itemDecoration);

        profiles = storage.getReferenceFromUrl(Constants.STORAGE_PATH_URL).child(Constants.STORAGE_PATH_USERS);
        users = database.getReference().child(Constants.DATABASE_PATH_USERS);
        online = database.getReference().child(Constants.DATABASE_PATH_ONLINE_PLAYERS);
        notifications = database.getReference().child(Constants.DATABASE_PATH_NOTIFICATIONS);
        if (auth.getCurrentUser() != null) {
            notifications.child(active_user.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null){
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            Notif notif = ds.getValue(Notif.class);
                            notifList.add(notif);
                            adapter.notifyDataSetChanged();
                        }
                    }else {
                        Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(getContext(), "nothing to show login first", Toast.LENGTH_SHORT).show();
        }

        adapter = new NotificationAdapter(getContext(), notifList);
        notif_list.setAdapter(adapter);
    }
}
