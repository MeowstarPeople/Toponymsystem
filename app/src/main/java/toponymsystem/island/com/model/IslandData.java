package toponymsystem.island.com.model;

import toponymsystem.island.com.rest.Dto.Dto;

/**
 * Created by CuiXiaoYong
 * on 2016/4/12
 * E-Mail:hellocui@aliyun.com
 */
public class IslandData extends Dto {
    private String city;
    private String dist;
    private String imgUrl;
    private int islandId;
    private String name;
    private String province;
    private boolean recommend;
    private boolean reside;
    private String usg;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUsg() {
        return usg;
    }

    public void setUsg(String usg) {
        this.usg = usg;
    }

    public boolean isReside() {
        return reside;
    }

    public void setReside(boolean reside) {
        this.reside = reside;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }
}
