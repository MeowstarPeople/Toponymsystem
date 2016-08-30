package toponymsystem.island.com.iView;

import java.util.List;

import toponymsystem.island.com.model.RegulationData;

/**
 * Created by CuiXiaoYong
 * on 2016/4/18
 * E-Mail:hellocui@aliyun.com
 */
public interface IRegulationView {
    /**
     * 加载政策法规
     *
     * @param data 加载政策数据
     */
    void updateNewsContentData(List<List<RegulationData>> data);

    /**
     * 显示进度对话框
     */
    void showLoadingDialog();

    /**
     * 隐藏进度对话框
     */
    void dismissLoadingDialog();
}
