package toponymsystem.island.com.model;

import org.androidannotations.annotations.EBean;

import toponymsystem.island.com.rest.Dto.Dto;

/**
 * Created by CuiXiaoYong
 * on 2016/4/9
 * E-Mail:hellocui@aliyun.com
 */
@EBean
public class LoginData extends Dto {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
