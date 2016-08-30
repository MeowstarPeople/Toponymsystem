package toponymsystem.island.com.iView;

/**
 * Created by CuiXiaoYong
 * on 2016/4/18
 * E-Mail:hellocui@aliyun.com
 */
public interface IBarChartStatisticsView {
    /**
     * 加载 柱壮图数据
     *
     * @param transportationCount
     * @param publicService
     * @param industry
     * @param tourism
     * @param fisheries
     */
    void updateBarChartStatisticsData(int transportationCount, int publicService, int industry, int tourism, int fisheries);

    /**
     * 显示进度对话框
     */
    void showLoadingDialog();

    /**
     * 隐藏进度对话框
     */
    void dismissLoadingDialog();
}
