package nassaty.playmatedesign.ui.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import nassaty.playmatedesign.R;

/**
 * Created by Prince on 11/5/2017.
 */

public class DialogHelper {

    private Context ctx;
    private MaterialNumberPicker picker;

    public DialogHelper(Context ctx) {
        this.ctx = ctx;
    }

    public void choosePos(final onPositionSelected onPositionSelected) {
        setupDialog();
        new AlertDialog.Builder(ctx)
                .setTitle("Choose Position")
                .setView(picker)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    onPositionSelected.selectedPosition(picker.getValue());
                    }
                }).show();

    }

    public void setupDialog() {
        picker = new MaterialNumberPicker.Builder(ctx)
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

    public void showSimpleDialog(String title, String msg){

        final Dialog standardDialog = new LovelyStandardDialog(ctx)
                .setTopColorRes(R.color.colorAccent)
                .setButtonsColorRes(R.color.colorAccent)
                .setIcon(R.drawable.payment)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ctx, "done", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public interface onPositionSelected {
        void selectedPosition(int pos);
    }

}
