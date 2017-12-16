package nassaty.playmatedesign.ui.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.utils.GameServiceHelper;

public class AddToken extends AppCompatActivity {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.unit)TextInputEditText unitField;
    @BindView(R.id.number)TextInputEditText numberField;

    private GameServiceHelper gameService;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_token);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tokens");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        gameService = new GameServiceHelper(this, auth.getCurrentUser());

    }

    public void payToken(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setTitle("Pay Tokens");
        dialog.setContentView(R.layout.dialog_pay);

        final CardForm cardForm = dialog.findViewById(R.id.card_form);
        TextView pay, cancel, choose;

        pay = dialog.findViewById(R.id.pay);
        cancel = dialog.findViewById(R.id.cancel);
        choose = dialog.findViewById(R.id.choose_card);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .setup(this);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = Integer.valueOf(numberField.getText().toString());
                int amount = Integer.valueOf(unitField.getText().toString());

                gameService.addGameToken(number, amount, new GameServiceHelper.addnewToken() {
                    @Override
                    public void tokenCallBack(Boolean status) {
                        if (status){
                            Toast.makeText(AddToken.this, "added", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(AddToken.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialog.show();

    }
}
