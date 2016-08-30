package toponymsystem.island.com.activity;

import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import toponymsystem.island.com.R;
import toponymsystem.island.com.iView.IBarChartStatisticsView;
import toponymsystem.island.com.model.BarChartItemData;
import toponymsystem.island.com.presenter.BarChartStatisticsPresenter;
import toponymsystem.island.com.utils.StringUtil;
import toponymsystem.island.com.widget.BarChartView;

@EActivity(R.layout.activity_barchart_statistics)
public class BarChartStatisticsActivity extends BaseActivity implements IBarChartStatisticsView {
    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.bar_chart)
    BarChartView barChartView;
    private BarChartStatisticsPresenter presenter;
    private int transportationCount;
    private int publicService;
    private int industry;
    private int tourism;
    private int fisheries;
    private String province = "";
    private String city = "";

    @AfterViews
    void afterView() {
        presenter = new BarChartStatisticsPresenter(this, appRequester);
        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        if (StringUtil.isEmpty(province)) {
            presenter.loadCityData(city);
        } else {
            presenter.loadProvinceData(province);
        }
        title.setText("柱状图模式统计");
    }

    @Click(R.id.back)
    void onClick() {
        finish();
    }

    @Override
    public void updateBarChartStatisticsData(int transportationCount, int publicService, int industry, int tourism, int fisheries) {
        this.transportationCount = transportationCount;
        this.publicService = publicService;
        this.industry = industry;
        this.tourism = tourism;
        this.fisheries = fisheries;
        updateView();
    }

    @UiThread
    void updateView() {
        BarChartItemData[] items = new BarChartItemData[]{
                new BarChartItemData("工业", transportationCount),
                new BarChartItemData("旅游", publicService),
                new BarChartItemData("交通", industry),
                new BarChartItemData("渔业", tourism),
                new BarChartItemData("公共", fisheries)};
        barChartView.setItems(items);
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
