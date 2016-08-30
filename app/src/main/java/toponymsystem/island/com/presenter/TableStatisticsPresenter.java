package toponymsystem.island.com.presenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import toponymsystem.island.com.iView.IRecommendView;
import toponymsystem.island.com.iView.ITableStatisticsView;
import toponymsystem.island.com.model.IslandData;
import toponymsystem.island.com.model.SearchData;
import toponymsystem.island.com.rest.Dto.PostParams;
import toponymsystem.island.com.rest.RestClient;
import toponymsystem.island.com.utils.ThreadPoolManager;

/**
 * Created by CuiXiaoYong
 * on 2016/4/13
 * E-Mail:hellocui@aliyun.com
 * <p>
 * MVP模式
 */
public class TableStatisticsPresenter {

    private  List<IslandData> dataList;
    private static final ThreadPoolManager mThreadPoolManager;

    private RestClient mGsonClient = null;
    private ITableStatisticsView iView;
    Map params = new IdentityHashMap();

    static {
        mThreadPoolManager = ThreadPoolManager.getInstance();
    }

    public TableStatisticsPresenter() {
    }

    public TableStatisticsPresenter(ITableStatisticsView view, RestClient gsonClient) {
        mGsonClient = gsonClient;
        iView = view;
    }

    public void loadCityData(List<String> list, String city) {
        params.put("city", city);
        for (int i = 0; i < list.size(); i++) {
            params.put(new String("usg"), list.get(i));
        }
        getItemData(params);
    }

    public void loadProvinceData(List<String> list, String province) {
        params.put("province", province);
        for (int i = 0; i < list.size(); i++) {
            params.put(new String("usg"), list.get(i));
        }
        getItemData(params);
    }

    /**
     * 获取各用途数据
     *
     * @param params 参数
     */
    private void getItemData(final Map params) {
        dataList = new ArrayList<>();
        iView.showLoadingDialog();
        params.put("pageIndex", "0");
        Call<SearchData<List<IslandData>>> mCall = mGsonClient.getPageCount(params);
        mCall.enqueue(new Callback<SearchData<List<IslandData>>>() {
            @Override
            public void onResponse(Response<SearchData<List<IslandData>>> response, Retrofit retrofit) {
                if (null != response) {
                    int pageCount = response.body().getTotal() / response.body().getPageSize();
                    getData(pageCount, dataList);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                iView.dismissLoadingDialog();
            }
        });
    }

    /**
     * 开起线程，按page循环执行同步请求
     *
     * @param count page总数
     * @param list  用途
     */
    private void getData(final int count, final List<IslandData> list) {
        mThreadPoolManager.executeTask(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= count; i++) {
                    params.remove("pageIndex");
                    params.put("pageIndex", String.valueOf(i));
                    Call<SearchData<List<IslandData>>> mCall = mGsonClient.getTableStatisticsData(params);
                    try {
                        Response<SearchData<List<IslandData>>> data = mCall.execute();
                        list.addAll(data.body().getDatas());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                iView.dismissLoadingDialog();
                iView.updateTableStatisticsData(list);
            }
        });
    }
}
