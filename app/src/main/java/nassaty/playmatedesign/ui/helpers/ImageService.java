package nassaty.playmatedesign.ui.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Base64;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Prince on 6/6/2017.
 */

public class ImageService {

    public static String pathfriend = UrlManager.getHost()+"roulette/users_picture/";
    public static String grouppath = UrlManager.getHost()+"roulette/group_picture/";

    public static String getImagePath(String name, String type) {

        String apath;
        if (type.contains("user")){
            apath = pathfriend+name;
            return apath;
        }else if(type.contains("group")){
            apath = grouppath+name;
            return apath;
        }else {
            return null;
        }
    }

    public static String convertImageToString(Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imgBytes = bos.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    public String returnRandomImageName(String extension) {
        String picname = String.valueOf(new Random().nextInt()) + extension;
        return picname;
    }

    public boolean checkCamera(Context ctx){
        if (ctx.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static void loadSimpleImage(Context ctx, CircleImageView img, String url, int width, int height){
        Picasso.with(ctx).load(url).resize(width, height).into(img);
    }

    public String getGrouppath() {
        return grouppath;
    }

    public String getPathfriend() {
        return pathfriend;
    }
}
