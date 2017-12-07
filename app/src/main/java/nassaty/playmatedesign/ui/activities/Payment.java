package nassaty.playmatedesign.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.data.SpecialBus;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.Notif;
import nassaty.playmatedesign.ui.model.Player;
import nassaty.playmatedesign.ui.utils.GameServiceHelper;
import nassaty.playmatedesign.ui.utils.PlayGround;
import nassaty.playmatedesign.ui.utils.Trigger_StepAdapter;

public class Payment extends AppCompatActivity implements StepperLayout.StepperListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.stepper)StepperLayout stepperLayout;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private GameServiceHelper gameServiceHelper;
    private String opponent;
    private PlayGround playGround;
    private Notif notification;
    private SpecialBus specialBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pay Game");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        opponent = getIntent().getStringExtra("opponent");
        playGround = new PlayGround(this);
        stepperLayout.setListener(this);
        notification = new Notif();

        if (user != null){
            stepperLayout.
                    setAdapter(new Trigger_StepAdapter(getSupportFragmentManager(),
                            this));
            gameServiceHelper = new GameServiceHelper(this, user);

        }else {
            //authenticate
        }

    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void getAll(SpecialBus bus){
        String cn = bus.getCardNumber();
        String ce = bus.getCardExpiry();
        String cv = bus.getCardCvv();
        int qty = bus.getQty();
        int amt = bus.getAmt();
        int position = bus.getPos();

        notification.setSender(user.getPhoneNumber());
        notification.setSender_position(position);
        notification.setReceiver(opponent);
        notification.setType(Constants.FRIEND_TYPE);
        notification.setRead(false);

        specialBus = bus;
    }

    public void notifyOpponent(final Notif notification){
        playGround.sendGameNotification(notification, notification.getReceiver(), new PlayGround.NotificationListener<Boolean>() {
            @Override
            public void isNotificationSent(Boolean status) {
                if (status){
                    Toast.makeText(Payment.this, "delivered", Toast.LENGTH_SHORT).show();
                    Player player = new Player();
                    player.setPhone(user.getPhoneNumber());
                    player.setCardNumber(specialBus.getCardNumber());
                    player.setCardCVV(specialBus.getCardCvv());
                    player.setCardEXP(specialBus.getCardExpiry());
                    player.setPosition(specialBus.getPos());

                    gameServiceHelper.setGameSession(player, new GameServiceHelper.createSession() {
                        @Override
                        public void isSessionCreated(Boolean status) {
                            if (status){
                                Toast.makeText(Payment.this, "game session created successfully", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Payment.this, "game session creation failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Payment.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCompleted(View completeButton) {
        if (notification != null){
            notifyOpponent(notification);
        }else {
            Toast.makeText(this, "empty notification", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

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
