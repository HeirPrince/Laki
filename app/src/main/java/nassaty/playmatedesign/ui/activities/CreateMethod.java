package nassaty.playmatedesign.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;

public class CreateMethod extends AppCompatActivity {


    @BindView(R.id.toolbar)Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_method);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }
}
