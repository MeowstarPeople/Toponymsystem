package toponymsystem.island.com.rest.Dto;

import com.google.gson.Gson;

import java.util.HashMap;

public class PostParams extends HashMap <String ,String >{

    private static final long serialVersionUID = -3918611306392239968L;

    public PostParams() {
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

