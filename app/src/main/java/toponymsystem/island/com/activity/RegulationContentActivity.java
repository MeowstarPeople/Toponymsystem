package toponymsystem.island.com.activity;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import toponymsystem.island.com.R;
import toponymsystem.island.com.iView.IRegulationContentView;
import toponymsystem.island.com.model.RegulationDetailsData;
import toponymsystem.island.com.presenter.RegulationContentPresenter;
import toponymsystem.island.com.rest.api.URL;

/**
 * Created by CuiXiaoYong
 * on 2016/4/21
 * E-Mail:hellocui@aliyun.com
 */
@EActivity(R.layout.activity_regulation_detials)
public class RegulationContentActivity extends BaseActivity implements IRegulationContentView {

    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.regulation_title)
    TextView regulationTitle;
    @ViewById(R.id.regulation_time)
    TextView regulationTime;
    @ViewById(R.id.regulation_content)
    TextView regulationContent;
    private String contentUrl;
    private RegulationContentPresenter presenter;

    @Click(R.id.back)
    void onClick() {
        finish();
    }

    @AfterViews
    void afterView() {
        title.setText("政策法规详情");
        contentUrl = getIntent().getStringExtra("regulationContentUrl");
        presenter = new RegulationContentPresenter(this, appRequester);
        presenter.loadData(URL.ISLAND_HOST + contentUrl);
    }

    @Override
    public void updateRegulationContentData(final RegulationDetailsData data) {
        runOnUiThread(new Runnable() {
            public void run() {
                updateUi(data);
            }
        });
    }

    void updateUi(RegulationDetailsData data) {
        regulationTitle.setText(data.getTitle());//标题
        regulationTime.setText(data.getDate());//时间
        regulationContent.setText(data.getContent());//内容
    }

    @Override
    public void showLoadingDialog() {
        showLoading();
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoading();
    }
}
