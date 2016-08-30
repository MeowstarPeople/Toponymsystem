package toponymsystem.island.com.iView;

import java.util.List;

import toponymsystem.island.com.model.IslandData;

/**
 * Created by CuiXiaoYong
 * on 2016/4/13
 * E-Mail:hellocui@aliyun.com
 */
public interface IRecommendView {
    void updateLYYLRecommendData(List<IslandData> childList);

    void updateGYRecommendData(List<IslandData> childList);

    void updateYYRecommendData(List<IslandData> childList);

    void updateGGFWRecommendData(List<IslandData> childList);

    void updateJTFWRecommendData(List<IslandData> childList);

    /**
     * 显示进度对话框
     */
    void showLoadingDialog();

    /**
     * 隐藏进度对话框
     */
    void dismissLoadingDialog();
}
