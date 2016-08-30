package toponymsystem.island.com.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import toponymsystem.island.com.R;
import toponymsystem.island.com.adapter.TableStatisticsAdapter;
import toponymsystem.island.com.iView.ITableStatisticsView;
import toponymsystem.island.com.model.IslandData;
import toponymsystem.island.com.presenter.TableStatisticsPresenter;
import toponymsystem.island.com.utils.StringUtil;

/**
 * Created by Yong
 * on 2016/4/21.
 * E-Mail:hellocui@aliyun.com
 */
@EActivity(R.layout.activity_table_statistics)
public class TableStatisticsActivity extends BaseActivity implements ITableStatisticsView, AdapterView.OnItemClickListener {

    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.data)
    ListView data;
    @ViewById(R.id.header)
    LinearLayout header;
    private TableStatisticsPresenter presenter;
    private List<IslandData> mListData;

    @AfterViews
    void afterView() {
        List<String> params = getIntent().getStringArrayListExtra("params");
        String province = getIntent().getStringExtra("province");
        String city = getIntent().getStringExtra("city");
        presenter = new TableStatisticsPresenter(this, mGsonClient);
        if (StringUtil.isEmpty(province)) {
            presenter.loadCityData(params, city);
        } else {
            presenter.loadProvinceData(params, province);
        }
        title.setText("列表模式统计");
        data.setOnItemClickListener(this);
    }

    @Click(R.id.back)
    void onClick() {
        finish();
    }

    @Override
    public void updateTableStatisticsData(List<IslandData> dataList) {
        mListData = dataList;
        updateUI(dataList);
    }

    @UiThread
    void updateUI(List<IslandData> dataList) {
        TableStatisticsAdapter adapter = new TableStatisticsAdapter(this, dataList);
        if (dataList.size() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_listheader_nodata, null);
            data.addHeaderView(view);
            data.setAdapter(adapter);
            return;
        }
        header.setVisibility(View.VISIBLE);
        data.setAdapter(adapter);
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
        Intent intent = new Intent(this, IslandInformationActivity_.class);
        intent.putExtra("islandID", mListData.get(position).getIslandId());
        startActivity(intent);
    }
}
