package toponymsystem.island.com.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import toponymsystem.island.com.R;
import toponymsystem.island.com.activity.BarChartStatisticsActivity_;
import toponymsystem.island.com.activity.TableStatisticsActivity_;
import toponymsystem.island.com.iView.IStatisticsView;
import toponymsystem.island.com.utils.StringUtil;
import toponymsystem.island.com.widget.cityView.RegionDialog;

/**
 * Created by CuiXiaoYong E-mial:hellocui@aliyun.com
 * on 2016/3/27
 */
@EFragment(R.layout.activity_island_statistics)
public class StatisticsFragment extends BaseFragment implements IStatisticsView, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    @ViewById(R.id.tv_dist)
    TextView dist;
    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.industrial)
    CheckBox industrial;
    @ViewById(R.id.tourism)
    CheckBox tourism;
    @ViewById(R.id.transportation)
    CheckBox transportation;
    @ViewById(R.id.fisheries)
    CheckBox fisheries;
    @ViewById(R.id.publicService)
    CheckBox publicService;
    @ViewById(R.id.information_radio_group)
    RadioGroup informationRadioGroup;
    @ViewById(R.id.list)
    RadioButton list;
    @ViewById(R.id.barChart)
    RadioButton barChart;
    private ArrayList<String> params = new ArrayList<>();

    private boolean isListStatistics = true;//默认列表统计
    private String province;
    private String city;

    public StatisticsFragment() {
    }

    @AfterViews
    void afterView() {
        title.setText("海岛统计");
        industrial.setOnCheckedChangeListener(this);
        tourism.setOnCheckedChangeListener(this);
        transportation.setOnCheckedChangeListener(this);
        fisheries.setOnCheckedChangeListener(this);
        publicService.setOnCheckedChangeListener(this);
        informationRadioGroup.setOnCheckedChangeListener(this);
    }

    @Click({R.id.rl_dist, R.id.go_next})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_dist:
                RegionDialog dialog = new RegionDialog(getActivity(), R.style.DialogStyleBottom, this);
                dialog.show();
                break;
            case R.id.go_next:
                if (StringUtil.isEmpty(String.valueOf(dist.getText()))) {
                    showToast("查询区域不能为空");
                    return;
                }
                if (isListStatistics) {
                    //跳转列表
                    Intent intent = new Intent(getActivity(), TableStatisticsActivity_.class);
                    intent.putStringArrayListExtra("params", params);
                    intent.putExtra("province", province);
                    intent.putExtra("city", city);
                    startActivity(intent);
                } else {
                    //柱状图
                    Intent intent = new Intent(getActivity(), BarChartStatisticsActivity_.class);
                    intent.putExtra("province", province);
                    intent.putExtra("city", city);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 获取选择的省市县
     *
     * @param ProvinceName 省
     * @param CityName     市
     * @param DistrictName 县——实际用不到
     */
    @Override
    public void getDistrictData(String ProvinceName, String CityName, String DistrictName) {

        if (CityName.equals("全部")) {
            dist.setText(ProvinceName);
            province = ProvinceName;
        } else {
            dist.setText(CityName);
            city = CityName;
        }
    }

    /**
     * checkbox事件，配置请求参数
     *
     * @param buttonView 点击按钮
     * @param isChecked  是否选中
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.industrial:
                if (isChecked) {
                    params.add("工业");
                } else {
                    params.remove("工业");
                }
                break;
            case R.id.tourism:
                if (isChecked) {
                    params.add("旅游娱乐");
                } else {
                    params.remove("旅游娱乐");
                }
                break;
            case R.id.transportation:
                if (isChecked) {
                    params.add("交通运输");
                } else {
                    params.remove("交通运输");
                }
                break;
            case R.id.fisheries:
                if (isChecked) {
                    params.add("渔业");
                } else {
                    params.remove("渔业");
                }
                break;
            case R.id.publicService:
                if (isChecked) {
                    params.add("公共服务");
                } else {
                    params.remove("公共服务");
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (list.getId() == checkedId) {
            isListStatistics = true;
        } else {
            isListStatistics = false;
        }
    }
}
