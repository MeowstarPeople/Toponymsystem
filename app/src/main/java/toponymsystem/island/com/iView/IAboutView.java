package toponymsystem.island.com.iView;

import toponymsystem.island.com.model.VersionInfoData;

/**
 * Created by Yong
 * on 2016/4/30.
 * E-Mail:hellocui@aliyun.com
 */
public interface IAboutView {
    /**
     * 显示进度对话框
     */
    void showLoadingDialog();

    /**
     * 隐藏进度对话框
     */
    void dismissLoadingDialog();
    void showTheLastVersion();

    void updateData(VersionInfoData data);
}
