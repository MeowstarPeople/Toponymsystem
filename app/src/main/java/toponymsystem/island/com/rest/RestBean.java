package toponymsystem.island.com.rest;


import toponymsystem.island.com.rest.Dto.Dto;

public class RestBean<T> extends Dto {

    private static final long serialVersionUID = 7647095138431194179L;

    private T datas;

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }
}
