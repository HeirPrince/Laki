package nassaty.playmatedesign.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.model.Reserve;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;

public class Reservation extends AppCompatActivity {

    private FirebaseAgent agent;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @BindView(R.id.amt)EditText amt;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reservations");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null){

        }else{
            agent = new FirebaseAgent(this);
        }
    }

    public void reserveAmt(View view) {
        Reserve reserve = new Reserve(Integer.valueOf(amt.getText().toString()));
        agent.reserveAmt(user.getPhoneNumber(), reserve, new FirebaseAgent.setReservation() {
            @Override
            public void isReserved(Boolean status) {
                if (status){
                    Toast.makeText(Reservation.this, "amt results", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Reservation.this, "amt not results", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
