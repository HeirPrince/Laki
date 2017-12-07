package nassaty.playmatedesign.ui.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import nassaty.playmatedesign.ui.fragments.Checkout;
import nassaty.playmatedesign.ui.fragments.Process_payment;
import nassaty.playmatedesign.ui.fragments.choosePosition;

/**
 * Created by Prince on 10/30/2017.
 */

public class StepAdapter extends AbstractFragmentStepAdapter {

    private static String posKey = "posKey";

    public StepAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }


    @Override
    public Step createStep(int position) {


        Bundle b = new Bundle();
        b.putInt(posKey, position);

        switch (position){
            case 0:
                Process_payment pay = new Process_payment();
                pay.setArguments(b);
                return pay;

            case 1:
                Checkout checkout = new Checkout();
                checkout.setArguments(b);
                return checkout;

            case 2:
                choosePosition pos = new choosePosition();
                pos.setArguments(b);
                return pos;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(int position) {
        StepViewModel.Builder viewModel = new StepViewModel.Builder(context);
        switch (position){
            case 0:
                viewModel
                        .setTitle("Card Details")
                        .setSubtitle("Fill in card details")
                        .setEndButtonLabel("NEXT");
                break;

            case 1:
                viewModel
                        .setTitle("CheckOut")
                        .setSubtitle("Complete Checkout")
                        .setEndButtonLabel("NEXT");
                break;

            case 2:
                viewModel
                        .setTitle("Position")
                        .setSubtitle("Click below to choose position")
                        .setBackButtonLabel("BACK")
                        .setEndButtonLabel("COMPLETE");
                break;

        }

        return viewModel.create();
    }
}
