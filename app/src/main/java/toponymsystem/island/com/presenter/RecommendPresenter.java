package toponymsystem.island.com.presenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import toponymsystem.island.com.iView.IRecommendView;
import toponymsystem.island.com.model.IslandData;
import toponymsystem.island.com.model.SearchData;
import toponymsystem.island.com.rest.Dto.PostParams;
import toponymsystem.island.com.rest.RestClient;
import toponymsystem.island.com.utils.ThreadPoolManager;

/**
 * Created by CuiXiaoYong
 * on 2016/4/13
 * E-Mail:hellocui@aliyun.com
 * <p/>
 * MVP模式
 */
public class RecommendPresenter {

    private static final String LYYL = "旅游娱乐";
    private static final String GY = "工业";
    private static final String YY = "渔业";
    private static final String GGFW = "公共服务";
    private static final String JT = "交通";
    private static final List<IslandData> lyList;
    private static final List<IslandData> gyList;
    private static final List<IslandData> yyList;
    private static final List<IslandData> ggList;
    private static final List<IslandData> jtList;
    private static final ThreadPoolManager mThreadPoolManager;

    private RestClient mGsonClient = null;
    private IRecommendView iView;

    static {
        mThreadPoolManager = ThreadPoolManager.getInstance();
        lyList = new ArrayList<>();
        gyList = new ArrayList<>();
        yyList = new ArrayList<>();
        ggList = new ArrayList<>();
        jtList = new ArrayList<>();
    }

    public RecommendPresenter() {
    }

    public RecommendPresenter(IRecommendView view, RestClient gsonClient) {
        mGsonClient = gsonClient;
        iView = view;
    }

    public void loadData() {
        getItemData("旅游娱乐", lyList);
        getItemData("工业", gyList);
        getItemData("渔业", yyList);
        getItemData("公共服务", ggList);
        getItemData("交通", jtList);
    }

    /**
     * 获取各用途数据
     *
     * @param usg 用途
     */
    private void getItemData(final String usg, final List<IslandData> list) {
        iView.showLoadingDialog();
        PostParams params = new PostParams();
        params.put("usg", usg);
        params.put("pageIndex", "0");
        Call<SearchData<List<IslandData>>> mCall = mGsonClient.getPageCount(params);
        mCall.enqueue(new Callback<SearchData<List<IslandData>>>() {
            @Override
            public void onResponse(Response<SearchData<List<IslandData>>> response, Retrofit retrofit) {
                if (null != response) {
                    int pageCount = response.body().getTotal() / response.body().getPageSize();
                    getData(pageCount, usg, list);
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
     * @param usg   用途
     */
    private void getData(final int count, final String usg, final List<IslandData> list) {
        mThreadPoolManager.executeTask(new Runnable() {
            @Override
            public void run() {
                list.clear();
                for (int i = 0; i <= count; i++) {
                    PostParams params = new PostParams();
                    params.put("usg", usg);
                    params.put("pageIndex", String.valueOf(i));
                    Call<SearchData<List<IslandData>>> mCall = mGsonClient.getRecommendData(params);
                    try {
                        Response<SearchData<List<IslandData>>> data = mCall.execute();
                        for (int j = 0; j < data.body().getDatas().size(); j++) {
                            if (data.body().getDatas().get(j).isRecommend()) {
                                list.add(data.body().getDatas().get(j));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
                iView.dismissLoadingDialog();
                if (usg.equals(LYYL)) {
                    iView.updateLYYLRecommendData(list);
                } else if (usg.equals(GY)) {
                    iView.updateGYRecommendData(list);
                } else if (usg.equals(YY)) {
                    iView.updateYYRecommendData(list);
                } else if (usg.equals(GGFW)) {
                    iView.updateGGFWRecommendData(list);
                } else if (usg.equals(JT)) {
                    iView.updateJTFWRecommendData(list);
                }
            }
        });
    }
}
