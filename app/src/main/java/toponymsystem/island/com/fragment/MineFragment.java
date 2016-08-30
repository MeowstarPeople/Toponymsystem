package toponymsystem.island.com.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.math.BigDecimal;

import toponymsystem.island.com.BuildConfig;
import toponymsystem.island.com.R;
import toponymsystem.island.com.activity.AboutActivity_;
import toponymsystem.island.com.activity.LoginActivity_;
import toponymsystem.island.com.activity.PersonInformationActivity_;
import toponymsystem.island.com.activity.SuggestionActivity_;
import toponymsystem.island.com.utils.SharePreferenceUtil;


/**
 * Created by CuiXiaoYong E-mial:hellocui@aliyun.com
 * on 2016/3/27
 */
@EFragment(R.layout.fragment_mine)
public class MineFragment extends BaseFragment {

    @ViewById(R.id.nickname)
    TextView nickname;
    @ViewById(R.id.personal_head)
    ImageView personalHead;
    @ViewById(R.id.head)
    LinearLayout head;
    @ViewById(R.id.tv_version)
    TextView version;
    @ViewById(R.id.tv_cache)
    TextView cache;
    private String gigaByte;

    public MineFragment() {
    }

    @AfterViews
    void afterViews() {
        if (SharePreferenceUtil.getBoolean(getActivity(), "login")) {
            nickname.setText(SharePreferenceUtil.getString(getActivity(), "username"));
            personalHead.setImageResource(R.drawable.ic_default_man_photo);
            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), PersonInformationActivity_.class));
                }
            });
        } else {
        }
        uiThread();

    }

    @UiThread
    void uiThread() {
        getVersionName();
        getCacheSize();
    }

    /**
     * 获取缓存大小
     */
    private void getCacheSize() {
        File file = getActivity().getExternalCacheDir();
        File file2 = getActivity().getCacheDir();
        long size = 0;
        try {
            size = getFolderSize(file) + getFolderSize(file2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gigaByte = getFormatSize(size);
        cache.setText(gigaByte);
    }

    /**
     * 设置当前版本号
     */
    public void getVersionName() {
        String versionName = BuildConfig.VERSION_NAME;
//        int versionCode = BuildConfig.VERSION_CODE;
        version.setText("V" + versionName);
    }

    /**
     * 获取缓存文件大小
     * Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            // return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            // return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
            // .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            //BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            //return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
            //  .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 删除缓存
     *
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Background
    void doBackground() {

    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Click({R.id.head, R.id.rl_version, R.id.rl_suggest, R.id.rl_about, R.id.rl_remove})
    void onClick(View v) {
        switch (v.getId()) {
            /**
             * 登录跳转
             */
            case R.id.head:
                startActivity(new Intent(getActivity(), LoginActivity_.class));
                getActivity().finish();
                break;
            /**
             * 版本
             */
            case R.id.rl_version:

                break;
            /**
             * 意见
             */
            case R.id.rl_suggest:
                startActivity(new Intent(getActivity(), SuggestionActivity_.class));
                break;
            /**
             * 关于
             */
            case R.id.rl_about:
                startActivity(new Intent(getActivity(), AboutActivity_.class));
                break;
            /**
             * 清除缓存
             */
            case R.id.rl_remove:
                new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("确定要清理缓存？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = getActivity().getExternalCacheDir();
                                File file2 = getActivity().getCacheDir();
                                deleteDir(file);
                                deleteDir(file2);
                                cache.setText("0.00MB");
                                showToast("清理完成");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
                break;
        }
    }
}
