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
import nassaty.playmatedesign.ui.model.Player;
import nassaty.playmatedesign.ui.utils.GameServiceHelper;
import nassaty.playmatedesign.ui.utils.StepAdapter;

public class PayBack extends AppCompatActivity implements StepperLayout.StepperListener {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.stepper)StepperLayout stepperLayout;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String cardn, cardcvv, cardexpy;
    private int pos, qty;
    private String t;
    private SpecialBus specialBus;
    private GameServiceHelper gameServiceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        t = getIntent().getStringExtra("sender");
        stepperLayout.setListener(this);

        if (user != null){
            stepperLayout.
                    setAdapter(new StepAdapter(
                            getSupportFragmentManager(),
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

        specialBus = bus;
    }

    public void attach(){
        Player player = new Player();
        player.setPhone(user.getPhoneNumber());
        player.setCardNumber(specialBus.getCardNumber());
        player.setCardCVV(specialBus.getCardCvv());
        player.setCardEXP(specialBus.getCardExpiry());
        player.setPosition(specialBus.getPos());

        gameServiceHelper.attachToSession(player, new GameServiceHelper.attach() {
            @Override
            public void isAttached(Boolean status) {
                if (status){
                    Toast.makeText(PayBack.this, "opponent attached", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PayBack.this, "attachment failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCompleted(View completeButton) {
        attach();
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


