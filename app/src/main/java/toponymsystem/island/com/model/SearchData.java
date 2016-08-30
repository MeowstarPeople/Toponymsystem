package toponymsystem.island.com.model;

import org.androidannotations.annotations.EBean;

import toponymsystem.island.com.rest.Dto.Dto;

/**
 * Created by CuiXiaoYong
 * on 2016/4/12
 * E-Mail:hellocui@aliyun.com
 */
@EBean
public class SearchData<T> extends Dto {
    private int pageSize;
    private int total;
    private T datas;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
