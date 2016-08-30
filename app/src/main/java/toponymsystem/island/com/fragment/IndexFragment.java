package toponymsystem.island.com.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import toponymsystem.island.com.R;
import toponymsystem.island.com.activity.IslandInformationActivity_;
import toponymsystem.island.com.activity.IslandSearchActivity_;
import toponymsystem.island.com.activity.SlideNewsContentActivity_;
import toponymsystem.island.com.adapter.RecommendAdapter;
import toponymsystem.island.com.adapter.SlidePagerAdapter;
import toponymsystem.island.com.iView.IRecommendView;
import toponymsystem.island.com.iView.SideItemListener;
import toponymsystem.island.com.model.IslandData;
import toponymsystem.island.com.model.SlideData;
import toponymsystem.island.com.parser.SideDataParser;
import toponymsystem.island.com.presenter.RecommendPresenter;
import toponymsystem.island.com.rest.api.CallbackListener;
import toponymsystem.island.com.rest.api.URL;
import toponymsystem.island.com.widget.CustomGridView;

/**
 * Created by CuiXiaoYong E-mial:hellocui@aliyun.com
 * on 2016/3/27
 */
@EFragment(R.layout.fragment_index)
public class IndexFragment extends BaseFragment implements SideItemListener, IRecommendView, CompoundButton.OnCheckedChangeListener {
    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.viewPager)
    ViewPager viewPager;
    @ViewById(R.id.dotLayout)
    LinearLayout dotLayout;
    @ViewById(R.id.cb_tourist_recreation)
    CheckBox mIsTouristRecreation;
    @ViewById(R.id.gv_tourist_recreation)
    CustomGridView mTouristRecreationGridView;
    @ViewById(R.id.cb_industry)
    CheckBox mIsIndustry;
    @ViewById(R.id.gv_industry)
    CustomGridView mIndustryGridView;
    @ViewById(R.id.cb_fishery)
    CheckBox mIsFishery;
    @ViewById(R.id.gv_fishery)
    CustomGridView mFisheryGridView;
    @ViewById(R.id.cb_public_service)
    CheckBox mIsPublicService;
    @ViewById(R.id.gv_public_service)
    CustomGridView mPublicServiceGridView;
    @ViewById(R.id.cb_transportation)
    CheckBox mIsTransportation;
    @ViewById(R.id.gv_transportation)
    CustomGridView mTransportation;
    private Context context = null;
    private final static boolean isAutoPlay = true;  //自动轮播启用开关
    private static final List<ImageView> imageViewsList;  //放轮播图片的ImageView 的list
    private static final List<View> dotViewsList;  //放圆点的View的list
    private int currentItem = 0;  //当前轮播页
    //    private ScheduledExecutorService scheduledExecutorService; //定时任务
    private Timer timer;
    private Handler handler;
    private SlidePagerAdapter adapter;
    private ArrayList<SlideData> listData = null;//Side解析数据
    private ArrayList<String> slideUrlList = null;//Side图片URL
    private static List<List<IslandData>> mChildList;
    private RecommendPresenter presenter;


    static {
        mChildList = new ArrayList<>();
        imageViewsList = new ArrayList<>();
        dotViewsList = new ArrayList<>();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.e("Fragment", "onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("Fragment", "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @AfterViews
    void afterView() {
//        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        presenter = new RecommendPresenter(this, mGsonClient);
        presenter.loadData();
        context = getActivity();
        title.setText("首页");


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                viewPager.setCurrentItem(currentItem);
            }
        };
        initListener();
    }

    /**
     * 初始化监听事件
     */
    private void initListener() {
        mIsTouristRecreation.setOnCheckedChangeListener(this);
        mIsIndustry.setOnCheckedChangeListener(this);
        mIsFishery.setOnCheckedChangeListener(this);
        mIsPublicService.setOnCheckedChangeListener(this);
        mIsTransportation.setOnCheckedChangeListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Fragment", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Fragment", "onStart");
        timer = new Timer();
        getSideData();
        if (isAutoPlay) {
            startPlay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Fragment", "onResume");
    }

    /**
     * 获取首页轮播数据
     */
    private void getSideData() {
        appRequester.request(URL.SIDE_API);
        appRequester.setOnCallbackListener(new CallbackListener() {
            @Override
            public void onSuccess(Object data) {
                slideUrlList = new ArrayList<>();
                listData = SideDataParser.parseSideJsonData(data.toString());
                for (SlideData item : listData) {
                    slideUrlList.add(URL.HTTP + item.getImgUrl());
                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        initSlideView(getActivity());
                    }
                });
            }

            @Override
            public void onFailure(String errorEvent) {
                showToast(errorEvent);
            }
        });
    }

    /**
     * 初始化Side View等UI
     */
    private void initSlideView(Context context) {
        if (slideUrlList == null || slideUrlList.size() == 0) {
            return;
        }
        dotLayout.removeAllViews();
        // 热点个数与图片特殊相等
        for (int i = 0; i < slideUrlList.size(); i++) {
            ImageView view = new ImageView(context);
            view.setTag(slideUrlList.get(i));
//            if (i == 0) {//给一个默认图
//                view.setBackgroundResource(R.drawable.appmain_subject);
//            }
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(view);
            //位置点
            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
            //第一个设为选中
            if (i == 0) {
                (dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_focus);
            } else {
                (dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_blur);
            }
        }
        adapter = new SlidePagerAdapter(imageViewsList, displayImageOptions);
        viewPager.addOnPageChangeListener(new MyPageChangeListener());
        adapter.setOnItemClickListener(this);
        viewPager.setFocusable(true);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void updateLYYLRecommendData(final List<IslandData> childList) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getTouristRecreationRecommendData(childList);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 加载旅游娱乐用途数据
     *
     * @param list 数据
     */
    void getTouristRecreationRecommendData(final List<IslandData> list) {
        RecommendAdapter adapter = new RecommendAdapter(getActivity(), list, displayImageOptions);
        mTouristRecreationGridView.setAdapter(adapter);
        mTouristRecreationGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), IslandInformationActivity_.class);
                intent.putExtra("islandID", list.get(position).getIslandId());
                startActivity(intent);
            }
        });
    }


    @Override
    public void updateGYRecommendData(final List<IslandData> childList) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getIndustryRecommendData(childList);
                }
            });

        } catch (Exception e) {
        }
    }

    /**
     * 加载工业用途数据
     *
     * @param list 数据
     */
    void getIndustryRecommendData(final List<IslandData> list) {
        RecommendAdapter adapter = new RecommendAdapter(getActivity(), list, displayImageOptions);
        mIndustryGridView.setAdapter(adapter);
        mIndustryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), IslandInformationActivity_.class);
                intent.putExtra("islandID", list.get(position).getIslandId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void updateYYRecommendData(final List<IslandData> childList) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getFisheryRecommendData(childList);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 加载渔业用途数据
     *
     * @param list 数据
     */
    void getFisheryRecommendData(final List<IslandData> list) {
        RecommendAdapter adapter = new RecommendAdapter(getActivity(), list, displayImageOptions);
        mFisheryGridView.setAdapter(adapter);
        mFisheryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), IslandInformationActivity_.class);
                intent.putExtra("islandID", list.get(position).getIslandId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void updateGGFWRecommendData(final List<IslandData> childList) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getPublicServiceRecommendData(childList);
                }
            });
        } catch (Exception e) {
        }
    }

    /**
     * 加载公共服务用途数据
     *
     * @param list 数据
     */
    void getPublicServiceRecommendData(final List<IslandData> list) {
        RecommendAdapter adapter = new RecommendAdapter(getActivity(), list, displayImageOptions);
        mPublicServiceGridView.setAdapter(adapter);
        mPublicServiceGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), IslandInformationActivity_.class);
                intent.putExtra("islandID", list.get(position).getIslandId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void updateJTFWRecommendData(final List<IslandData> childList) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getTransportationServicesRecommendData(childList);
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    public void showLoadingDialog() {
        showLoading();
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoading();
    }

    /**
     * 加载交通服务用途数据
     *
     * @param list 数据
     */
    void getTransportationServicesRecommendData(final List<IslandData> list) {
        RecommendAdapter adapter = new RecommendAdapter(getActivity(), list, displayImageOptions);
        mTransportation.setAdapter(adapter);
        mTransportation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), IslandInformationActivity_.class);
                intent.putExtra("islandID", list.get(position).getIslandId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_tourist_recreation:
                if (isChecked) {
                    mTouristRecreationGridView.setVisibility(View.GONE);
                } else {
                    mTouristRecreationGridView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.cb_industry:
                if (isChecked) {
                    mIndustryGridView.setVisibility(View.GONE);
                } else {
                    mIndustryGridView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.cb_fishery:
                if (isChecked) {
                    mFisheryGridView.setVisibility(View.GONE);
                } else {
                    mFisheryGridView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.cb_public_service:
                if (isChecked) {
                    mPublicServiceGridView.setVisibility(View.GONE);
                } else {
                    mPublicServiceGridView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.cb_transportation:
                if (isChecked) {
                    mTransportation.setVisibility(View.GONE);
                } else {
                    mTransportation.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    /**
     * 跳转首页轮播详情页面
     *
     * @param position
     */
    @Override
    public void getPosition(int position) {
        if (listData.get(position).getIslandId() > 0) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), IslandInformationActivity_.class);
            intent.putExtra("islandID", listData.get(position).getIslandId());
            startActivity(intent);
        } else {
            Intent slideNewsIntent = new Intent(getActivity(), SlideNewsContentActivity_.class);
            slideNewsIntent.putExtra("newsId", listData.get(position).getId());
            startActivity(slideNewsIntent);
        }
    }


    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int postion) {
            switch (postion) {
                case 1:// 手势正在滑动
                    isAutoPlay = false;
                    break;
                case 2:// 滑动完毕
                    isAutoPlay = true;
                    break;
                case 0: //什么都没做
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageSelected(int pos) {
            currentItem = pos;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos) {
                    ((View) dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot_focus);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_blur);
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    /**
     * 开始轮播图切换
     */
    private void startPlay() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                synchronized (viewPager) {
                    if (imageViewsList.size() == 0) {
                        currentItem = 0;
                    } else {
                        currentItem = (currentItem + 1) % imageViewsList.size();
                        handler.obtainMessage().sendToTarget();
                    }
                }
            }
        };
        timer.schedule(task, 0, 3000);
//        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (viewPager) {
//                    currentItem = (currentItem + 1) % imageViewsList.size();
//                    handler.obtainMessage().sendToTarget();
//                }
//            }
//        }, 1, 3, TimeUnit.SECONDS);
    }

    @Click(R.id.edit_seek)
    void ocClick() {
        startActivity(new Intent(getActivity(), IslandSearchActivity_.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Fragment", "onPause");
        if (null != imageViewsList) {
            imageViewsList.clear();
        }

        if (null != dotViewsList) {
            dotViewsList.clear();
        }

        if (null != slideUrlList) {
            slideUrlList.clear();
        }
        if (null != listData) {
            listData.clear();
        }
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Fragment", "onStop");
        timer.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Fragment", "onDestroy");
        if (null != imageViewsList) {
            imageViewsList.clear();
        }

        if (null != dotViewsList) {
            dotViewsList.clear();
        }

        if (null != slideUrlList) {
            slideUrlList.clear();
        }
        if (null != listData) {
            listData.clear();
        }
        if (null != adapter) {
            adapter.notifyDataSetChanged();
        }
    }
}
