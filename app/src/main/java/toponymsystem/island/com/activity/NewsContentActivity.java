package toponymsystem.island.com.activity;

import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import toponymsystem.island.com.R;
import toponymsystem.island.com.iView.INewsContentView;
import toponymsystem.island.com.model.NewsContentData;
import toponymsystem.island.com.presenter.NewsContentPresenter;
import toponymsystem.island.com.rest.api.URL;
import toponymsystem.island.com.utils.ScreenUtils;
import toponymsystem.island.com.widget.AlignTextView;

/**
 * Created by CuiXiaoYong
 * on 2016/4/19
 * E-Mail:hellocui@aliyun.com
 */
@EActivity(R.layout.activty_news_content)
public class NewsContentActivity extends BaseActivity implements INewsContentView {
    /**
     * 标题
     */
    @ViewById(R.id.title)
    TextView title;
    /**
     * 新闻标题
     */
    @ViewById(R.id.tv_title)
    TextView newsTitle;
    /**
     * 时间
     */
    @ViewById(R.id.tv_date)
    TextView date;
    /**
     * 来源
     */
    @ViewById(R.id.tv_source)
    TextView source;
    /**
     * 正文
     */
    @ViewById(R.id.tv_content)
    AlignTextView content;
    @ViewById(R.id.ll_images)
    LinearLayout ll_iNewsImages;
    private NewsContentPresenter presenter;
    private String contentLink;

    @AfterViews
    void afterView() {
        title.setText("新闻详情");
        contentLink = getIntent().getStringExtra("contentLink");
        presenter = new NewsContentPresenter(this, appRequester);
        presenter.loadData(URL.ISLAND_HOST + contentLink);
    }

    /**
     * 点击事件
     */
    @Click(R.id.back)
    void onClick() {
        finish();
    }

    /**
     * 获取返回数据
     *
     * @param data 新闻数据
     */
    @Override
    public void updateNewsContentData(NewsContentData data) {
        updateView(data);
    }

    /**
     * 主线程加载数据
     */
    @UiThread
    void updateView(NewsContentData data) {
        newsTitle.setText(getIntent().getStringExtra("newsTitle"));
        date.setText(data.getDate());
        source.setText(data.getSource());
        content.setText(data.getContent());
        for (int i = 0; i < data.getImageLink().size(); i++) {
            try {
                createImage(data.getImageLink().get(i), data.getImageTitle().get(i));
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    /**
     * 创建图片
     *
     * @param imageUrl
     */
    private void createImage(String imageUrl, String imageTitle) {
        String url = URL.ISLAND_HOST + imageUrl;
        //图片属性
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                ScreenUtils.dip2px(this, 150));
        imageLayoutParams.setMargins(10, 10, 10, 10);
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ll_iNewsImages.addView(imageView, imageLayoutParams);
        //图标题属性
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView text = new TextView(this);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setText(imageTitle);
        ll_iNewsImages.addView(text, textLayoutParams);
        ImageLoader.getInstance().displayImage(url, imageView, displayImageOptions);
    }

    @Override
    public void showLoadingDialog() {
        showLoading();
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoading();
    }
}
