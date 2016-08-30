package toponymsystem.island.com.model;

import org.androidannotations.annotations.EBean;

import toponymsystem.island.com.rest.Dto.Dto;

/**
 * Created by CuiXiaoYong
 * on 2016/4/9
 * E-Mail:hellocui@aliyun.com
 */
@EBean
public class SlideData extends Dto {
    private int id;
    private String imgUrl;
    private int islandId;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIslandId() {
        return islandId;
    }

    public void setIslandId(int islandId) {
        this.islandId = islandId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
