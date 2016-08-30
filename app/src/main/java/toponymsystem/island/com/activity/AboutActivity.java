package toponymsystem.island.com.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import toponymsystem.island.com.R;
import toponymsystem.island.com.iView.IAboutView;
import toponymsystem.island.com.model.VersionInfoData;
import toponymsystem.island.com.presenter.VersionUpdatePresenter;
import toponymsystem.island.com.rest.api.URL;
import toponymsystem.island.com.service.UpdateService;
import toponymsystem.island.com.utils.CallUtil;

/**
 * Created by Yong
 * on 2016/4/30.
 * E-Mail:hellocui@aliyun.com
 */
@EActivity(R.layout.activity_about)
public class AboutActivity extends BaseActivity implements IAboutView {
    @ViewById(R.id.title)
    TextView title;
    private Context context;

    private VersionUpdatePresenter presenter;

    @AfterViews
    void afterViews() {
        context = this;
        title.setText("关于海岛地名");
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Click({R.id.rl_about, R.id.rl_update, R.id.rl_phone, R.id.back})
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 关于
             */
            case R.id.rl_about:
                startActivity(new Intent(context, AboutUsActivity_.class));
                break;
            /**
             * 检查更新
             */
            case R.id.rl_update:
                presenter = new VersionUpdatePresenter(this, mGsonClient);
                presenter.loadData();
                break;
            /**
             * 联系电话
             */
            case R.id.rl_phone:
                new AlertDialog.Builder(context).setTitle("提示").setMessage("您要拨打联系电话吗？")
                        .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CallUtil.makeCall(context, "022-24308207");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();

                break;
            case R.id.back:
                finish();

                break;
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

    @Override
    public void showTheLastVersion() {
        showToast("当前版本为最新版本");
    }

    @Override
    public void updateData(final VersionInfoData data) {
        new AlertDialog.Builder(context).setTitle("提示").setMessage("您需要更新到最新版本吗？")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startService(new Intent(context, UpdateService.class)
                                .putExtra(UpdateService.FILE_PATH, URL.HTTP + data.getDownloadUrl()));
                        showToast("正在下载中");
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();
    }
}
