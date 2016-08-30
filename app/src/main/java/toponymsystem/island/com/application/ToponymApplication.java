package toponymsystem.island.com.application;

import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.androidannotations.annotations.EApplication;

import java.io.File;

import toponymsystem.island.com.service.LocationService;

public class ToponymApplication extends Application {
    public LocationService locationService;
    private static ToponymApplication sInstance;
    public static BMapManager mBMapManager = null;
    public static final String BAIDU_KEY = "ViaGHfH8n9slMgeUbtv0CZodIIulpy38";

    @Override
    public void onCreate() {
        super.onCreate();
        locationService = new LocationService(getApplicationContext());
        sInstance = this;
        initImageLoader(getContext());
        initEngineManager(this);
    }

    static public void finish() {
        android.os.Process.killProcess(Process.myPid());
        System.exit(0);
    }
    public static ToponymApplication getInstance() {
        return sInstance;
    }

    public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }
        if (!mBMapManager.init(BAIDU_KEY, new MyGeneralListener())) {
            Toast.makeText(ToponymApplication.getInstance().getApplicationContext(),
                    "BMapManager  初始化错误!", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(ToponymApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onGetPermissionState(int i) {

        }
    }

    /**
     * ImageLoader
     *
     * @param context
     */
    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(this);  //缓存文件夹路径
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default 可以自定义缓存路径
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    private Context getContext() {
        return getApplicationContext();
    }
}
