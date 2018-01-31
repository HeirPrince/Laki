package nassaty.playmatedesign.ui.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.utils.DialogHelper;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;
import nassaty.playmatedesign.ui.utils.GameServiceHelper;
import nassaty.playmatedesign.ui.utils.PlayGround;

public class SinglePayed extends AppCompatActivity {

    @BindView(R.id.profile_image)
    CircleImageView pic;
    @BindView(R.id.profile_name)
    TextView nm;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.position)
    TextView pos;
    @BindView(R.id.opp_pos)
    TextView opp_pos;
    @BindView(R.id.wheel)ImageView wheel;
    @BindView(R.id.replay)FloatingActionButton replay;

    private FirebaseAgent agent;
    private FirebaseAuth auth;
    private FirebaseUser active;
    private GameServiceHelper helper;
    private PlayGround playGround;
    private DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_payed);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        active = auth.getCurrentUser();
        helper = new GameServiceHelper(this, active);
        dialogHelper = new DialogHelper(this);
        playGround = new PlayGround(this);


        if (active.getPhoneNumber() != null){
            agent = new FirebaseAgent(this);
            initViews();
        }else{
            //auth
        }
    }

    public void initViews(){
        startGame();
        pos.setText(String.valueOf(getIntent().getIntExtra("pos", -1)));
        opp_pos.setText(String.valueOf(helper.getRandomPos()));
        agent.getUserByPhone(active.getPhoneNumber(), new FirebaseAgent.addOnNameChangeListener<String>() {
            @Override
            public void onNameChangedListener(String name, String image) {
                agent.downloadImage(image, Constants.STORAGE_PATH_USERS, new FirebaseAgent.OnStatusListener<Boolean>() {
                    @Override
                    public void isComplete(Boolean status, String url) {
                        if (status){
                            Glide.with(SinglePayed.this).load(url).into(pic);

                        }else{
                            //load default image
                        }
                    }

                    @Override
                    public void isFailed(Boolean failed) {

                    }
                });
                nm.setText(name);
            }
        });
    }

    public void startGame(){
        playGround.countDown(playGround.overK(), new PlayGround.dotheCountDown<Boolean>() {
            @Override
            public void isCountdown(long tick) {
                replay.setVisibility(View.GONE);
                Toast.makeText(SinglePayed.this, String.valueOf(tick), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void isCountdownDone() {
                final List<String> items = new ArrayList<>();
                items.add("Pay");
                items.add("Cancel");
                replay.setVisibility(View.GONE);
                playGround.startGame(wheel, new PlayGround.getGameResults() {
                    @Override
                    public void results(int finalPos) {
                        if (finalPos == getIntent().getIntExtra("pos", -1)){
                            //TODO show celebrations and get money
                            replay.setVisibility(View.GONE);
                            Toast.makeText(SinglePayed.this, "winner", Toast.LENGTH_SHORT).show();
                        }else if (finalPos == Integer.valueOf(opp_pos.getText().toString())){
                            dialogHelper.showSimpleDialog("PAY", "Cancel", "UmeBet", "You can play another game if you want", new DialogHelper.clickedBtn() {
                                @Override
                                public void clicked(Boolean state) {
                                    if (state){
                                        //TODO proceed with payment
                                        replay.setVisibility(View.VISIBLE);
                                        Toast.makeText(SinglePayed.this, "true", Toast.LENGTH_SHORT).show();
                                    }else {
                                        replay.setVisibility(View.GONE);
                                        Toast.makeText(SinglePayed.this, "false", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //TODO get money from the loser
                            Toast.makeText(SinglePayed.this, "pc won", Toast.LENGTH_SHORT).show();
                        }else {
                            dialogHelper.showSimpleDialog("PAY", "Cancel", "UmeBet", "You can play another game if you want", new DialogHelper.clickedBtn() {
                                @Override
                                public void clicked(Boolean state) {
                                    if (state){
                                        //TODO Proceed with payments
                                        replay.setVisibility(View.VISIBLE);
                                        Toast.makeText(SinglePayed.this, "true", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(SinglePayed.this, "false", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Toast.makeText(SinglePayed.this, "all lost", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void start(View view) {
        startGame();
    }

}
