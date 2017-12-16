package nassaty.playmatedesign.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.fragments.Active;
import nassaty.playmatedesign.ui.fragments.Favorite;
import nassaty.playmatedesign.ui.fragments.Online;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.ActiveUser;
import nassaty.playmatedesign.ui.model.User;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;
import nassaty.playmatedesign.ui.utils.PlayGround;

public class MainActivity extends AppCompatActivity {


    private static final String KEY_TEXT_REPLY = "key_text_reply";
    int mRequestCode = 1000;
    private static final int RC_SIGN_IN = 123;
    public static final int RC_CONTACTS = 1;
    PlayGround playGround;
    FirebaseAgent agent;
    @BindView(R.id.toolbar)
    Toolbar tb;
    @BindView(R.id.viewpager)
    ViewPager pager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.fb)
    FabSpeedDial mfab;
    @BindView(R.id.drawer)
    NavigationView drawer;

    private int[] icons = new int[]
            {
                    R.drawable.about,
                    R.drawable.pending,
                    R.drawable.online
            };


    //firebase
    private FirebaseAuth auth;
    private DatabaseReference users;
    private FirebaseStorage storage;
    private StorageReference profiles;
    private DatabaseReference online;
    private FirebaseFirestore db;

    private static final int MY_SCAN_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(tb);
        initUI();

        playGround = new PlayGround(this);
        agent = new FirebaseAgent(this);
        online = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_ONLINE_PLAYERS);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null){
            authenticate();
        }else {
            agent.isRegistered(new FirebaseAgent.getInfo<Boolean>() {
                @Override
                public void isRegistered(Boolean state) {
                    if (state){
                        setupDrawer(auth.getCurrentUser().getPhoneNumber());
                        PlayGround.makeMeActive(auth.getCurrentUser().getPhoneNumber());
                    }else if (!state){
                        finish();
                        Intent i = new Intent(MainActivity.this, UserData.class);
                        i.putExtra("phone", auth.getCurrentUser().getPhoneNumber());
                        startActivity(i);
                    }
                }
            }, auth.getCurrentUser().getPhoneNumber());
        }
    }

    public void initUI(){
        if (pager != null) {
            setUpViewPager(pager);
        }
        tabs.setupWithViewPager(pager);
//        setupTabIcons();
        users = FirebaseDatabase.getInstance().getReference().child("users");
        storage = FirebaseStorage.getInstance();
        profiles = storage.getReferenceFromUrl(Constants.STORAGE_PATH_URL).child(Constants.STORAGE_PATH_USERS);


        mfab.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.user:
                        startActivity(new Intent(MainActivity.this, chooseFriend.class));
                        break;
                    case R.id.group:
                        startActivity(new Intent(MainActivity.this, Multi.class));
                        break;
                }
                return false;
            }
        });
    }

    public void setupDrawer(String phone){

        if (phone != null){

            final View header = drawer.inflateHeaderView(R.layout.drawer_header_new);
            final Button btnLogin = header.findViewById(R.id.login_state);

            users.child(phone).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final User user = dataSnapshot.getValue(User.class);

                    if (user != null) {
                        btnLogin.setVisibility(View.GONE);
                        final CircleImageView dimage = header.findViewById(R.id.drawer_image);
                        final TextView dphone = header.findViewById(R.id.drawer_phone);
                        final TextView dname = header.findViewById(R.id.drawer_name);
                        dname.setText(user.getUsername());
                        dphone.setText(user.getPhone_number());
                        agent.downloadImage(user.getImage(), Constants.STORAGE_PATH_USERS, new FirebaseAgent.OnStatusListener<Boolean>() {
                            @Override
                            public void isComplete(Boolean status, String url) {
                                if (status && url != null) {
                                    Glide.with(MainActivity.this)
                                            .load(url)
                                            .centerCrop()
                                            .crossFade(5000)
                                            .into(dimage);
                                }
                            }

                            @Override
                            public void isFailed(Boolean failed) {

                            }
                        });

                        drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.pay:
                                        ViewCards();
                                        break;
                                    case R.id.fav:
                                        startActivity(new Intent(MainActivity.this, AddToken.class));
                                        break;

                                }

                                return false;
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //cancelled
                }
            });

        }
    }

    private void ViewCards() {
        startActivity(new Intent(MainActivity.this, ViewCreditCards.class));
    }



    private void setUpViewPager(ViewPager pager) {
        SlideAdapter adapter = new SlideAdapter(getSupportFragmentManager());
        adapter.addFragment(new Active());
        adapter.addFragment(new Online());
        adapter.addFragment(new Favorite());
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(icons[0]);
        tabs.getTabAt(1).setIcon(icons[1]);
        tabs.getTabAt(2).setIcon(icons[2]);
    }


    class SlideAdapter extends FragmentPagerAdapter {

        private String[] titles = new String[]{
                "Requests",
                "Active",
                "Favorite"
        };

        public List<Fragment> fragments = new ArrayList<>();


        public SlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    public void authenticate(){
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build())).build(), RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
            playGround.checkMeOut(auth.getCurrentUser().getPhoneNumber());
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, Profile.class));
                break;
            case R.id.out:
                if (auth.getCurrentUser().getPhoneNumber() != null){
                    online = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_ONLINE_PLAYERS);
                    online.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ActiveUser u = dataSnapshot.getValue(ActiveUser.class);
                            if (u != null){
                                Toast.makeText(MainActivity.this, "still online signing out...", Toast.LENGTH_SHORT).show();
                                online.child(auth.getCurrentUser().getPhoneNumber()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MainActivity.this, "out successfully", Toast.LENGTH_SHORT).show();
                                        auth.signOut();
                                        authenticate();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(MainActivity.this, "offline", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(this, "invalid phone number", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.addfriend:
                startActivity(new Intent(MainActivity.this, AddFriend.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                return;
            } else {
                if (response == null) {
                    Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Unknown error please try again.", Toast.LENGTH_SHORT).show();
                }
            }

        }else if (requestCode == MY_SCAN_REQUEST_CODE) {
            String results;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)){
                CreditCard card = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                results = "Card Number: "+card.getRedactedCardNumber() + "\n";
                Toast.makeText(this, results, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void confirmPicture(){
        users.child(auth.getCurrentUser().getPhoneNumber()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getImage() != null){
                    Toast.makeText(MainActivity.this, "has picture", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "has no picture", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
