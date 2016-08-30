package toponymsystem.island.com.widget.refreshlayout;

/**
 * Created by CuiXiaoYong
 * on 2016/4/17
 * E-Mail:hellocui@aliyun.com
 */
public interface OnPullToRefreshListener {
    /**
     * 刷新操作
     */
    void onRefresh(PullToRefreshLayout pullToRefreshLayout);

    /**
     * 加载操作
     */
    void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
}
