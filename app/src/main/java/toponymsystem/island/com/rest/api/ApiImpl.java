package toponymsystem.island.com.rest.api;


import java.io.IOException;
import java.util.Map;

/**
 * Api实现类
 *
 * @author CuiXiaoYong
 * @version 1.0
 * @date 16/3/21
 */
public class ApiImpl implements Api {

    private HttpRequest httpRequest;

    public ApiImpl() {
        httpRequest = HttpRequest.getInstance();
    }

    @Override
    public String request(Map<String, String> map, String Url) {
        try {
            return httpRequest.postHandle(map, Url);
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    public String request(String Url) {
        try {
            return httpRequest.postHandle(Url);
        } catch (IOException e) {
            return e.getMessage();
        }
    }


}
