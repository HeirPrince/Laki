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
public class trigger_payment extends Fragment implements BlockingStep {

    private CardForm cardForm;
    onCardValidated mCallback;
    private SpecialBus all;

    public interface onCardValidated{
        void Data(CardForm cardForm);
    }

    public trigger_payment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trigger_payment, container, false);
        cardForm = view.findViewById(R.id.card_form);
        setupForm();
        return view;
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
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        if (cardForm.isValid()){
            sendCard(cardForm.getCardNumber(), cardForm.getExpirationMonth(), cardForm.getExpirationYear(), cardForm.getCvv());
            callback.goToNextStep();
        }else {
            Toast.makeText(getContext(), "Fill out credit card first", Toast.LENGTH_SHORT).show();
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
