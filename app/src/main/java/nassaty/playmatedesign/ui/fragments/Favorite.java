package nassaty.playmatedesign.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import nassaty.playmatedesign.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favorite extends Fragment {


    private FirebaseAuth auth;

    public Favorite() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.calendar_year_label_text_view, container, false);
        auth = FirebaseAuth.getInstance();


        return v;
    }

}
