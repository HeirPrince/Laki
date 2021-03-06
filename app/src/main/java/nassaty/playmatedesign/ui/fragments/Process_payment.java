package nassaty.playmatedesign.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.greenrobot.eventbus.EventBus;

import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.data.SpecialBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class Process_payment extends Fragment implements BlockingStep {

    private CardForm cardForm;
    private SpecialBus all;

    public Process_payment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_spin, container, false);
        cardForm = v.findViewById(R.id.card_form);
        setupForm();

        return v;
    }

    //sends card data to next fragment
    public void sendCard(String number, String month, String year, String cvv){
        all = new SpecialBus();
        all.setCardNumber(number);
        all.setCardExpiry(month+"/"+year);
        all.setCardCvv(cvv);
        EventBus.getDefault().post(all);
    }

    public void setupForm() {
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .setup(getActivity());
    }

    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {

                if (cardForm.isValid()){
                    sendCard(cardForm.getCardNumber(), cardForm.getExpirationMonth(), cardForm.getExpirationYear(), cardForm.getCvv());
                    callback.goToNextStep();
                }else {
                    Toast.makeText(getContext(), "card not valid", Toast.LENGTH_SHORT).show();
                }

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
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
}
