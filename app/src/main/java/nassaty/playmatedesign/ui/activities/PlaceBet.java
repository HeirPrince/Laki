package nassaty.playmatedesign.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.model.Bet;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;

public class PlaceBet extends AppCompatActivity {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.unit)TextInputEditText unitField;
    private FirebaseAuth auth;
    private FirebaseAgent agent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_token);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tokens");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        agent = new FirebaseAgent(this);
    }

    public void payToken(View view) {
        int amount = Integer.valueOf(unitField.getText().toString());

        if (amount >= 500){
            final Bet bet = new Bet(amount);
            agent.betFromReserved(auth.getCurrentUser().getPhoneNumber(), bet, new FirebaseAgent.placeBet() {
                @Override
                public void isBetPlaced(Boolean state) {
                    if (state){
                        Toast.makeText(PlaceBet.this, "bet placed", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(PlaceBet.this, "bet not placed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(this, "bets must be not less than 500 RWF", Toast.LENGTH_SHORT).show();
        }

    }
}
