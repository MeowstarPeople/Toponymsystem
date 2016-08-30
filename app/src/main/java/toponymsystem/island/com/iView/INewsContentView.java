package toponymsystem.island.com.iView;

import toponymsystem.island.com.model.NewsContentData;

/**
 * Created by CuiXiaoYong
 * on 2016/4/18
 * E-Mail:hellocui@aliyun.com
 */
public interface INewsContentView {
    /**
     * 加载新闻
     *
     * @param data 新闻数据
     */
    void updateNewsContentData(NewsContentData data);

    /**
     * 显示进度对话框
     */
    void showLoadingDialog();

    /**
     * 隐藏进度对话框
     */
    void dismissLoadingDialog();
}
