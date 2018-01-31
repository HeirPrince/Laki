package nassaty.playmatedesign.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.activities.Payment;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.Participant;
import nassaty.playmatedesign.ui.model.User;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;

/**
 * Created by Prince on 11/14/2017.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.DataHolder> {

    private List<User> userList;
    private Context ctx;
    private FirebaseAgent agent;
    private int TYPE;
    private DatabaseReference friendship;
    private FirebaseAuth auth;
    private FirebaseUser active;

    public FriendAdapter(List<User> userList, Context ctx, int TYPE) {
        this.userList = userList;
        this.ctx = ctx;
        this.agent = new FirebaseAgent(ctx);
        this.TYPE = TYPE;
        this.auth = FirebaseAuth.getInstance();
        this.active = auth.getCurrentUser();
        friendship = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_FRIENDS);
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (TYPE){
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend2, parent, false);
                return new DataHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend, parent, false);
                return new DataHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_friend, parent, false);
                return new DataHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final DataHolder holder, int position) {
        final User user = userList.get(position);
        switch (TYPE){
            case 1:
                holder.addFriendData(user.getUsername(), user.getPhone_number(), user.getImage(), true);
                break;
            case 2:
                agent.isUserOnline(new FirebaseAgent.OnlineListener<Boolean>() {
                    @Override
                    public void isUserOnline(Boolean isOnline) {
                        if (isOnline) {
                            holder.chooseFriendData(user.getUsername(), user.getPhone_number(), user.getImage(), true);
                        } else {
                            holder.chooseFriendData(user.getUsername(), user.getPhone_number(), user.getImage(), false);
                        }
                    }
                }, user.getPhone_number());
                break;
            case 3:
                agent.isUserOnline(new FirebaseAgent.OnlineListener<Boolean>() {
                    @Override
                    public void isUserOnline(Boolean isOnline) {
                        if (isOnline) {
                            holder.chooseFriendData(user.getUsername(), user.getPhone_number(), user.getImage(), true);
                        } else {
                            holder.chooseFriendData(user.getUsername(), user.getPhone_number(), user.getImage(), false);
                        }
                    }
                }, user.getPhone_number());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView sname, sphone, sbet;
        public CircleImageView sprofile;
        public View online, offline;
        public Button friend, not_friend;
        public Context context;

        public DataHolder(View itemView) {
            super(itemView);

            sname = itemView.findViewById(R.id.sname);
            sphone = itemView.findViewById(R.id.sphone);
            sprofile = itemView.findViewById(R.id.sprofile);
            online = itemView.findViewById(R.id.online);
            offline = itemView.findViewById(R.id.offline);
            friend = itemView.findViewById(R.id.friend_true);
            not_friend = itemView.findViewById(R.id.friend_false);
            sbet = itemView.findViewById(R.id.sbet);
            this.context = ctx;
            itemView.setOnClickListener(this);
        }

        public void chooseFriendData(String name, String phone, String image, Boolean isOnline) {
            sname.setText(name);
            sphone.setText(phone);

            agent.getBetAmt(phone, new FirebaseAgent.getCurrentBet() {
                @Override
                public void currentBet(int amt) {
                    sbet.setText(String.valueOf(amt));
                }
            });

            agent.downloadImage(image, Constants.STORAGE_PATH_USERS, new FirebaseAgent.OnStatusListener<Boolean>() {
                @Override
                public void isComplete(Boolean status, String url) {
                    if (status) {
                        Glide.with(context)
                                .load(url)
                                .centerCrop()
                                .crossFade()
                                .into(sprofile);
                    } else {
                        //default image
                        sprofile.setImageDrawable(ctx.getDrawable(R.drawable.user));
                    }
                }

                @Override
                public void isFailed(Boolean failed) {
                    if (failed) {
                        Toast.makeText(ctx, "failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            changeOnlineState(isOnline);

        }

        public void addFriendData(String name, String phone, String image, Boolean isFriends){
            sname.setText(name);
            sphone.setText(phone);
            agent.downloadImage(image, Constants.STORAGE_PATH_USERS, new FirebaseAgent.OnStatusListener<Boolean>() {
                @Override
                public void isComplete(Boolean status, String url) {
                    if (status) {
                        Glide.with(context)
                                .load(url)
                                .centerCrop()
                                .crossFade()
                                .into(sprofile);
                    } else {
                        //default image
                        sprofile.setImageDrawable(ctx.getDrawable(R.drawable.user));
                    }
                }

                @Override
                public void isFailed(Boolean failed) {
                    if (failed) {
                        Toast.makeText(ctx, "failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            checkFriendship(phone);

            int position = getAdapterPosition();
            final User user = userList.get(position);

            friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    agent.unFriend(new FirebaseAgent.unFriendListener() {
                        @Override
                        public void Succeded(boolean state) {
                            if (state){
                                not_friend.setVisibility(View.VISIBLE);
                                friend.setVisibility(View.GONE);
                            }else {
                                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void Failed(String error) {

                        }
                    }, user.getPhone_number());
                }
            });

            not_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    agent.addFriend(new FirebaseAgent.friendAddedListener() {
                        @Override
                        public void Succeded(boolean key) {
                            Toast.makeText(context, "Wama success"+key, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void Failed(String error) {
                            Toast.makeText(context, "Wama failed man", Toast.LENGTH_SHORT).show();
                        }
                    }, user);
                }
            });

        }

        public void changeOnlineState(Boolean isOnline) {
            if (isOnline) {
                online.setVisibility(View.VISIBLE);
                offline.setVisibility(View.GONE);
            } else {
                online.setVisibility(View.GONE);
                offline.setVisibility(View.VISIBLE);
            }
        }

        public void checkFriendship(final String phone){
            agent.getFriendshipStatus(new FirebaseAgent.isMyFriend() {
                @Override
                public void Yes(Boolean status) {
                    if (status){
                        not_friend.setVisibility(View.GONE);
                        friend.setVisibility(View.VISIBLE);
                    }else {
                        not_friend.setVisibility(View.VISIBLE);
                        friend.setVisibility(View.GONE);
                    }
                }

                @Override
                public void Failed(String msg) {

                }
            }, phone);
        }

        @Override
        public void onClick(View view) {
            //init interface
            int position = getAdapterPosition();
            final User user = userList.get(position);

            switch (TYPE){
                case 1:
                    Toast.makeText(context, "really", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    agent.getBetAmt(active.getPhoneNumber(), new FirebaseAgent.getCurrentBet() {
                        @Override
                        public void currentBet(int amt) {
                            agent.checkBetEquality(user.getPhone_number(), amt, new FirebaseAgent.betAmountVerifier() {
                                @Override
                                public void verificationResult(Boolean allowed, String message) {
                                    if (allowed){
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ctx, Payment.class);
                                        intent.putExtra("opponent", user.getPhone_number());
                                        context.startActivity(intent);
                                    }else {
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });

                    break;
                case 3:
                    Participant participant = new Participant();
                    participant.setPhone(user.getPhone_number());
                    participant.setImage(user.getImage());
                    EventBus.getDefault().post(participant);
                    break;
            }
        }
    }
}
