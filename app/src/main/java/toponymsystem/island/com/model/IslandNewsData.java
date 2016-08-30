package toponymsystem.island.com.model;

import toponymsystem.island.com.rest.Dto.Dto;

/**
 * Created by CuiXiaoYong
 * on 2016/4/18
 * E-Mail:hellocui@aliyun.com
 */
public class IslandNewsData extends Dto{

    private String title;
    private String newsContentLink;

    public String getNewsContentLink() {
        return newsContentLink;
    }

    public void setNewsContentLink(String newsContentLink) {
        this.newsContentLink = newsContentLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
