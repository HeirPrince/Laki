package nassaty.playmatedesign.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.GroupNotif;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;

/**
 * Created by Prince on 1/30/2018.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private FirebaseAgent agent;
    private Context context;
    private List<GroupNotif> notifList;

    public GroupAdapter(Context context, List<GroupNotif> notifList) {
        this.agent = agent;
        this.context = context;
        this.notifList = notifList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_group_item2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GroupNotif notif = notifList.get(position);
        holder.setData(notif.getImage(), notif.getParticipants());
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private CircleImageView profile;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.group_participants);
            profile = itemView.findViewById(R.id.group_image);
        }

        public void setData(String url, List<String> participants) {
            agent.downloadImage(url, Constants.STORAGE_PATH_USERS, new FirebaseAgent.OnStatusListener<Boolean>() {
                @Override
                public void isComplete(Boolean status, String url) {
                    if (status) {
                        Glide.with(context).load(url).crossFade().centerCrop().into(profile);
                    }
                }

                @Override
                public void isFailed(Boolean failed) {

                }
            });

            for (String phone : participants){
                name.append(phone+",");
            }

        }
    }

}
