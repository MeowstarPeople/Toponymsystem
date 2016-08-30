package toponymsystem.island.com.activity;

import android.content.Intent;
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
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import toponymsystem.island.com.R;
import toponymsystem.island.com.model.IslandInformationData;
import toponymsystem.island.com.rest.Dto.PostParams;
import toponymsystem.island.com.rest.api.URL;
import toponymsystem.island.com.utils.ScreenUtils;
import toponymsystem.island.com.utils.StringUtil;
import toponymsystem.island.com.widget.AlignTextView;

/**
 * Created by CuiXiaoYong
 * on 2016/4/10
 * E-Mail:hellocui@aliyun.com
 */
@EActivity(R.layout.activity_island_information)
public class IslandInformationActivity extends BaseActivity {

    @ViewById(R.id.ll_island_images)
    LinearLayout ll_IslandIimages;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.island_name)
    TextView name;

    @ViewById(R.id.coordinates)
    TextView coordinates;

    @ViewById(R.id.is_reside)
    TextView reside;

    @ViewById(R.id.is_develop)
    AlignTextView develop;

    @ViewById(R.id.dist)
    TextView dist;

    @ViewById(R.id.usg)
    TextView usg;

    @ViewById(R.id.details)
    AlignTextView details;
    @ViewById(R.id.show_image)
    TextView showImage;
    @Bean
    IslandInformationData data;

    private int mIslandID = -1;
    private String mLongitude;//东经
    private String mLatitude;//北纬
    private String[] arrayUrl;//海岛图片URL数组
    private String imageUrl;


    @AfterViews
    public void afterView() {
        mIslandID = getIntent().getIntExtra("islandID", -1);
        getIslandDetail(mIslandID);
    }


    @Click({R.id.back, R.id.location})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.location:
                Intent intent = new Intent(this, HomeActivity_.class);
                intent.putExtra("longitude", data.getEl());
                intent.putExtra("latitude", data.getNl());
                intent.putExtra("goToIslandMapFragment", true);
                startActivity(intent);
                break;
        }
    }

    /**
     * UI操作
     *
     * @param data
     */
    @UiThread
    void updateUi(IslandInformationData data) {
        this.data = data;
        title.setText("海岛详情");
        name.setText(data.getName());//海盗名
        mLongitude = getCoordinates(data.getEl());//东经
        mLatitude = getCoordinates(data.getNl());//北纬
        coordinates.setText(mLongitude + "E, " + mLatitude + "N");//经纬度
        dist.setText(data.getProvince() + data.getCity());//所属行政区域
        if (data.isReside()) {//是否有居民
            reside.setText("有居民");
        } else {
            reside.setText("无居民");
        }
        if (data.getStatus() != "") {//是否开发
            develop.setText(data.getStatus());
        } else {
            develop.setText("未开发");
        }
        usg.setText(data.getUsg());//用途
        details.setText(data.getRes());//详情介绍
        if (data.getImgs().contains(",")) {
            showImage.setVisibility(View.VISIBLE);
            arrayUrl = data.getImgs().replace("[", "").replace("]", "").split(",");
            for (int i = 0; i < arrayUrl.length; i++) {
                createImage(arrayUrl[i].replace("\"", ""), i + 1);
            }
        } else {
            showImage.setVisibility(View.VISIBLE);
            imageUrl = data.getImgUrl();
            createImage(imageUrl, 1);
        }
    }

    /**
     * 创建图片
     *
     * @param imageUrl 图片URL
     */
    private void createImage(String imageUrl, int count) {
        String url = URL.HTTP + imageUrl;
        //图片属性
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                ScreenUtils.dip2px(this, 150));
        imageLayoutParams.setMargins(10, 10, 10, 10);
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ll_IslandIimages.addView(imageView, imageLayoutParams);
        //图标题属性
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView text = new TextView(this);
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setText(data.getName() + "-图(" + count + ")");
        ll_IslandIimages.addView(text, textLayoutParams);
        ImageLoader.getInstance().displayImage(url, imageView, displayImageOptions);
//        String path = ImageLoader.getInstance().getDiscCache().get(url).getPath();
    }

    /**
     * 坐标转换
     * <p>
     * 转换据成坐标(Degrees,minutes,seconds)(如：205°23'44.1")。
     * 步骤如下：
     * 1， 直接读取"度"：205
     * 2，(205.395583333332-205)*60=23.734999999920 得到"分"：23
     *
     * @param coordinates 坐标
     * @return String
     */
    private String getCoordinates(String coordinates) {
        String[] arr;//小数分为两部分
        String coor1, coor2;//coor1为°，coor2为′
        arr = coordinates.split("[.]");
        coor1 = arr[0] + "°";
        coor2 = String.valueOf(Math.round(StringUtil.str2Double("0." + arr[1]) * 60) + "′");
        return coor1 + coor2;
    }

    /**
     * 获取海岛详情页面
     *
     * @param islandID
     */
    private void getIslandDetail(int islandID) {
        showLoading();
        PostParams params = new PostParams();
        params.put("islandId", String.valueOf(islandID));
        Call<IslandInformationData> mCall = mGsonClient.getIslandDetail(params);
        mCall.enqueue(new Callback<IslandInformationData>() {
            @Override
            public void onResponse(Response<IslandInformationData> response, Retrofit retrofit) {
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
