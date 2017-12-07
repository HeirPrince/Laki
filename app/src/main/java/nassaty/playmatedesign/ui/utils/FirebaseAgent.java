package nassaty.playmatedesign.ui.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import nassaty.playmatedesign.ui.activities.MainActivity;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.ActiveUser;
import nassaty.playmatedesign.ui.model.CreditCard;
import nassaty.playmatedesign.ui.model.Player;
import nassaty.playmatedesign.ui.model.User;

/**
 * Created by Prince on 7/25/2017.
 */

public class FirebaseAgent {

    //variables
    private String name;
    private String p;
    private String i;

    //storage
    private FirebaseDatabase database;
    private StorageReference drive;


    //Database
    private DatabaseReference games;
    private DatabaseReference users;
    private DatabaseReference online;
    private DatabaseReference credit_cards;
    private DatabaseReference friendship;
    private StorageReference storageReference;
    private DatabaseReference game_room;
    private Boolean isConnected;
    FirebaseAuth auth;

    friendAddedListener mFriendCallback;
    isMyFriend mFriendshipCallback;
    unFriendListener mUnFriendCallBack;

    private Context ctx;
    private User user;

    public FirebaseAgent(Context ctx) {
        this.ctx = ctx;
        database = FirebaseDatabase.getInstance();
        users = database.getReference().child(Constants.DATABASE_PATH_USERS);
        games = database.getReference().child(Constants.DATABASE_PATH_GAME_SESSIONS);
        credit_cards = database.getReference().child(Constants.DATABASE_PATH_CREDIT_CARDS);
        friendship = database.getReference().child(Constants.DATABASE_PATH_FRIENDS);
        game_room = database.getReference().child(Constants.DATABASE_PATH_GAME_SESSIONS);
        drive = FirebaseStorage.getInstance().getReference();
        online = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_PATH_ONLINE_PLAYERS);
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Constants.STORAGE_PATH_URL);
        user = new User();
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setupProfile(final Context ctx, final Uri uri, final String name, final String phone) {
        if (uri != null) {
            final ProgressDialog dialog = new ProgressDialog(ctx);
            dialog.setTitle("Uploading...");
            dialog.show();

            StorageReference profile = drive.child(Constants.STORAGE_PATH_USERS + name);

            profile.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if (taskSnapshot != null) {
                        User user = new User(name, phone, phone, name, uri.getPath());
                        users.child(phone).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                Toast.makeText(ctx, "Cool", Toast.LENGTH_SHORT).show();
                                ctx.startActivity(new Intent(ctx, MainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    dialog.setMessage("Please wait " + ((int) progress) + "%...");
                }
            });

        } else {
            Toast.makeText(ctx, "no file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnlineListener<T> {
        void isUserOnline(T isOnline);
    }

    public void isUserOnline(@NonNull final OnlineListener<Boolean> isOnline, final String phone) {
        online.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ActiveUser u = dataSnapshot.getValue(ActiveUser.class);
                if (u != null) {
                    isOnline.isUserOnline(true);
                } else {
                    isOnline.isUserOnline(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("online node error", databaseError.getMessage());
            }
        });
    }

    public void addFriend(final friendAddedListener listener, User u) {
        this.mFriendCallback = listener;
        User user = u;
        u.setPhone_number(u.getPhone_number());
        friendship.child(auth.getCurrentUser().getPhoneNumber()).child(u.getPhone_number()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.Succeded(true);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.Failed(e.getMessage());
            }
        });
    }

    public void  unFriend(final unFriendListener listener, String phone){
        this.mUnFriendCallBack = listener;
        friendship.child(auth.getCurrentUser().getPhoneNumber()).child(phone).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.Succeded(task.isSuccessful());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.Failed(e.getMessage());
            }
        });
    }


    public void getFriendshipStatus(final isMyFriend myFriend, String phone) {
        this.mFriendshipCallback = myFriend;
        friendship.child(auth.getCurrentUser().getPhoneNumber()).child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    myFriend.Yes(true);
                }else {
                    myFriend.Yes(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                myFriend.Yes(null);
            }
        });
    }

    public interface friendAddedListener {
        void Succeded(boolean state);

        void Failed(String error);
    }

    public interface unFriendListener {
        void Succeded(boolean state);
        void Failed(String error);
    }

    public interface isMyFriend {
        void Yes(Boolean status);

        void Failed(String msg);
    }

    public interface F2FListener<T> {
        void isOpponentOnline(T online);

        void isTriggerOnline(T online);

        void isAllOnline(T all);
    }

    public interface getInfo<T> {
        void isRegistered(T state);
    }

    public interface isCardAddedListener<T> {
        void isComplete(T status, String key);

        void isFailed(T status, String msg);
    }

    public void addCreditCard(CreditCard card, @NonNull final isCardAddedListener<Boolean> isCardAddedListener) {
        final String k = users.child(auth.getCurrentUser().getPhoneNumber()).child(Constants.DATABASE_PATH_CREDIT_CARDS).push().getKey();
        users.child(auth.getCurrentUser().getPhoneNumber()).child(Constants.DATABASE_PATH_CREDIT_CARDS).child(k).setValue(card)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            isCardAddedListener.isComplete(true, k);
                        } else {
                            isCardAddedListener.isComplete(false, null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isCardAddedListener.isFailed(true, e.getMessage());
            }
        });
    }

    public interface isCardRegistered<T> {
        void isRegistered(T status);

        void ReferenceKey(String key);
    }

    public void getCreditCard(final String phone, @NonNull final isCardRegistered<Boolean> isCardRegistered) {
        DatabaseReference rs = users.child(auth.getCurrentUser().getPhoneNumber()).child(Constants.DATABASE_PATH_CREDIT_CARDS);
        rs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                isCardRegistered.isRegistered(false);
            }
        });
    }

    public void isRegistered(@NonNull final getInfo<Boolean> isRegisteredListener, String phone) {
        users.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    if (user.getPhone_number() != null) {
                        isRegisteredListener.isRegistered(true);
                    } else {
                        isRegisteredListener.isRegistered(false);
                    }
                } else {
                    Toast.makeText(ctx, "user not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                isRegisteredListener.isRegistered(null);
                Toast.makeText(ctx, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void isF2FReady(@NonNull final F2FListener<Boolean> f2FListener, final String trigger, final String opponent) {
        online.child(trigger).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ActiveUser active = dataSnapshot.getValue(ActiveUser.class);
                if (active != null) {
                    f2FListener.isTriggerOnline(true);
                    online.child(opponent).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ActiveUser active = dataSnapshot.getValue(ActiveUser.class);
                            if (active != null) {
                                f2FListener.isOpponentOnline(true);
                                f2FListener.isAllOnline(true);
                            } else {
                                f2FListener.isOpponentOnline(false);
                                f2FListener.isAllOnline(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    f2FListener.isTriggerOnline(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface OnStatusListener<T> {
        void isComplete(T status, String url);

        void isFailed(T failed);
    }

    public void downloadImage(String name, String path, @NonNull final OnStatusListener<Boolean> onStatusListener) {
        storageReference.child(path).child(name).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    onStatusListener.isComplete(task.isComplete(), task.getResult().toString());
                    onStatusListener.isFailed(false);
                } else {
                    onStatusListener.isComplete(task.isComplete(), task.getResult().toString());
                    onStatusListener.isFailed(true);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onStatusListener.isFailed(true);
            }
        });
    }

    public interface addOnNameChangeListener<T> {
        void onNameChangedListener(String name, String image);
    }

    public interface getPlayerInfo{
        void isSuccessful(Boolean status, Player player);
    }

    public void getUserByPhone(String phone, final addOnNameChangeListener<String> onNameChangeListener) {
        users.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                onNameChangeListener.onNameChangedListener(user.getUsername(), user.getImage());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onNameChangeListener.onNameChangedListener(null, null);
            }
        });
    }

    public void getPlayerByPhone(String phone, String type, String game_type, final getPlayerInfo getPlayerInfo){
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null){
                    Player player = dataSnapshot.getValue(Player.class);
                    getPlayerInfo.isSuccessful(true, player);
                }else {
                    getPlayerInfo.isSuccessful(false, null);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null){
                    Player player = dataSnapshot.getValue(Player.class);
                    getPlayerInfo.isSuccessful(true, player);
                }else {
                    getPlayerInfo.isSuccessful(false, null);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null){
                    Player player = dataSnapshot.getValue(Player.class);
                    getPlayerInfo.isSuccessful(true, player);
                }else {
                    getPlayerInfo.isSuccessful(false, null);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null){
                    Player player = dataSnapshot.getValue(Player.class);
                    getPlayerInfo.isSuccessful(true, player);
                }else {
                    getPlayerInfo.isSuccessful(false, null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                getPlayerInfo.isSuccessful(false, null);
            }
        };

        game_room.child(auth.getCurrentUser().getPhoneNumber()).child(type).child(game_type).addChildEventListener(listener);
    }


}
