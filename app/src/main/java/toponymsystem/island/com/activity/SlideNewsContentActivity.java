package toponymsystem.island.com.activity;

import android.support.annotation.UiThread;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import toponymsystem.island.com.R;
import toponymsystem.island.com.model.SlideNewsDetailsData;
import toponymsystem.island.com.rest.Dto.PostParams;
import toponymsystem.island.com.rest.api.URL;
import toponymsystem.island.com.utils.ScreenUtils;

/**
 * Created by CuiXiaoYong
 * on 2016/4/12
 * E-Mail:hellocui@aliyun.com
 */
@EActivity(R.layout.activity_slide_news_detials)
public class SlideNewsContentActivity extends BaseActivity {

    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.news_title)
    TextView newsTitle;
    @ViewById(R.id.news_time)
    TextView newsTime;
    @ViewById(R.id.news_content)
    TextView newsContent;
    @ViewById(R.id.ll_news_images)
    LinearLayout ll_NewsIimages;
    @Bean
    SlideNewsDetailsData data;
    private int mNewsId;
    private String imageUrl;
    private String[] arrayUrl;//新闻图片URL数组

    @AfterViews
    void afterView() {
        title.setText("新闻详情");
        mNewsId = getIntent().getIntExtra("newsId", -1);
        getSlideNewsDetail(mNewsId);
    }

    @UiThread
    void updateUi(SlideNewsDetailsData data) {

        newsTitle.setText(data.getTitle());//标题
        newsTime.setText("时间：" + data.getCreatedStr());//时间
        newsContent.setText(data.getContent());//内容

        if (data.getImgs().contains(",")) {
            arrayUrl = data.getImgs().replace("[", "").replace("]", "").split(",");
            for (int i = 0; i < arrayUrl.length; i++) {
                createImage(arrayUrl[i].replace("\"", ""), i + 1);
            }
        } else {
            imageUrl = data.getImgUrl();
            createImage(imageUrl, 1);
        }
    }

    @Click({R.id.back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * 创建图片
     *
     * @param imageUrl
     */
    private void createImage(String imageUrl, int count) {
        String url = URL.HTTP + imageUrl;
        //图片属性
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                ScreenUtils.dip2px(this, 150));
        imageLayoutParams.setMargins(10, 10, 10, 10);
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ll_NewsIimages.addView(imageView, imageLayoutParams);
        //图标题属性
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView text = new TextView(this);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setText("图(" + count + ")");
        ll_NewsIimages.addView(text, textLayoutParams);
        ImageLoader.getInstance().displayImage(url, imageView, displayImageOptions);
    }

    /**
     * 获取轮播新闻详情
     *
     * @param newsId
     */
    private void getSlideNewsDetail(int newsId) {
        showLoading();
        PostParams params = new PostParams();
        params.put("id", String.valueOf(newsId));
        Call<SlideNewsDetailsData> mCall = mGsonClient.getSlideNewsDetail(params);
        mCall.enqueue(new Callback<SlideNewsDetailsData>() {
            @Override
            public void onResponse(Response<SlideNewsDetailsData> response, Retrofit retrofit) {
                if (null != response) {
                    data = response.body();
                    updateUi(data);
                    dismissLoading();
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                dismissLoading();
            }
        });
    }
}
