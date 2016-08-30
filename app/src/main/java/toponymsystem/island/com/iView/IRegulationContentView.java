package toponymsystem.island.com.iView;

import toponymsystem.island.com.model.RegulationDetailsData;

/**
 * Created by CuiXiaoYong
 * on 2016/4/18
 * E-Mail:hellocui@aliyun.com
 */
public interface IRegulationContentView {
    /**
     * 加载政策法规
     *
     * @param data 加载政策数据
     */
    void updateRegulationContentData(RegulationDetailsData data);

    /**
     * 显示进度对话框
     */
    void showLoadingDialog();

    /**
     * 隐藏进度对话框
     */
    void dismissLoadingDialog();
}
