package nassaty.playmatedesign.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;

/**
 * Created by Prince on 10/11/2017.
 */

public class ChooseGame extends AppCompatActivity {

    @BindView(R.id.f2f)ImageButton btnF2f;
    @BindView(R.id.grp)ImageButton btnGrp;
    @BindView(R.id.chp)ImageButton btnChp;
    @BindView(R.id.fav)ImageButton btnFav;
    private MaterialNumberPicker positionPicker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_choose_game);
        ButterKnife.bind(this);
        initGUI();

    }

    public void popDialog() {
        positionPicker = new MaterialNumberPicker.Builder(this)
                .minValue(0)
                .maxValue(36)
                .defaultValue(0)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .enableFocusability(true)
                .wrapSelectorWheel(true)
                .build();
    }

    public void approveNotification() {

        popDialog();
        AlertDialog done = new AlertDialog.Builder(this)
                .setTitle("Choose Position")
                .setView(positionPicker)
                .setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();

    }

    public void initGUI(){

        btnF2f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent v = new Intent(ChooseGame.this, chooseFriend.class);
                startActivity(v);
            }
        });

        btnGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseGame.this, chooseGroup.class));
            }
        });

        btnChp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChooseGame.this, "Championship", Toast.LENGTH_SHORT).show();
            }
        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChooseGame.this, "GroupFrag", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
