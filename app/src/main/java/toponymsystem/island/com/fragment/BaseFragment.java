package toponymsystem.island.com.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import toponymsystem.island.com.activity.BaseActivity;
import toponymsystem.island.com.rest.LoggingInterceptor;
import toponymsystem.island.com.rest.RestClient;
import toponymsystem.island.com.rest.api.URL;
import toponymsystem.island.com.rest.api.requester.AppRequester;
import toponymsystem.island.com.rest.api.requester.AppRequesterImpl;

public class BaseFragment extends Fragment implements FragmentInterface {

    private static final int SHOW_PROGRESS = 1;
    private static final int DISMISS_PROGRESS = 2;
    private ProgressHandler mProgressHandler;
    DisplayImageOptions displayImageOptions;
    protected RestClient mGsonClient = null;
    protected AppRequester appRequester;
    protected BaseActivity mActivity;

    public BaseFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressHandler = new ProgressHandler(getActivity());
        appRequester = new AppRequesterImpl(getActivity());
        initDisplayImageOptions();
        initGsonRetrofit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    /**
     * 初始化返回数据为JSON类型的Retrofit并配置拦截器
     */
    private void initGsonRetrofit() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.HTTP)
                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
                .build();
        mGsonClient = retrofit.create(RestClient.class);
    }

    /**
     * 初始化ImageLoader配置
     */
    private void initDisplayImageOptions() {
        displayImageOptions = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  //  设置图片在加载前是否重置、复位
                .cacheInMemory(true) //   设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) //   设置下载的图片是否缓存在SD卡中
                .displayer(new FadeInBitmapDisplayer(100))// 图片加载好后渐入的动画时间
                .displayer(new SimpleBitmapDisplayer()) //  还可以设置圆角图片new RoundedBitmapDisplayer(20)
                .handler(new Handler())
                .build();
    }

    /**
     * Toast
     *
     * @param msg
     */
    public void showToast(String msg) {
        if (msg != null || msg.length() > 0)
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        mActivity.showLoading();
    }

    @Override
    public void dismissLoading() {
        mActivity.dismissLoading();
    }

    public static class ProgressHandler extends Handler {
        private ProgressDialog mProgressDialog;
        private Activity mActivity;

        public ProgressHandler(Activity activity) {
            super(Looper.getMainLooper());
            mActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SHOW_PROGRESS:
                    if (null == mProgressDialog) {
                        mProgressDialog = new ProgressDialog(mActivity);
                    }
                    if (!mProgressDialog.isShowing()) {
                        mProgressDialog.setCancelable(true);
                        mProgressDialog.show();
                        mProgressDialog.setContentView(new ProgressBar(mActivity));
                        mProgressDialog.getWindow().setGravity(Gravity.CENTER);
                    }
                    break;
                case DISMISS_PROGRESS:
                    if (null != mProgressDialog && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
