package toponymsystem.island.com.activity;


import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import toponymsystem.island.com.R;

@EActivity(R.layout.activity_about_island)
public class AboutUsActivity extends BaseActivity {
    @ViewById(R.id.title)
    TextView title;
    @AfterViews
    void afterView(){
        title.setText("关于我们");
    }
    @Click(R.id.back)
    void onClick(){
        finish();
    }
}
