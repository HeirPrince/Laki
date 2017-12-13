package nassaty.playmatedesign.ui.fragments;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.greenrobot.eventbus.EventBus;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.data.SpecialBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class trigger_Position extends Fragment implements BlockingStep {

    private Button pick;
    private MaterialNumberPicker positionPicker;
    private String cardn, carde, cardc, opponent;
    private int amt, qty, pos;
    private SpecialBus next;
    private SpecialBus specialBus;

    public trigger_Position() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trigger__position, container, false);
        pick = view.findViewById(R.id.position);


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approvePosition();
            }
        });
        return view;
    }

    public void approvePosition() {
        setupDialog();
        new AlertDialog.Builder(getContext())
                .setTitle("Choose a Position")
                .setView(positionPicker)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pos = positionPicker.getValue();
                        sendPosition(pos, qty, amt, cardn, carde, cardc);
                    }
                }).show();
    }

    public void sendPosition(int pos, int qty, int amt, String cardn, String carde, String cardc){
        next = new SpecialBus();
        next.setPos(pos);
        next.setQty(qty);
        next.setAmt(amt);
        next.setCardNumber(cardn);
        next.setCardExpiry(carde);
        next.setCardCvv(cardc);
        EventBus.getDefault().post(next);
    }



    public void setupDialog() {
        positionPicker = new MaterialNumberPicker.Builder(getContext())
                .minValue(0)
                .maxValue(36)
                .defaultValue(0)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
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
