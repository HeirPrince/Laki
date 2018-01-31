package nassaty.playmatedesign.ui.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.List;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import nassaty.playmatedesign.R;

/**
 * Created by Prince on 11/5/2017.
 */

public class DialogHelper {

    private Context ctx;
    private MaterialNumberPicker picker;
    private LovelyChoiceDialog choiceDialog;
    private Dialog standardDialog;
    private LovelyProgressDialog progressDialog;
    private LovelyTextInputDialog textInputDialog;
    private LovelyInfoDialog infoDialog;

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

    public void showSimpleDialog(String btnPos, String btnNeg, String title, String message, final clickedBtn clickedBtn){

        final AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnPos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickedBtn.clicked(true);
                    }
                })
                .setNegativeButton(btnNeg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clickedBtn.clicked(false);
                    }
                }).create();
        dialog.show();
    }

    public void showInfoDialog(String title, String message){
        infoDialog = new LovelyInfoDialog(ctx,R.style.AppTheme)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false);
        infoDialog.show();
    }

    public void showChoiceDialog(List<String> choices, String title, String message){
        choiceDialog = new LovelyChoiceDialog(ctx, R.style.AppTheme)
                .setTitle(title)
                .setTitle(message)
                .setItemsMultiChoice(choices, new LovelyChoiceDialog.OnItemsSelectedListener<String>() {
                    @Override
                    public void onItemsSelected(List<Integer> list, List<String> list1) {
                        String selected = list1.toString();
                        Toast.makeText(ctx, selected, Toast.LENGTH_SHORT).show();
                    }
                });

        choiceDialog.show();
    }

    public void showStandardDialog(String btnPos, String btnNeg, String title, String message, final clickedBtn clickedBtn){
        standardDialog = new LovelyStandardDialog(ctx, R.style.AppTheme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnPos, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickedBtn.clicked(true);
                    }
                }).setNegativeButton(btnNeg, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickedBtn.clicked(false);
                    }
                }).show();
    }

    //interface
    public interface onPositionSelected {
        void selectedPosition(int pos);
    }

    public interface clickedBtn{
        void clicked(Boolean state);
    }


}
