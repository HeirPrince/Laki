package nassaty.playmatedesign.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;
import nassaty.playmatedesign.ui.utils.SecurityAgent;

public class groupGame extends AppCompatActivity {

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.participant_list)RecyclerView participant_list;
    @BindView(R.id.wheel)ImageView wheel;

    private FirebaseDatabase database;
    private DatabaseReference game_sessions;
    private FirebaseAgent agent;
    private SecurityAgent securityAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_game);
        ButterKnife.bind(this);
        securityAgent = new SecurityAgent(this);
        agent = new FirebaseAgent(this);

        database = FirebaseDatabase.getInstance();
        game_sessions = database.getReference().child(Constants.DATABASE_PATH_GAME_SESSIONS);

        securityAgent.getAuthStatus(new SecurityAgent.AuthStatusListener() {
            @Override
            public void isAuthenticated(Boolean status) {
                if (status){
                    Toast.makeText(groupGame.this, "signed in", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(groupGame.this, "signed out", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void fetchData(){
    }



}
