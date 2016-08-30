package toponymsystem.island.com.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import toponymsystem.island.com.R;
import toponymsystem.island.com.activity.NewsContentActivity_;
import toponymsystem.island.com.adapter.NewsAdapter;
import toponymsystem.island.com.iView.IIslandNewsView;
import toponymsystem.island.com.model.IslandNewsData;
import toponymsystem.island.com.presenter.IslandNewsPresenter;
import toponymsystem.island.com.rest.api.URL;
import toponymsystem.island.com.widget.refreshlayout.OnPullToRefreshListener;
import toponymsystem.island.com.widget.refreshlayout.PullToRefreshLayout;
import toponymsystem.island.com.widget.refreshlayout.PullToRefreshListView;

/**
 * Created by CuiXiaoYong
 * on 2016/4/15
 * E-Mail:hellocui@aliyun.com
 */
@EFragment(R.layout.fragment_islandnews)
public class IslandNewsFragment extends BaseFragment implements IIslandNewsView, OnPullToRefreshListener, AdapterView.OnItemClickListener {
    @ViewById(R.id.data)
    PullToRefreshListView data;
    @ViewById(R.id.refresh_view)
    PullToRefreshLayout refresh_view;

    private List<IslandNewsData> newsData = new ArrayList<>();
    private IslandNewsPresenter presenter;
    private NewsAdapter adapter;
    private static final int ALLPAGE = 10;//最多上拉10次
    private int pageCount = 1;

    private boolean isRefresh;

    @Override
    public void onResume() {
        super.onResume();
    }

    @AfterViews
    void afterView() {
        presenter = new IslandNewsPresenter(this, appRequester);
        presenter.loadData(URL.ISLAND_NEWS_INDEX);
        adapter = new NewsAdapter(getActivity(), newsData, displayImageOptions);
        data.setAdapter(adapter);
        data.setOnRefreshListener(this);
        initListItemListener();
    }

    private void initListItemListener() {
        data.setOnItemClickListener(this);
    }

    @Override
    public void updateIIslandNewsData(List<IslandNewsData> dataList) {
        updateLoadMoreData(dataList);
    }

    @Override
    public void showLoadingDialog() {
        showLoading();
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoading();
    }


    @UiThread
    void updateLoadMoreData(List<IslandNewsData> dataList) {
        if (isRefresh) {
            newsData.clear();
            newsData.addAll(dataList);
            adapter.notifyDataSetChanged();
        } else {
            newsData.addAll(dataList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        isRefresh = true;
        presenter.loadData(URL.ISLAND_NEWS_INDEX);
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);//通知控件刷新完成
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        isRefresh = false;
        pageCount++;
        presenter.loadData("http://www.chinaislands.gov.cn/channels/20198_" + pageCount + ".html");
        adapter.notifyDataSetChanged();
        pullToRefreshLayout.loadMoreFinish(PullToRefreshLayout.SUCCEED);
        int position = data.getFirstVisiblePosition();
        data.setSelection(position + 1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), NewsContentActivity_.class);
        intent.putExtra("contentLink", newsData.get(position).getNewsContentLink());
        intent.putExtra("newsTitle", newsData.get(position).getTitle());
        startActivity(intent);
    }
}
