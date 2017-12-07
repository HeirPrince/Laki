package nassaty.playmatedesign.ui.helpers;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import nassaty.playmatedesign.ui.model.User;

/**
 * Created by Prince on 7/27/2017.
 */

public class FirebaseHelper {

    DatabaseReference db;
    Boolean saved = null;
    ArrayList<String> mUserList;

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    public Boolean write(User user){
        if (user == null){
            saved = false;
        }else {
            try {
                db.child("users").push().setValue(user);
            }catch (DatabaseException e){
                e.printStackTrace();
                saved = false;
            }
        }
        return saved;
    }

    public void read(DataSnapshot dataSnapshot){
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            ds.getValue(User.class).getUsername();
            String name = "hello";
            mUserList.add(name);
        }
    }

    public ArrayList<String> getUsers(){
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                read(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                read(dataSnapshot);
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
        });

        return mUserList;
    }


}
