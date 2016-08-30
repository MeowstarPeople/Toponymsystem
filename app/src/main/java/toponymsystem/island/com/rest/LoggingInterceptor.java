package toponymsystem.island.com.rest;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;

/**
 * Created by CuiXiaoYong
 * on 2016/3/17.
 */

/**
 * 创建请求拦截器，打印请求以及响应数据
 */
public class LoggingInterceptor implements Interceptor {
    private String requestParams = null;
    private String requestHeaders = null;
    private String responseHeadersAndParams = null;
    private String responseData = null;
    private long timeStart = 0;
    private long timeEnd = 0;
    private Request request;
    private Response response;

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {

        request = chain.request();

        timeStart = System.nanoTime();
        requestHeaders = String.format("(headers) %s", request.headers()).replace("\n", "");
        if (request.method().compareToIgnoreCase("post") == 0) {
            requestParams = bodyToString(request);
        }
        response = chain.proceed(request);
        timeEnd = System.nanoTime();

        responseHeadersAndParams = String.format("Request:%s %s in %.1fms",
                response.request().url(),
                requestHeaders + " (params) " + requestParams,
                (timeEnd - timeStart) / 1e6d);
        responseData = response.body().string();

        Log.d("TAG", responseHeadersAndParams + "\n" + "Response:" + responseData);

        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), responseData))
                .build();
    }

    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}
