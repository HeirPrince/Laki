package nassaty.playmatedesign.ui.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import nassaty.playmatedesign.ui.helpers.Constants;

/**
 * Created by Prince on 1/29/2018.
 */

public class MysqlHelper {

    private Context ctx;
    private RequestQueue requestQueue;

    public MysqlHelper(Context ctx) {
        this.ctx = ctx;
        this.requestQueue = Volley.newRequestQueue(ctx);
    }


    //interfaces
    public interface SqlResults {
        void results(String SQLResults);
    }


    //methods
    public void reserveAmt(final String amount, @NonNull final SqlResults callback){
        String reserve_url = "m/tiya";
        String url = Constants.MYSQL_BASE_URL+ reserve_url;
        StringRequest str = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callback.results(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.results(volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("user_reservation", "user_reservation");
                map.put("amount", amount);

                return map;
            }
        };

        requestQueue.add(str);
    }

    public void checkBet(final String amount, final SqlResults sqlResults){
        String bet_url = "m/tiya";
        String url = Constants.MYSQL_BASE_URL+ bet_url;
        StringRequest str = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //results
                sqlResults.results(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                sqlResults.results(volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_tariff","user_tariff");
                params.put("amount", amount);

                return params;
            }
        };

        requestQueue.add(str);
    }

    public void checkCGame(final String phone, final SqlResults sqlResults){//param phone: opponent
        String Cgame_url = "m/createG";
        String url = Constants.MYSQL_BASE_URL+ Cgame_url;
        StringRequest str = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //results
                sqlResults.results(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                sqlResults.results(volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("type","couple_money");
                params.put("oppPhone", phone);

                return params;
            }
        };

        requestQueue.add(str);
    }
}

