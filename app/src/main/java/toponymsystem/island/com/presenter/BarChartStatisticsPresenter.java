package toponymsystem.island.com.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import toponymsystem.island.com.iView.IBarChartStatisticsView;
import toponymsystem.island.com.rest.api.CallbackListener;
import toponymsystem.island.com.rest.api.URL;
import toponymsystem.island.com.rest.api.requester.AppRequester;

/**
 * Created by CuiXiaoYong
 * on 2016/4/13
 * E-Mail:hellocui@aliyun.com
 * <p>
 * MVP模式
 */
public class BarChartStatisticsPresenter {

    private AppRequester appRequester;
    IBarChartStatisticsView iView;
    private String httpURL;

    private int transportationCount;
    private int publicService;
    private int industry;
    private int tourism;
    private int fisheries;

    public BarChartStatisticsPresenter() {
    }

    public BarChartStatisticsPresenter(IBarChartStatisticsView view, AppRequester appRequester) {
        this.appRequester = appRequester;
        iView = view;
    }

    public void loadCityData(String city) {
        try {
            httpURL = URL.ISLAND_STATISTICS + "?" + "city=" + URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getData();
    }

    public void loadProvinceData(String province) {
        try {
            httpURL = URL.ISLAND_STATISTICS + "?" + "province=" + URLEncoder.encode(province, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getData();
    }

    private void getData() {
        iView.showLoadingDialog();
        appRequester.request(httpURL);
        appRequester.setOnCallbackListener(new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                JSONObject jsonObjs = null;
                try {
                    jsonObjs = new JSONObject(data);
                    JSONObject json = jsonObjs.getJSONObject("usg");
                    if (json.has("交通运输")) {
                        transportationCount = json.getInt("交通运输");
                    }
                    if (json.has("公共服务")) {
                        publicService = json.getInt("公共服务");
                    }
                    if (json.has("工业")) {
                        industry = json.getInt("工业");
                    }
                    if (json.has("旅游娱乐")) {
                        tourism = json.getInt("旅游娱乐");
                    }
                    if (json.has("渔业")) {
                        fisheries = json.getInt("渔业");
                    }
                    iView.updateBarChartStatisticsData(transportationCount, publicService,industry,tourism,fisheries);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                iView.dismissLoadingDialog();
            }

            @Override
            public void onFailure(String errorEvent) {
                iView.dismissLoadingDialog();
            }
        });
    }
}
