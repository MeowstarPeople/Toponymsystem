package toponymsystem.island.com.iView;

import java.util.List;

import toponymsystem.island.com.model.IslandNewsData;

/**
 * Created by CuiXiaoYong
 * on 2016/4/18
 * E-Mail:hellocui@aliyun.com
 */
public interface IIslandNewsView {
    /**
     * 加载新闻列表
     *
     * @param dataList 新闻列表数据
     */
    void updateIIslandNewsData(List<IslandNewsData> dataList);

    /**
     * 显示进度对话框
     */
    void showLoadingDialog();

    /**
     * 隐藏进度对话框
     */
    void dismissLoadingDialog();
}
