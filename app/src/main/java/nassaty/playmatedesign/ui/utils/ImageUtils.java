package nassaty.playmatedesign.ui.utils;

import android.content.Context;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageUtils {

    private Context context;

    public ImageUtils(Context context) {
        this.context = context;
    }

    public void displayCircledImage(String image, CircleImageView view){
        Glide.with(context).load(image).crossFade().centerCrop().into(view);
    }

    public void showDefaultImage(CircleImageView view){
        //TODO load default image from drawable
    }

}
