package toponymsystem.island.com.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import toponymsystem.island.com.R;
import toponymsystem.island.com.adapter.MoreRegulationsAdapter;
import toponymsystem.island.com.iView.IMoreRegulationView;
import toponymsystem.island.com.model.RegulationData;
import toponymsystem.island.com.presenter.MoreRegulationsPresenter;
import toponymsystem.island.com.rest.api.URL;

/**
 * Created by CuiXiaoYong
 * on 2016/4/12
 * E-Mail:hellocui@aliyun.com
 */
@EActivity(R.layout.activity_more_regulations)
public class MoreRegulationsActivity extends BaseActivity implements IMoreRegulationView, AdapterView.OnItemClickListener {
    @ViewById(R.id.data)
    ListView data;
    @ViewById(R.id.title)
    TextView title;
    private String moreLink;
    private MoreRegulationsPresenter presenter;
    List<RegulationData> dataList;

    @Click(R.id.back)
    void onClick() {
        finish();
    }

    @AfterViews
    void afterView() {
        Intent intent = getIntent();
        title.setText(intent.getStringExtra("regulationTitle"));
        moreLink = intent.getStringExtra("regulationMoreUrl");
        presenter = new MoreRegulationsPresenter(this, appRequester);
        presenter.loadData(URL.ISLAND_HOST + moreLink);
    }

    @Override
    public void updateMoreRegulationsData(List<RegulationData> data) {
        dataList = data;
        upDateView(data);
    }

    @UiThread
    void upDateView(List<RegulationData> dataList) {
        MoreRegulationsAdapter adapter = new MoreRegulationsAdapter(this, dataList);
        data.setAdapter(adapter);
        data.setOnItemClickListener(this);
    }

    @Override
    public void showLoadingDialog() {
        showLoading();
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoading();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        contentUrl = getIntent().getStringExtra("regulationContentUrl");
        Intent intent = new Intent(this, RegulationContentActivity_.class);
        intent.putExtra("regulationContentUrl", dataList.get(position).getRegulationLink());
        startActivity(intent);
    }
}
