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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.helpers.SqliteHelper;
import nassaty.playmatedesign.ui.model.GameMetaData;
import nassaty.playmatedesign.ui.model.Player;
import nassaty.playmatedesign.ui.model.Token;

/**
 * Created by Prince on 11/20/2017.
 */

public class GameServiceHelper {

    private FirebaseDatabase database;
    private DatabaseReference game_session;
    private DatabaseReference users;
    private DatabaseReference replay;
    private DatabaseReference bets;
    private Context ctx;
    private FirebaseUser active;
    private SqliteHelper sqlite;


    public GameServiceHelper(Context context, FirebaseUser user) {
        this.ctx = context;
        this.database = FirebaseDatabase.getInstance();
        if (user != null &&user.getPhoneNumber() != null){
            this.game_session = database.getReference().child(Constants.DATABASE_PATH_GAME_SESSIONS);
            this.users = database.getReference().child(Constants.DATABASE_PATH_USERS);
            this.replay = users.child(Constants.DATABASE_PATH_REPLAY_REQUEST);
            this.active = user;
        }else {
            //warning
        }
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

    public interface addnewToken{
        void tokenCallBack(Boolean status);
    }

    public interface getTokens{
        void tokenList(List<Token> tokens);
    }

    public interface useToken {
        void isTokenUsed(Boolean status);
    }

    public interface TokenCount{
        void tokenNumber(int total);
    }

    public interface getTokenAmounts{
        void items(List<Integer> items);
    }

    public interface requestReplay{
        void isRequested(Boolean status);
    }

    public interface isBetFound{
        void isFound(Boolean status);
    }



    //methods

    public int getRandomPos(){
        return new Random().nextInt(16);
    }

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

    public void addGameToken(int number, int amount, final addnewToken addnewToken){

        for (int i = 1; i<= number; i++){
            Token token = new Token();
            token.setToken_id(returnRandomTokenId());
            token.setAmount(amount);
            users.child(active.getPhoneNumber()).child(Constants.DATABASE_PATH_COINS).child(String.valueOf(token.getAmount())).push().setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    addnewToken.tokenCallBack(task.isSuccessful());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    addnewToken.tokenCallBack(false);
                }
            });
        }
    }

    public void getTokenList(String phone, final getTokens getTokens){
        final List<Token> tokens = new ArrayList<>();
        tokens.clear();
        users.child(active.getPhoneNumber()).child(Constants.DATABASE_PATH_COINS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Token token = dataSnapshot.getValue(Token.class);
                tokens.add(token);
                getTokens.tokenList(tokens);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                getTokens.tokenList(tokens);
            }
        });
    }

    public void useToken(String id, final useToken callback){
        users.child(active.getPhoneNumber()).child(Constants.DATABASE_PATH_COINS).child(id).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.isTokenUsed(task.isSuccessful());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.isTokenUsed(false);
            }
        });
    }

    public void getTokenCount(String phone, int amount, final TokenCount count){
        users.child(phone).child(Constants.DATABASE_PATH_COINS).child(String.valueOf(amount)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    count.tokenNumber((int) dataSnapshot.getChildrenCount());
                }else {
                    count.tokenNumber(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                count.tokenNumber(0);
            }
        });
    }

    public void getTokenAmountList(String phone, final getTokenAmounts callback){
        final List<Integer> amounts = new ArrayList<>();
        users.child(phone).child(Constants.DATABASE_PATH_COINS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    amounts.add(Integer.valueOf(ds.getKey()));
                    callback.items(amounts);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public int returnRandomTokenId(){
        int rnd = new Random().nextInt(500);
        return rnd;
    }

    //sends a replay request to all numbers except the trigger
    public void requestReplayToAllExcept(String phone, List<String> phoneNumbers){
        for (final String p : phoneNumbers){
            if (!p.equals(phone)){
                sendReplayRequest(p, new requestReplay() {
                    @Override
                    public void isRequested(Boolean status) {
                        if (status){
                            Toast.makeText(ctx, "request sent to "+p, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ctx, "request not sent", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    //sends replay request to a single phone
    private void sendReplayRequest(String phone, final requestReplay callback){
        replay.child(phone).child(Constants.DATABASE_PATH_REPLAY_REQUEST).setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.isRequested(task.isSuccessful());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.isRequested(false);
            }
        });
    }

    public void checkBet(String phone, final isBetFound callback){
        bets = users.child(phone).child(Constants.DATABASE_PATH_ACCOUNT).child(Constants.DATABASE_PATH_BETS);
        bets.child("amt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null){
                    String result = dataSnapshot.getValue().toString();
                    if (Integer.valueOf(result) != 0){
                        callback.isFound(true);
                    }else {
                        callback.isFound(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.isFound(false);
            }
        });
    }



}
