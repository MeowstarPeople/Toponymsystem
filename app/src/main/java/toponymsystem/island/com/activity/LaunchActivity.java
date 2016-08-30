package toponymsystem.island.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import toponymsystem.island.com.R;


/**
 * Created by Administrator on 2015/8/26.
 */
@EActivity(R.layout.activity_launch)
public class LaunchActivity extends BaseActivity {
    @ViewById(R.id.launch_pic)
    ImageView mLaunchPic;

    @AfterViews
    void afterView() {
        Animation animation = new AlphaAnimation(0.1f, 1.0f);
        //如果存在图片，则显示相应的图片，没有则是默认的。
        animation.setDuration(3000);
        mLaunchPic.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                doPageJump();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void doPageJump() {
        finish();
        startActivity(new Intent(this, HomeActivity_.class));
    }
}
