package nassaty.playmatedesign.ui.activities;
//TODO to be removed
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Random;

import butterknife.ButterKnife;
import nassaty.playmatedesign.R;

public class Roulette extends AppCompatActivity {


    //get position list
    //track winner
    //get winner's phone number

//    @BindView(R.id.wheel)ImageView wheel;
//    @BindView(R.id.true_position)TextView tp;
//    @BindView(R.id.rand_rotation)TextView rr;
//    @BindView(R.id.rand_position)TextView rp;

    Random r;
    int degrees = 0, degrees_old = 0;
    private static final float FACTOR = 4.84f;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_active_list);
        ButterKnife.bind(this);

//        r = new Random();
//
//        wheel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                degrees_old = degrees % 360;
//                degrees = r.nextInt(3400) + 720;
//                RotateAnimation rotate = new RotateAnimation(degrees_old, degrees, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//                rotate.setDuration(1000);
//                rotate.setFillAfter(true);
//                rotate.setInterpolator(new DecelerateInterpolator());
//                rotate.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        rp.setText(String.valueOf(currentPosition(360 - (degrees % 360))));
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//                wheel.startAnimation(rotate);
//            }
//        });

    }


    public int currentPosition(int degrees){
        String text = "";
        int pos = 0;

        if (degrees >= (FACTOR * 1) && degrees < (FACTOR * 3)){
            text = "32 red";
            pos = 32;
        }
        if (degrees >= (FACTOR * 3) && degrees < (FACTOR * 5)){
            text = "15 black";
            pos = 15;
        }
        if (degrees >= (FACTOR * 5) && degrees < (FACTOR * 7)){
            text = "19 red";
            pos = 19;
        }
        if (degrees >= (FACTOR * 7) && degrees < (FACTOR * 9)){
            text = "4 black";
            pos = 4;
        }
        if (degrees >= (FACTOR * 9) && degrees < (FACTOR * 11)){
            text = "21 red";
            pos = 21;
        }
        if (degrees >= (FACTOR * 11) && degrees < (FACTOR * 13)){
            text = "2 black";
            pos = 2;
        }
        if (degrees >= (FACTOR * 13) && degrees < (FACTOR * 15)){
            text = "25 red";
            pos = 25;
        }
        if (degrees >= (FACTOR * 15) && degrees < (FACTOR * 17)){
            text = "17 black";
            pos = 17;
        }
        if (degrees >= (FACTOR * 17) && degrees < (FACTOR * 19)){
            text = "34 red";
            pos = 34;
        }
        if (degrees >= (FACTOR * 19) && degrees < (FACTOR * 21)){
            text = "6 black";
            pos = 6;
        }
        if (degrees >= (FACTOR * 21) && degrees < (FACTOR * 23)){
            text = "27 red";
            pos = 27;
        }
        if (degrees >= (FACTOR * 23) && degrees < (FACTOR * 25)){
            text = "13 black";
            pos = 13;
        }
        if (degrees >= (FACTOR * 25) && degrees < (FACTOR * 27)){
            text = "36 red";
            pos = 36;
        }
        if (degrees >= (FACTOR * 27) && degrees < (FACTOR * 29)){
            text = "11 black";
            pos = 11;
        }
        if (degrees >= (FACTOR * 29) && degrees < (FACTOR * 31)){
            text = "30 red";
            pos = 30;
        }
        if (degrees >= (FACTOR * 31) && degrees < (FACTOR * 33)){
            text = "8 black";
            pos = 8;
        }
        if (degrees >= (FACTOR * 33) && degrees < (FACTOR * 35)){
            text = "23 red";
            pos = 23;
        }
        if (degrees >= (FACTOR * 35) && degrees < (FACTOR * 37)){
            text = "10 black";
            pos = 35;
        }
        if (degrees >= (FACTOR * 37) && degrees < (FACTOR * 39)){
            text = "5 red";
            pos = 5;
        }
        if (degrees >= (FACTOR * 39) && degrees < (FACTOR * 41)){
            text = "24 black";
            pos = 24;
        }
        if (degrees >= (FACTOR * 41) && degrees < (FACTOR * 43)){
            text = "16 red";
            pos = 16;
        }
        if (degrees >= (FACTOR * 43) && degrees < (FACTOR * 45)){
            text = "33 black";
            pos = 33;
        }
        if (degrees >= (FACTOR * 45) && degrees < (FACTOR * 47)){
            text = "1 red";
            pos = 1;
        }
        if (degrees >= (FACTOR * 47) && degrees < (FACTOR * 49)){
            text = "20 black";
            pos = 20;
        }
        if (degrees >= (FACTOR * 49) && degrees < (FACTOR * 51)){
            text = "14 red";
            pos = 14;
        }
        if (degrees >= (FACTOR * 51) && degrees < (FACTOR * 53)){
            text = "31 black";
            pos = 31;
        }
        if (degrees >= (FACTOR * 53) && degrees < (FACTOR * 55)){
            text = "9 red";
            pos = 9;
        }
        if (degrees >= (FACTOR * 55) && degrees < (FACTOR * 57)){
            text = "22 black";
            pos = 22;
        }
        if (degrees >= (FACTOR * 57) && degrees < (FACTOR * 59)){
            text = "16 red";
            pos = 16;
        }
        if (degrees >= (FACTOR * 59) && degrees < (FACTOR * 61)){
            text = "29 black";
            pos = 29;
        }
        if (degrees >= (FACTOR * 61) && degrees < (FACTOR * 63)){
            text = "7 red";
            pos = 7;
        }
        if (degrees >= (FACTOR * 63) && degrees < (FACTOR * 65)){
            text = "28 black";
            pos = 28;
        }
        if (degrees >= (FACTOR * 65) && degrees < (FACTOR * 67)){
            text = "12 red";
            pos = 12;
        }
        if (degrees >= (FACTOR * 67) && degrees < (FACTOR * 69)){
            text = "35 black";
            pos = 35;
        }
        if (degrees >= (FACTOR * 69) && degrees < (FACTOR * 71)){
            text = "3 red";
            pos = 3;
        }
        if (degrees >= (FACTOR * 71) && degrees < (FACTOR * 73)){
            text = "26 black";
            pos = 26;
        }
        if (degrees >= (FACTOR * 73) && degrees < 360 || degrees >= 0 && degrees < (FACTOR * 1)){
            text = "0 green";
            pos = 0;
        }

        return pos;
    }

}
