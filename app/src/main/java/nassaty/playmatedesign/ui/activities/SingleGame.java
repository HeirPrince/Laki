package nassaty.playmatedesign.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.utils.DialogHelper;

public class SingleGame extends AppCompatActivity {

    @BindView(R.id.toolbar)Toolbar toolbar;
    private DialogHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_game);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        helper = new DialogHelper(this);

    }


    public void singlefree(View view) {
        helper.choosePos(new DialogHelper.onPositionSelected() {
            @Override
            public void selectedPosition(int pos) {
                Intent i = new Intent(SingleGame.this, SingleFree.class);
                i.putExtra("pos", pos);
                startActivity(i);
            }
        });
    }

    public void singlepayed(View view) {
        helper.choosePos(new DialogHelper.onPositionSelected() {
            @Override
            public void selectedPosition(int pos) {
                Intent i = new Intent(SingleGame.this, SinglePayed.class);
                i.putExtra("pos", pos);
                startActivity(i);
            }
        });
    }

    public void start(View view) {

    }
}
