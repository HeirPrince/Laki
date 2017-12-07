package nassaty.playmatedesign.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.Participant;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;

/**
 * Created by Prince on 11/30/2017.
 */

public class PartipantAdapter extends RecyclerView.Adapter<PartipantAdapter.Viewholder>{

    private Context ctx;
    private List<Participant> participants;
    private FirebaseAgent agent;

    public PartipantAdapter(Context ctx, List<Participant> participants) {
        this.ctx = ctx;
        this.participants = participants;
        this.agent = new FirebaseAgent(ctx);
    }

    @Override
    public PartipantAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_participant, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        Participant participant = participants.get(position);
        holder.setdata(participant.getPosition(), participant.getImage());
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView position;
        public CircleImageView image;

        public Viewholder(View itemView) {
            super(itemView);
            position = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        public void setdata(int n, String url){
            position.setText(String.valueOf(n));
            agent.downloadImage(url, Constants.STORAGE_PATH_USERS, new FirebaseAgent.OnStatusListener<Boolean>() {
                @Override
                public void isComplete(Boolean status, String url) {
                    Glide.with(ctx).load(url).crossFade().centerCrop().into(image);
                }

                @Override
                public void isFailed(Boolean failed) {

                }
            });

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(ctx, "biggie biggie can't u see sometimes your word is hypnotizing", Toast.LENGTH_SHORT).show();
        }
    }

}
