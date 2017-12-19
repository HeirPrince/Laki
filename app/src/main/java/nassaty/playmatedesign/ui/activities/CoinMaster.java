package nassaty.playmatedesign.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.adapters.CoinAdapter;
import nassaty.playmatedesign.ui.utils.GameServiceHelper;

public class CoinMaster extends AppCompatActivity {

    @BindView(R.id.coin_list)RecyclerView coinList;
    @BindView(R.id.toolbar)Toolbar toolbar;
    private CoinAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseUser active;
    private GameServiceHelper gameServiceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_master);
        ButterKnife.bind(this);
        int padding = getResources().getDimensionPixelOffset(R.dimen.toolbar_content_inset);
        toolbar.setContentInsetsAbsolute(padding, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Buy Coins");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);


        auth = FirebaseAuth.getInstance();
        active = auth.getCurrentUser();
        gameServiceHelper = new GameServiceHelper(this, active);

        showCoins();
    }

    public void showCoins(){
        LinearLayoutManager linear = new LinearLayoutManager(this);
        coinList.setLayoutManager(linear);
        coinList.setHasFixedSize(true);

        gameServiceHelper.getTokenAmountList(active.getPhoneNumber(), new GameServiceHelper.getTokenAmounts() {
            @Override
            public void items(List<Integer> items) {
                adapter = new CoinAdapter(CoinMaster.this, items, active.getPhoneNumber());
                coinList.setAdapter(adapter);
            }
        });
    }
}
