package nassaty.playmatedesign.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.utils.GameServiceHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class chooseToken extends Fragment implements BlockingStep {

    private TextView val, tokens, cash;
    private Button plus;
    private Button minus;
    private GameServiceHelper gameServiceHelper;
    private FirebaseAuth auth;
    private FirebaseUser active;
    int current = 1;
    int amount;



    public chooseToken() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_token, container, false);
        val = v.findViewById(R.id.value);
        val.setText(String.valueOf(current));
        cash = v.findViewById(R.id.cash);
        tokens = v.findViewById(R.id.total);
        plus = v.findViewById(R.id.plus);
        minus = v.findViewById(R.id.minus);

        auth = FirebaseAuth.getInstance();
        active = auth.getCurrentUser();
        gameServiceHelper = new GameServiceHelper(getContext(), active);

        amount = 500;//replace with user action
        updateUI();

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToken();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeToken();
            }
        });

        return v;
    }

    public void updateUI(){
        gameServiceHelper.getTokenCount(active.getPhoneNumber(), 500, new GameServiceHelper.TokenCount() {
            @Override
            public void tokenNumber(int total) {
                if (total == 0){
                    val.setText(0);
                }else {
                    tokens.setText(String.valueOf(total));
                    cash.setText(String.valueOf(amount * current)+"$");
                }
            }
        });
    }

    private void removeToken() {
        if (current > 1){
            current = current - 1;
            val.setText(String.valueOf(current));
            cash.setText(String.valueOf(amount * current)+"$");
        }
        if (val.getText().toString().contains(String.valueOf(1))){
            minus.setEnabled(false);
            plus.setEnabled(true);
        }
    }

    private void addToken() {
        gameServiceHelper.getTokenCount(active.getPhoneNumber(), amount, new GameServiceHelper.TokenCount() {
            @Override
            public void tokenNumber(int total) {
                if (total == 0){
                    val.setText(0);
                }else {
                    current = current + 1;
                    cash.setText(String.valueOf(amount * current)+"$");
                    val.setText(String.valueOf(current));
                    if (current == total) {
                        plus.setEnabled(false);
                        minus.setEnabled(true);
                    }
                }
            }
        });

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback onNextClickedCallback) {
        onNextClickedCallback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback onCompleteClickedCallback) {
        onCompleteClickedCallback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback onBackClickedCallback) {

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError verificationError) {

    }
}
