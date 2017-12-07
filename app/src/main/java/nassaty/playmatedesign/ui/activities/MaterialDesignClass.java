package nassaty.playmatedesign.ui.activities;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;

public class MaterialDesignClass extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.holder)LinearLayout holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_design_class);
        ButterKnife.bind(this);
        holder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {


    }

    public void reveal(View view) {
        int[] clickCoords = new int[2];
        // Find the location of clickSource on the screen
        view.getLocationOnScreen(clickCoords);
// Tweak that location so that it points at the center of the view,
        // not the corner
        clickCoords[0] += view.getWidth() / 2;
        clickCoords[1] += view.getHeight() / 2;
        performRevealAnimation(holder, clickCoords[0], clickCoords[1]);
    }

    private void performRevealAnimation(View view, int screenCenterX, int screenCenterY) {
        // Find the center relative to the view that will be animated
        int[] animatingViewCoords = new int[2];
        view.getLocationOnScreen(animatingViewCoords);
        int centerX = screenCenterX - animatingViewCoords[0];
        int centerY = screenCenterY - animatingViewCoords[1];
// Find the maximum radius
        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        int maxRadius = size.y;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, maxRadius)
                    .start();
            view.setVisibility(View.VISIBLE);
        }
    }
}
