package toponymsystem.island.com.iView;

import java.util.List;

import toponymsystem.island.com.model.IslandData;

/**
 * Created by CuiXiaoYong
 * on 2016/4/18
 * E-Mail:hellocui@aliyun.com
 */
public interface ITableStatisticsView {
    /**
     * 加载统计列表
     *
     * @param dataList 统计列表数据
     */
    void updateTableStatisticsData(List<IslandData> dataList);

    /**
     * 显示进度对话框
     */
    void showLoadingDialog();

    /**
     * 隐藏进度对话框
     */
    void dismissLoadingDialog();
}
