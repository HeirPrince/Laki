package nassaty.playmatedesign.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.utils.GameServiceHelper;
import nassaty.playmatedesign.ui.utils.PlayGround;

public class Rival extends AppCompatActivity {

    public static final int GROUP_TYPE = 1;
    public static final int FRIEND_TYPE = 0;
    @BindView(R.id.wheel)ImageView wheel;
    @BindView(R.id.game_toolbar)Toolbar toolbar;
    GameServiceHelper helper;
    private PlayGround playGround;

    private FirebaseAuth auth;
    private FirebaseUser active;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Playing...");

        auth = FirebaseAuth.getInstance();
        active = auth.getCurrentUser();
        playGround = new PlayGround(this);

        if (active != null){
            helper = new GameServiceHelper(this, active);
        }
    }

    public void decideWinner(int pos, int tpos, int opos) {
        if (pos == tpos) {
            Toast.makeText(this, "trigger wins", Toast.LENGTH_SHORT).show();
        } else if (pos == opos) {
            Toast.makeText(this, "opponent wins", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "both failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.replay:
                Toast.makeText(this, "replay action clicked ma man :D", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
