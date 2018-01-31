package nassaty.playmatedesign.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.utils.DialogHelper;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;
import nassaty.playmatedesign.ui.utils.GameServiceHelper;
import nassaty.playmatedesign.ui.utils.PlayGround;

public class SingleFree extends AppCompatActivity {

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
    @BindView(R.id.progress)CircleProgressBar progress;
    @BindView(R.id.loading)RelativeLayout loadingView;

    private FirebaseAgent agent;
    private FirebaseAuth auth;
    private FirebaseUser active;
    private GameServiceHelper helper;
    private PlayGround playGround;
    private DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_free);
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
                            Glide.with(SingleFree.this).load(url).into(pic);

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

        helper.checkBet(auth.getCurrentUser().getPhoneNumber(), new GameServiceHelper.isBetFound() {
            @Override
            public void isFound(Boolean status) {
                if (status){
                    ValueAnimator animator = ValueAnimator.ofInt(0, 100);

                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int p = (int) valueAnimator.getAnimatedValue();
                            progress.setProgress(p);
                            if (p == 5000){
                                Toast.makeText(SingleFree.this, "done", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    animator.setRepeatCount(0);
                    animator.setDuration(5000);
                    animator.start();

                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            playGround.startGame(wheel, new PlayGround.getGameResults() {
                                @Override
                                public void results(int finalPos) {
                                    if (finalPos == getIntent().getIntExtra("pos", -1)){
                                        //TODO check if #stars <= 5;
                                        replay.setVisibility(View.VISIBLE);
                                        Toast.makeText(SingleFree.this, "winner", Toast.LENGTH_SHORT).show();
                                    }else if (finalPos == Integer.valueOf(opp_pos.getText().toString())){
                                        replay.setVisibility(View.VISIBLE);
                                        Toast.makeText(SingleFree.this, "pc won", Toast.LENGTH_SHORT).show();
                                    }else {
                                        //TODO check if #stars <= 5;
                                        replay.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    });
                }else {
                    Toast.makeText(SingleFree.this, "you have no bet placed", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        playGround.countDown(playGround.overK(), new PlayGround.dotheCountDown<Boolean>() {
//            @Override
//            public void isCountdown(long tick) {
//                replay.setVisibility(View.GONE);
//                Toast.makeText(SingleFree.this, String.valueOf(tick), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void isCountdownDone() {
//                final List<String> items = new ArrayList<>();
//                items.add("Pay");
//                items.add("Cancel");
//                replay.setVisibility(View.GONE);
//
//            }
//        });
    }

    public void start(View view) {
        startGame();
    }
}
