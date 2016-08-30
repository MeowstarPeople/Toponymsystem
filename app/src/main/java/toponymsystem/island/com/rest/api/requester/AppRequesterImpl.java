package toponymsystem.island.com.rest.api.requester;

import android.content.Context;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

import toponymsystem.island.com.rest.RestBean;
import toponymsystem.island.com.rest.api.Api;
import toponymsystem.island.com.rest.api.ApiImpl;
import toponymsystem.island.com.rest.api.CallbackListener;
import toponymsystem.island.com.rest.api.HttpRequest;
import toponymsystem.island.com.utils.ThreadPoolManager;


/**
 * AppRequester接口的实现类
 *
 * @author CuixXiaoYong
 * @version 1.0
 * @date 16/3/21
 */
public class AppRequesterImpl<T> implements AppRequester<T> {

    private final static String TIME_OUT_EVENT_MSG = "连接服务器失败";
    private Context context;
    private Api api;
    private HttpRequest httpRequest;
    private ThreadPoolManager mThreadPoolManager;
    CallbackListener<T> listener;

    public AppRequesterImpl(Context context) {
        this.context = context;
        httpRequest = HttpRequest.getInstance();
        this.api = new ApiImpl();
        mThreadPoolManager = ThreadPoolManager.getInstance();
    }

    /**
     * 设置监听事件
     *
     * @param listener
     */
    @Override
    public void setOnCallbackListener(CallbackListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public void request(final String Url, final Map<String, String> params, final Type type) {
        mThreadPoolManager.executeTask(new Runnable() {
            @Override
            public void run() {
                String mRequestData = api.request(params, Url);
                Gson gson = new Gson();
                RestBean<T> data = gson.fromJson(mRequestData, type);
                if (listener != null && data != null) {
                    listener.onSuccess((T) data);
                } else {
                    listener.onFailure(data.toString());
                }
            }
        });
    }

    @Override
    public void request(final String Url, final Map<String, String> params) {
        mThreadPoolManager.executeTask(new Runnable() {
            @Override
            public void run() {
                String data = api.request(params, Url);
                if (listener != null && data != null) {
                    listener.onSuccess((T) data);
                } else {
                    listener.onFailure(TIME_OUT_EVENT_MSG);
                }
            }
        });
    }

    @Override
    public void request(final String Url) {
        mThreadPoolManager.executeTask(new Runnable() {
            @Override
            public void run() {
                String data = api.request(Url);
                if (listener != null && data != null) {
                    listener.onSuccess((T) data);
                } else {
                    listener.onFailure(TIME_OUT_EVENT_MSG);
                }
            }
        });

    }
}
