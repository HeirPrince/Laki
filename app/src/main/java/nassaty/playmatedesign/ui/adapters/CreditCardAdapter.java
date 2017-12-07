package nassaty.playmatedesign.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.creditcarddesign.CreditCardView;

import java.util.List;

import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.model.CreditCard;

/**
 * Created by Prince on 11/3/2017.
 */

public class CreditCardAdapter extends RecyclerView.Adapter<CreditCardAdapter.CreditCardHolder> {

    private List<CreditCard> creditCards;
    private Context ctx;

    public CreditCardAdapter(List<CreditCard> creditCards, Context ctx) {
        this.creditCards = creditCards;
        this.ctx = ctx;
    }

    @Override
    public CreditCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_normal_ccard, parent, false);
        return new CreditCardHolder(v);
    }

    @Override
    public void onBindViewHolder(CreditCardHolder holder, int position) {
        CreditCard creditCard = creditCards.get(position);
        holder.setData(creditCard);
    }

    @Override
    public int getItemCount() {
        return creditCards.size();
    }

    static class CreditCardHolder extends RecyclerView.ViewHolder {

        public CreditCardView creditCardView;


        public CreditCardHolder(View itemView) {
            super(itemView);
            creditCardView = itemView.findViewById(R.id.ncard);
        }

        public void setData(CreditCard card){
            creditCardView.setCardExpiry(card.getExpiry());
            creditCardView.setCVV(card.getCvv());
            creditCardView.setCardNumber(card.getHolderNumber());
        }
    }
}
