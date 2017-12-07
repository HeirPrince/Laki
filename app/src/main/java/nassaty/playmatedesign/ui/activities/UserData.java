package nassaty.playmatedesign.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;

public class UserData extends AppCompatActivity {

    @BindView(R.id.profile_pic)CircleImageView profile;
    @BindView(R.id.profile_name)TextInputEditText pname;
    @BindView(R.id.image_crop)View imageCrop;
    private Uri filePath;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        imageCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserData.this, "cropping", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void chooseImage(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select Image"), Constants.GALLERY_CODE);
    }

    public void begin(View view) {
        FirebaseAgent agent = new FirebaseAgent(this);
        agent.setupProfile(this, filePath, pname.getText().toString(), user.getPhoneNumber());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == Constants.GALLERY_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
//            filePath = data.getData();
////            CropImage.activity(filePath).start(this);
//        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK){
//                Uri cropped = result.getUri();
//                try {
//                    Bitmap bmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cropped);
//                    profile.setImageBitmap(bmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

}
