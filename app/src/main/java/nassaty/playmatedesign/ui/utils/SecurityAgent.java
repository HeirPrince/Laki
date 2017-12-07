package nassaty.playmatedesign.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nassaty.playmatedesign.ui.activities.UserData;

/**
 * Created by Prince on 12/1/2017.
 */

public class SecurityAgent {

    private Context context;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    public SecurityAgent(Context context) {
        this.context = context;
    }

    public void getAuthStatus(final AuthStatusListener authStatusListener){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null){
                    authStatusListener.isAuthenticated(false);
                }else {
                    authStatusListener.isAuthenticated(true);
                }
            }
        };
    }

    public void setupProfile(){
        context.startActivity(new Intent(context, UserData.class));
    }

    public interface AuthStatusListener{
        void isAuthenticated(Boolean status);
    }

}
