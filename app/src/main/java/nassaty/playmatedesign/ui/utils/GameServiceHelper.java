package nassaty.playmatedesign.ui.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.GameMetaData;
import nassaty.playmatedesign.ui.model.Player;

/**
 * Created by Prince on 11/20/2017.
 */

public class GameServiceHelper {

    createSession mSessionCallBack;
    private FirebaseDatabase database;
    private DatabaseReference game_session;
    private Context ctx;
    private FirebaseUser active;


    public GameServiceHelper(Context context, FirebaseUser user) {
        this.ctx = context;
        this.database = FirebaseDatabase.getInstance();
        this.game_session = database.getReference().child(Constants.DATABASE_PATH_GAME_SESSIONS);
        this.active = user;
    }

    //interfaces
    public interface createSession {
        void isSessionCreated(Boolean status);
    }

    public interface attach {
        void isAttached(Boolean status);
    }

    public interface searchSession {
        void isFound(Boolean status);
    }

    public interface createGroupSession {
        void isCreated(Boolean status);
    }

    //methods
    public void setGameSession(Player player, final createSession session) {
        game_session.child(active.getPhoneNumber()).child(Constants.FRIEND_TYPE).push().child("trigger").setValue(player).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    session.isSessionCreated(true);
                } else {
                    session.isSessionCreated(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                session.isSessionCreated(null);
            }
        });
    }

    public void attachToSession(final Player player, final attach attachListener) {
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        game_session.child(active.getPhoneNumber()).child(Constants.FRIEND_TYPE).addChildEventListener(listener);
    }

    public void findSessionById(String id, final searchSession searchSession) {
        game_session.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null && dataSnapshot.getValue() != null) {
                    searchSession.isFound(true);
                } else {
                    searchSession.isFound(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                searchSession.isFound(null);
            }
        });
    }

    public void attachGameMetaData(String id, GameMetaData metaData) {
        game_session.child(id).child("Meta_data").setValue(metaData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ctx, "meta data added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ctx, "meta_data failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setGroupSession(Player trigger, final createGroupSession createGroupSession) {
        game_session.child(active.getPhoneNumber()).child(Constants.GROUP_TYPE).push().child(trigger.getPhone()).child("trigger").setValue(trigger)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            createGroupSession.isCreated(true);
                        } else {
                            createGroupSession.isCreated(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void attachToGroupSession(final List<Player> participants, final attach attach) {
        game_session.child(active.getPhoneNumber()).child(Constants.GROUP_TYPE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    DatabaseReference reference = ds.getRef();
                    for (Player player : participants){
                        reference.child("participants").setValue(player);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
