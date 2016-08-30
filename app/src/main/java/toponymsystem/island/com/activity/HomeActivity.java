package toponymsystem.island.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Stack;

import toponymsystem.island.com.R;
import toponymsystem.island.com.fragment.IndexFragment_;
import toponymsystem.island.com.fragment.InformationFragment_;
import toponymsystem.island.com.fragment.IslandMapFragment_;
import toponymsystem.island.com.fragment.MineFragment_;
import toponymsystem.island.com.fragment.StatisticsFragment_;
import toponymsystem.island.com.service.ImportDBService;
import toponymsystem.island.com.utils.NetworkStatusUtil;

/**
 * Created by CuiXiaoYong
 * E-mail:hellocui@aliyun.com
 * on 2016/3/23
 */
@EActivity(R.layout.activity_index)
public class HomeActivity extends BaseActivity {
    @ViewById(R.id.tabhost)
    FragmentTabHost mTabHost;
    private long exitTime = 0;
    static final int TAB_COUNT = 5;
    private Stack<Integer> mFragmentIdStack = new Stack<>();
    private Bundle[] mBundles = new Bundle[TAB_COUNT];
    static final Class[] clazzes = new Class[]{
            IndexFragment_.class,
            IslandMapFragment_.class,
            InformationFragment_.class,
            StatisticsFragment_.class,
            MineFragment_.class
    };
    static final int[] drawableIds = new int[]{
            R.drawable.main_page_home,
            R.drawable.main_page_map,
            R.drawable.main_page_news,
            R.drawable.main_page_name,
            R.drawable.main_page_mine
    };
    static final int[] tabStrings = {
            R.string.tab_string1,
            R.string.tab_string2,
            R.string.tab_string5,
            R.string.tab_string3,
            R.string.tab_string4
    };

    private String mRegister = "r";
    private String mLogin = "l";

    @AfterViews
    void afterView() {
        checkConnection();
        startService(new Intent(this, ImportDBService.class));
        initFragments();
        if (getIntent().getStringExtra("register") != null) {
            mRegister = getIntent().getStringExtra("register");
            if (mRegister.equals("register")) {
                mTabHost.setCurrentTab(4);
                mFragmentIdStack.push(4);
            }
        } else if (getIntent().getStringExtra("login") != null) {
            mLogin = getIntent().getStringExtra("login");
            if (mLogin.equals("login")) {
                mTabHost.setCurrentTab(4);
                mFragmentIdStack.push(4);
            }
        } else {
            mTabHost.setCurrentTab(0);
            mFragmentIdStack.push(0);
        }
        if (getIntent().getBooleanExtra("goToIslandMapFragment", false)) {
//            ClassEvent eventData = new ClassEvent();
//            eventData.setLatitude(getIntent().getStringExtra("latitude"));
//            eventData.setLongitude(getIntent().getStringExtra("longitude"));
//            EventBus.getDefault().post(eventData);//发送事件
            mTabHost.setCurrentTab(1);
            mFragmentIdStack.push(1);
        }
    }

    /**
     * 检查设备网络状态
     */
    private boolean checkConnection() {
        boolean value = NetworkStatusUtil.isAvailable();
        if (!value) {
            showToast("当前设备无网络");
        }
        return value;
    }

    private void initFragments() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);//消除分割线
        for (int i = 0; i < TAB_COUNT; i++) {
            String str = getResources().getString(tabStrings[i]);
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(str);
//            if (i == 2) {
//                ImageView imageView = new ImageView(this);
//                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));
//                imageView.setImageResource(R.drawable.tab11);
//                tabSpec.setIndicator(imageView);
//            } else {
//            }
            View view = getLayoutInflater().inflate(R.layout.layout_main_page_tab, null);
            ((TextView) view.findViewById(R.id.text)).setText(str);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(drawableIds[i]);
            tabSpec.setIndicator(view);
            mBundles[i] = new Bundle();
            mTabHost.addTab(tabSpec, clazzes[i], mBundles[i]);
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                mFragmentIdStack.remove((Integer) mTabHost.getCurrentTab());
                mFragmentIdStack.push(mTabHost.getCurrentTab());
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出海岛地名");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
