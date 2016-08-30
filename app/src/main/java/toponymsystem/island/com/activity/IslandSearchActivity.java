package toponymsystem.island.com.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.UiThread;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import toponymsystem.island.com.R;
import toponymsystem.island.com.adapter.SearchDataAdapter;
import toponymsystem.island.com.model.IslandData;
import toponymsystem.island.com.model.SearchData;
import toponymsystem.island.com.rest.Dto.PostParams;

/**
 * Created by CuiXiaoYong
 * on 2016/4/12
 * E-Mail:hellocui@aliyun.com
 */
@EActivity(R.layout.activity_search)
public class IslandSearchActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private SearchData<List<IslandData>> data;
    private SearchDataAdapter adapter;
    private List<IslandData> listData;
    private String keyWord;
    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.auto_text_island_name)
    AutoCompleteTextView keyName;
    @ViewById(R.id.list_name)
    ListView listName;
    @ViewById(R.id.linear_search)
    LinearLayout linearSearch;
    private TextView loadMore;
    private View view;
    private int count = 0;

    @AfterViews
    void afterView() {
        title.setText("海岛查询");
        view = getLayoutInflater().inflate(R.layout.layout_load_more, null);
        loadMore = (TextView) view.findViewById(R.id.load_more);
        keyName.setOnKeyListener(onKeyListener);
        listName.addFooterView(view);
        listName.setOnItemClickListener(this);
    }

    @Click({R.id.back, R.id.text_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.text_cancel:
                finish();
                break;
        }
    }


    /**
     * 回车键监听
     */
    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                /*隐藏软键盘*/
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    listData = null;
                    keyWord = keyName.getText().toString().trim();
                    //发送请求
                    loadData(keyWord);
                } catch (Exception e) {
                }
                return true;
            }
            return false;
        }
    };

    @UiThread
    void updateUi(final SearchData<List<IslandData>> data) {
        if (data.getDatas().size() > 0) {
            listData = data.getDatas();
            linearSearch.setVisibility(View.VISIBLE);
            adapter = new SearchDataAdapter(this, listData, displayImageOptions);
            listName.setAdapter(adapter);
            if (listData.size() < data.getTotal()) {
                /**
                 * 点击加载更多
                 */
                loadMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count++;
                        loadMore(keyWord, count);
                        if (count == (data.getTotal() / data.getPageSize())) {
                            loadMore.setText("没有更多内容");
                            loadMore.setEnabled(false);
                        }
                    }
                });
            } else {
                loadMore.setText("没有更多内容");
                loadMore.setEnabled(false);
            }
        } else {
            showToast("查询结果为空");
        }
    }

    /**
     * 根据关键字查询海岛
     *
     * @param keyWord 查询关键字
     */
    public void loadData(String keyWord) {
        showLoading();
        PostParams params = new PostParams();
        params.put("name", keyWord);
        params.put("pageIndex", String.valueOf(0));
        Call<SearchData<List<IslandData>>> mCall = mGsonClient.getSearchData(params);
        mCall.enqueue(new Callback<SearchData<List<IslandData>>>() {
            @Override
            public void onResponse(Response<SearchData<List<IslandData>>> response, Retrofit retrofit) {
                if (null != response) {
                    data = response.body();
                    updateUi(data);
                } else {
                }
                dismissLoading();
            }

            @Override
            public void onFailure(Throwable throwable) {
                dismissLoading();
            }
        });
    }

    /**
     * 加载更多数据
     *
     * @param page 页数
     */
    public void loadMore(String keyWord, int page) {
        showLoading();
        PostParams params = new PostParams();
        params.put("name", keyWord);
        params.put("pageIndex", String.valueOf(page));
        Call<SearchData<List<IslandData>>> mCall = mGsonClient.getSearchData(params);
        mCall.enqueue(new Callback<SearchData<List<IslandData>>>() {
            @Override
            public void onResponse(Response<SearchData<List<IslandData>>> response, Retrofit retrofit) {
                if (null != response) {
                    listData.addAll(response.body().getDatas());
                    adapter.notifyDataSetChanged();
                }
                dismissLoading();
            }

            @Override
            public void onFailure(Throwable throwable) {
                dismissLoading();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int mIslandId = -1;
        Intent intent = new Intent(this, IslandInformationActivity_.class);
        mIslandId = listData.get(position).getIslandId();
        intent.putExtra("islandID", mIslandId);
        startActivity(intent);
    }
}
