package toponymsystem.island.com.presenter;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import toponymsystem.island.com.BuildConfig;
import toponymsystem.island.com.iView.IAboutView;
import toponymsystem.island.com.model.VersionInfoData;
import toponymsystem.island.com.rest.Dto.PostParams;
import toponymsystem.island.com.rest.RestClient;

/**
 * Created by Yong
 * on 2016/4/30.
 * E-Mail:hellocui@aliyun.com
 */
public class VersionUpdatePresenter {

    private RestClient mGsonClient = null;
    private IAboutView iView;
    private static final int LASTEST_VERSION = 0;
    private static final int HAS_NEW_VERSION = 1;

    public VersionUpdatePresenter() {
    }

    public VersionUpdatePresenter(IAboutView view, RestClient gsonClient) {
        mGsonClient = gsonClient;
        iView = view;
    }

    public void loadData() {
        getData();
    }

    /**
     * 获取版本
     */
    private void getData() {
        iView.showLoadingDialog();
        PostParams params = new PostParams();
        params.put("version", String.valueOf(BuildConfig.VERSION_CODE));
        Call<VersionInfoData> mCall = mGsonClient.getVersionData(params);
        mCall.enqueue(new Callback<VersionInfoData>() {
            @Override
            public void onResponse(Response<VersionInfoData> response, Retrofit retrofit) {
                if (response.body().getCode() == LASTEST_VERSION) {
                    iView.showTheLastVersion();
                } else {
                    iView.updateData(response.body());
                }
                iView.dismissLoadingDialog();
            }

            @Override
            public void onFailure(Throwable throwable) {
                iView.dismissLoadingDialog();
            }
        });
    }
}
