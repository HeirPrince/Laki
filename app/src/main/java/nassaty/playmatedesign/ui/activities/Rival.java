package nassaty.playmatedesign.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.fragments.SimpleBus;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;
import nassaty.playmatedesign.ui.utils.GameServiceHelper;
import nassaty.playmatedesign.ui.utils.ImageUtils;
import nassaty.playmatedesign.ui.utils.PlayGround;

public class Rival extends AppCompatActivity {

    public static final int GROUP_TYPE = 1;
    public static final int FRIEND_TYPE = 0;
    GameServiceHelper helper;
    private PlayGround playGround;
    private FirebaseAgent agent;
    private FirebaseAuth auth;
    private FirebaseUser active;
    private ImageUtils imageUtils;

    @BindView(R.id.wheel)ImageView wheel;
    @BindView(R.id.game_toolbar)Toolbar toolbar;
    @BindView(R.id.trigger_image)CircleImageView trigger_image;
    @BindView(R.id.trigger_name)TextView trigger_name;
    @BindView(R.id.trigger_price)TextView trigger_price;
    @BindView(R.id.trigger_position)TextView trigger_position;
    @BindView(R.id.opponent_image)CircleImageView opponent_image;
    @BindView(R.id.opponent_name)TextView opponent_name;
    @BindView(R.id.opponent_price)TextView opponent_price;
    @BindView(R.id.opponent_position)TextView opponent_position;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Playing...");

        auth = FirebaseAuth.getInstance();
        active = auth.getCurrentUser();
        playGround = new PlayGround(this);
        agent = new FirebaseAgent(this);
        imageUtils = new ImageUtils(this);

        if (active != null){
            helper = new GameServiceHelper(this, active);
        }
    }

    public void decideWinner(int pos, int tpos, int opos) {
        if (pos == tpos) {
            Toast.makeText(this, "trigger wins", Toast.LENGTH_SHORT).show();
        } else if (pos == opos) {
            Toast.makeText(this, "opponent wins", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "both failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void setHeader(SimpleBus bus){
        //user
//        agent.getUserByPhone(player.getPhone(), new FirebaseAgent.addOnNameChangeListener<String>() {
//            @Override
//            public void onNameChangedListener(String name, String image) {
//                imageUtils.displayCircledImage(image, trigger_image);
//                trigger_name.setText(name);
//            }
//        });
//        trigger_price.setText(player.getAmount());
//        trigger_position.setText(player.getPosition());
//        Toast.makeText(this, "rival data : "+player.getPhone(), Toast.LENGTH_SHORT).show();
        if (bus != null){
            Toast.makeText(this, bus.getData(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "null bus", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.replay:
                Toast.makeText(this, "replay action clicked ma man :D", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
