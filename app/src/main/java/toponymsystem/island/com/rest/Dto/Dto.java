package toponymsystem.island.com.rest.Dto;



import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/29.
 */
public class Dto implements Serializable {

    private static final long serialVersionUID = 8422225916501968784L;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
