package toponymsystem.island.com.rest.api.requester;

import java.lang.reflect.Type;
import java.util.Map;

import toponymsystem.island.com.rest.api.CallbackListener;


/**
 * 接收app层的各种Action
 *
 * @author CuiXiaoYong
 * @version 1.0
 * @date 16/3/25
 */
public interface AppRequester<T> {
    /**
     * @param Url    请求地址
     * @param params 请求参数
     */
    void request(String Url, Map<String, String> params, Type type);

    /**
     * @param Url
     * @param params
     */
    void request(String Url, Map<String, String> params);

    /**
     * @param Url
     */
    void request(String Url);

    /**
     * @param listener
     */
    void setOnCallbackListener(CallbackListener<T> listener);
}
