package nassaty.playmatedesign.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormFieldFocusedListener;
import com.braintreepayments.cardform.view.CardForm;
import com.cooltechworks.creditcarddesign.CreditCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;

public class AddCreditCard extends AppCompatActivity {

    private static int SCAN_CARD = 1;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.credit_card)
    CreditCardView creditCardView;
    @BindView(R.id.card_form)CardForm cardForm;
    @BindView(R.id.btnSave)Button saveCard;

    private FirebaseAgent agent;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_card);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Card");
        agent = new FirebaseAgent(this);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        cardForm.cardRequired(true)
                .cvvRequired(true)
                .expirationRequired(true)
                .actionLabel("Save Card")
        .setup(this);


        cardForm.setOnFormFieldFocusedListener(new OnCardFormFieldFocusedListener() {
            @Override
            public void onCardFormFieldFocused(View field) {

            }
        });

        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()){
                    hideKeyBoard();
                    final nassaty.playmatedesign.ui.model.CreditCard card = new nassaty.playmatedesign.ui.model.CreditCard();
                    card.setCvv(cardForm.getCvv());
                    card.setHolderNumber(cardForm.getCardNumber());
                    card.setExpiry(cardForm.getExpirationMonth()+"/"+cardForm.getExpirationYear());
                    card.setHolderPhone(firebaseUser.getPhoneNumber());
                    agent.addCreditCard(card, new FirebaseAgent.isCardAddedListener<Boolean>() {
                        @Override
                        public void isComplete(Boolean status, String key) {
                            if (status){
                                Toast.makeText(AddCreditCard.this, "card added", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void isFailed(Boolean status, String msg) {

                        }
                    });
                }else {
                    Toast.makeText(AddCreditCard.this, "invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void ScanCard(View view) {
        cardForm.isCardScanningAvailable();
        cardForm.scanCard(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_CARD) {
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard card = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                creditCardView.setCardNumber(card.getRedactedCardNumber());
                creditCardView.setCVV(card.cvv);
                creditCardView.setCardExpiry(card.expiryMonth + "/" + card.expiryYear);
                creditCardView.setCardHolderName(card.cardholderName);
            }
        }
    }

    private void hideKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void saveCard() {

    }
}
