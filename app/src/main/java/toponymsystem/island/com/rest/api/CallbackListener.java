package toponymsystem.island.com.rest.api;

/**
 * Action的处理结果回调监听器
 *
 * @author CuiXiaoYong
 * @date 16/3/25
 * @version 1.0
 */
public interface CallbackListener<T> {
    /**
     * 成功时调用
     *
     * @param data 返回的数据
     */
     void onSuccess(T data);

    /**
     * 失败时调用
     *
     * @param errorEvent 错误信息
     */
     void onFailure(String errorEvent);
}
