package toponymsystem.island.com.model;

import org.androidannotations.annotations.EBean;

import toponymsystem.island.com.rest.Dto.Dto;

/**
 * Created by CuiXiaoYong
 * on 2016/4/10
 * E-Mail:hellocui@aliyun.com
 */
@EBean
public class IslandInformationData<T> extends Dto {

    private String city;
    private String dist;
    private String el;
    private String imgUrl;
    private String imgs;
    private int islandId;
    private String name;
    private String nl;
    private String province;
    private boolean recommend;
    private String res;
    private boolean reside;
    private String status;
    private String text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isReside() {
        return reside;
    }

    public void setReside(boolean reside) {
        this.reside = reside;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
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

    public String getNl() {
        return nl;
    }

    public void setNl(String nl) {
        this.nl = nl;
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

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEl() {
        return el;
    }

    public void setEl(String el) {
        this.el = el;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }




}
