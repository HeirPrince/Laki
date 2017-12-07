package nassaty.playmatedesign.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.data.SpecialBus;

/**
 * Created by Prince on 11/12/2017.
 */

public class Checkout extends Fragment implements BlockingStep {

    private Button increase, decrease;
    private TextView qty, total;
    private TextInputEditText amount;
    private String nm, exp, cv;
    private SpecialBus next;


    private int quantity, current;

    public Checkout() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        increase = view.findViewById(R.id.increase);
        decrease = view.findViewById(R.id.decrease);
        qty = view.findViewById(R.id.qty);
        total = view.findViewById(R.id.total);
        amount = view.findViewById(R.id.amount);
        quantity = 0;

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty.setText(String.valueOf(quantity));
                current = Integer.valueOf(amount.getText().toString());
                total.setText(String.valueOf(current * quantity));
                quantity ++;
                sendAmount();
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty.setText(String.valueOf(quantity));
                int current = Integer.valueOf(amount.getText().toString());
                total.setText(String.valueOf(current * quantity));
                quantity --;
                sendAmount();
            }
        });

        return view;
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void getPrevious(SpecialBus bus){
        String prev = bus.getCardNumber();
        String cvv = bus.getCardCvv();
        String expiry = bus.getCardExpiry();
        next = new SpecialBus();
        next.setCardNumber(prev);
        next.setCardCvv(cvv);
        next.setCardExpiry(expiry);
        next.setQty(quantity);
        next.setAmt(current);
    }

    public void sendAmount(){
        EventBus.getDefault().post(next);
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
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        if (quantity > 0 && !TextUtils.isEmpty(amount.getText())){
            callback.goToNextStep();
        }else {
            Toast.makeText(getContext(), "fields missing", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
