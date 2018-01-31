package nassaty.playmatedesign.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.activities.CoinMaster;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.Notif;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;

/**
 * Created by Prince on 11/1/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Notif> notifList;
    private FirebaseAgent agent;
    private Context context;
    private final int F2F = 0, GROUP = 1;

    public NotificationAdapter(Context context, List<Notif> notifList) {
        this.notifList = notifList;
        this.context = context;
        this.agent = new FirebaseAgent(context);
    }

    //***Separates data***
//    @Override
//    public int getItemViewType(int position) {
//        if (notifList != null) {
//            if (notifList.get(position).getType().equals(Constants.FRIEND_TYPE)) {
//                return F2F;
//            } else if (notifList.get(position).getType().equals(Constants.GROUP_TYPE)) {
//                return GROUP;
//            }
//        } else {
//            return 0;
//        }
//        return -1;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View f = inflater.inflate(R.layout.layout_active_list, parent, false);
        viewHolder = new NotificationHolder(f, context);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Notif notif = notifList.get(position);


        final NotificationHolder notificationHolder = (NotificationHolder) holder;
        agent.isUserOnline(new FirebaseAgent.OnlineListener<Boolean>() {
            @Override
            public void isUserOnline(Boolean isOnline) {
                if (isOnline) {
                    agent.getUserByPhone(notif.getSender(), new FirebaseAgent.addOnNameChangeListener<String>() {
                        @Override
                        public void onNameChangedListener(String name, String image) {
                            notificationHolder.feedViews(name, notif.getSender(), image, true);
                        }
                    });
                } else {
                    agent.getUserByPhone(notif.getSender(), new FirebaseAgent.addOnNameChangeListener<String>() {
                        @Override
                        public void onNameChangedListener(String name, String image) {
                            notificationHolder.feedViews(name, notif.getSender(), image, false);
                        }
                    });
                }
            }
        }, notif.getSender());

    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView phone;
        public TextView name;
        public CircleImageView profile;
        public View online;
        public View offline;
        public Context context;

        public NotificationHolder(View itemView, Context ctx) {
            super(itemView);
            phone = itemView.findViewById(R.id.sphone);
            name = itemView.findViewById(R.id.sname);
            profile = itemView.findViewById(R.id.sprofile);
            online = itemView.findViewById(R.id.online);
            offline = itemView.findViewById(R.id.offline);
            this.context = ctx;
            itemView.setOnClickListener(this);
        }

        public void feedViews(String nm, String pn, String imageUrl, Boolean isOnline) {
            name.setText(nm);
            phone.setText(pn);
            decideView(isOnline);
            agent.downloadImage(imageUrl, Constants.STORAGE_PATH_USERS, new FirebaseAgent.OnStatusListener<Boolean>() {
                @Override
                public void isComplete(Boolean status, String url) {
                    Glide.with(context)
                            .load(url)
                            .crossFade()
                            .centerCrop()
                            .into(profile);
                }

                @Override
                public void isFailed(Boolean failed) {
                    //load default image
                }
            });

        }

        public void decideView(Boolean isOnline) {
            if (isOnline) {
                online.setVisibility(View.VISIBLE);
                offline.setVisibility(View.GONE);
            } else {
                online.setVisibility(View.GONE);
                offline.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Notif notification = notifList.get(position);
            Intent intent = new Intent(context, CoinMaster.class);
            intent.putExtra("sender", notification.getSender());
            intent.putExtra("receiver", notification.getReceiver());
            intent.putExtra("sender_position", notification.getSender_position());
            intent.putExtra("type", notification.getType());
            context.startActivity(intent);
        }
    }
}
