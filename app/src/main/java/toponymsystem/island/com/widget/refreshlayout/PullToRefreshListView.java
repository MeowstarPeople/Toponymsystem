package toponymsystem.island.com.widget.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PullToRefreshListView extends ListView implements Pullable {

    private OnPullToRefreshListener mListener;

    public PullToRefreshListView(Context context) {
        super(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnRefreshListener(OnPullToRefreshListener listener) {
        mListener = listener;
        PullToRefreshLayout.setOnRefreshListener(listener);
    }

    @Override
    public boolean canPullDown() {
        if (mListener != null) {
            if (getCount() == 0) {
                return true;// 无数据时也可以下拉刷新
            } else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0) {
                return true;// 滑到ListView的顶部
            } else
                return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean canPullUp() {
        if (mListener != null) {
            if (getCount() == 0) {
                // 没有item的时候也可以上拉加载
                return true;
            } else if (getLastVisiblePosition() == (getCount() - 1)) {
                // 滑到底部了
                if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null && getChildAt(getLastVisiblePosition()
                        - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                    return true;
            }
        }
        return false;
    }
}
