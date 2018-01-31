package nassaty.playmatedesign.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.Participant;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;

/**
 * Created by Prince on 11/25/2017.
 */

public class HeadAdapter extends RecyclerView.Adapter<HeadAdapter.HeadHolder> {

    private Context ctx;
    private FirebaseAgent agent;
    private List<Participant> participantList;

    public HeadAdapter(Context ctx, List<Participant> participantList) {
        this.ctx = ctx;
        this.agent = new FirebaseAgent(ctx);
        this.participantList = participantList;
    }

    @Override
    public HeadHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_group_participant, parent, false);
        return new HeadHolder(view);
    }

    @Override
    public void onBindViewHolder(HeadHolder holder, int position) {
        Participant participant = participantList.get(position);
        holder.setData(participant.getImage());
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }


    class HeadHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CircleImageView avatar;

        public HeadHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            itemView.setOnClickListener(this);
        }

        public void setData(String url){
            agent.downloadImage(url, Constants.STORAGE_PATH_USERS, new FirebaseAgent.OnStatusListener<Boolean>() {
                @Override
                public void isComplete(Boolean status, String url) {
                    Glide.with(ctx).load(url).into(avatar);
                }

                @Override
                public void isFailed(Boolean failed) {
                    return;
                }
            });
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            //TODO REMOVE USER
        }
    }

}
