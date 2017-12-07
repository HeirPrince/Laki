package nassaty.playmatedesign.ui.fragments;


import android.content.DialogInterface;
import android.content.Intent;
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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.activities.Rival;
import nassaty.playmatedesign.ui.data.SpecialBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class choosePosition extends Fragment implements BlockingStep {

    private Button pick;
    private MaterialNumberPicker positionPicker;
    private String cardn, carde, cardc;
    private int amt, qty, pos;
    private Boolean isTrigger;
    private SpecialBus next;

    public choosePosition() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_choose_position, container, false);

        pick = v.findViewById(R.id.position);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approvePosition();
            }
        });

        return v;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAll(SpecialBus b) {
        cardn = b.getCardNumber();
        carde = b.getCardExpiry();
        cardc = b.getCardCvv();
        qty = b.getQty();
        amt = b.getAmt();
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

    public void sendPosition(int pos, int qty, int amt, String cardn, String carde, String cardc) {
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

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
        Intent intent = new Intent(getContext(), Rival.class);
        getContext().startActivity(intent);
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
