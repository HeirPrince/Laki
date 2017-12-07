package nassaty.playmatedesign.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.User;

public class Profile extends AppCompatActivity {


    @BindView(R.id.toolbar)Toolbar mToolbar;
    @BindView(R.id.profile_pic)CircleImageView profile_image;
    @BindView(R.id.profile_phone)TextView profile_phone;
    private FirebaseAuth auth;
    private FirebaseUser active_user;
    private DatabaseReference users;
    private StorageReference profiles;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        active_user = auth.getCurrentUser();
        profiles = FirebaseStorage.getInstance().getReferenceFromUrl(Constants.STORAGE_PATH_URL).child(Constants.STORAGE_PATH_USERS);
        users = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_USERS);

        if (active_user != null){
            initHeader();
        }

    }

    public void initHeader(){
        if (active_user.getPhoneNumber() != null){
            users.child(active_user.getPhoneNumber()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    if (user != null){
                        Toast.makeText(Profile.this, user.getPhone_number(), Toast.LENGTH_SHORT).show();
                        profile_phone.setText(user.getPhone_number());
                        StorageReference rs = profiles.child(user.getImage());
                        Glide.with(Profile.this).load(rs).centerCrop().crossFade().into(profile_image);
                    }else{
                        Toast.makeText(Profile.this, "invalid user", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(this, "user not found", Toast.LENGTH_SHORT).show();
        }

    }

}
