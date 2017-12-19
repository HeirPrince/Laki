package nassaty.playmatedesign.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.utils.GameServiceHelper;

/**
 * Created by Prince on 12/18/2017.
 */

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.Viewholder> {

    private Context context;
    private List<Integer> amounts;
    private String activePhone;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseUser activeUser;
    private DatabaseReference coins;
    private GameServiceHelper gameServiceHelper;

    public CoinAdapter(Context context, List<Integer> amounts, String activePhone) {
        this.context = context;
        this.amounts = amounts;
        this.activePhone = activePhone;
        this.database = FirebaseDatabase.getInstance();
        this.auth = FirebaseAuth.getInstance();
        this.activeUser = auth.getCurrentUser();
        gameServiceHelper = new GameServiceHelper(context, activeUser);
        this.coins = database.getReference().child(Constants.DATABASE_PATH_USERS).child(activePhone).child(Constants.DATABASE_PATH_COINS);
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_coin_amount_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        int amount = amounts.get(position);
        holder.distributeAmounts(amount);
    }

    @Override
    public int getItemCount() {
        return amounts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Button amount;
        public TextView number;

        public Viewholder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            number = itemView.findViewById(R.id.number);
        }

        public void distributeAmounts(int amt) {
            amount.setText(String.valueOf(amt)+" RWF");
            gameServiceHelper.getTokenCount(activeUser.getPhoneNumber(), amt, new GameServiceHelper.TokenCount() {
                @Override
                public void tokenNumber(int total) {
                    number.setText(String.valueOf(total)+" Coins");
                }
            });
            amount.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            boolean wrapInScrollView = true;
            new MaterialDialog.Builder(context)
                    .title("Use coins")
                    .customView(R.layout.dialog_pay_token, wrapInScrollView)
                    .positiveText("DONE")
                    .show();
        }

    }

}
