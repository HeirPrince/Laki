package nassaty.playmatedesign.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.activities.Multi;
import nassaty.playmatedesign.ui.adapters.GroupAdapter;
import nassaty.playmatedesign.ui.model.GroupNotif;
import nassaty.playmatedesign.ui.utils.PlayGround;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFrag extends Fragment {


    private FirebaseAuth auth;
    private FirebaseUser user;
    private RecyclerView group_list;
    private LinearLayout header;
    private GroupAdapter adapter;
    private PlayGround playGround;

    public GroupFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups, container, false);
        auth = FirebaseAuth.getInstance();
        group_list = v.findViewById(R.id.group_list);
        header = v.findViewById(R.id.header);
        playGround = new PlayGround(getContext());
        user = auth.getCurrentUser();

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Multi.class));
            }
        });

        return v;
    }

    public void showList(){
        group_list.setHasFixedSize(true);
        GridLayoutManager grid = new GridLayoutManager(getContext(), 2);
        group_list.setLayoutManager(grid);
        final List<GroupNotif> notifList = new ArrayList<>();

        playGround.getGNotifs(user.getPhoneNumber(), new PlayGround.groupNotifications() {
            @Override
            public void groups(GroupNotif notif) {
                notifList.add(notif);
            }
        });

        adapter = new GroupAdapter(getContext(), notifList);
        group_list.setAdapter(adapter);
    }

}
