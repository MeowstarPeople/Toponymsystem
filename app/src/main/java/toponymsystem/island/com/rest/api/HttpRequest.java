package toponymsystem.island.com.rest.api;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Http处理类
 *
 * @author CuiXiaoyong
 * @date 16/3/21
 */
public class HttpRequest {

    private final static String TAG = "HttpRequest";
    private final static String REQUEST_POST = "POST";
    private final static String REQUEST_GET = "GET";
    private final static String ENCODE_TYPE = "UTF-8";
    private final static int TIME_OUT = 10000;
    private static HttpRequest instance = null;

    private HttpRequest() {
    }

    public static HttpRequest getInstance() {
        if (instance == null) {
            instance = new HttpRequest();
        }
        return instance;
    }

    /**
     * 返回String结果集
     *
     * @param paramsMap 请求参数
     * @param Url       请求Url
     * @return
     * @throws IOException
     */
    public String postHandle(Map<String, String> paramsMap, String Url) throws IOException {
        HttpURLConnection connection = getConnection(Url);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        InputStream is = null;
        if (null != paramsMap && paramsMap.size() > 0) {
            String data = joinParams(paramsMap);
            // 打印出请求
            Log.i(TAG, "request: " + data);
            connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
            connection.connect();
            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());
            os.flush();
        }
        if (connection.getResponseCode() == 200) {
            try {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "GB2312"));
                String mReadLine = null;
                while ((mReadLine = br.readLine()) != null) {
                    sb.append(mReadLine);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        final String mResult = new String(sb);
        // 打印出结果
        Log.i(TAG, "response: " + mResult);
        return mResult;
    }

    /**
     * @param Url
     * @return
     * @throws IOException
     */
    public String postHandle(String Url) throws IOException {
        HttpURLConnection connection = getConnection(Url);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        InputStream is = null;
        if (connection.getResponseCode() == 200) {
            try {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String mReadLine = null;
                while ((mReadLine = br.readLine()) != null) {
                    sb.append(mReadLine);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        final String mResult = new String(sb);
        // 打印出结果
        Log.i(TAG, "response: " + mResult);
        return mResult;
    }

    /**
     * 设置请求属性，获取connection
     *
     * @param Url 请求地址
     * @return
     */
    private HttpURLConnection getConnection(String Url) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(Url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_GET);
            connection.setDoInput(true);
            connection.setDoOutput(false);//无参数的时候，true会导致连接失败
            connection.setUseCaches(false);
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Response-Type", "json");
            connection.setChunkedStreamingMode(0);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return connection;
    }


    /**
     * 拼接参数列表
     *
     * @param paramsMap 请求参数
     * @return
     */
    private String joinParams(Map<String, String> paramsMap) {
        if (null != paramsMap) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String key : paramsMap.keySet()) {
                stringBuilder.append(key);
                stringBuilder.append("=");
                try {
                    stringBuilder.append(URLEncoder.encode(paramsMap.get(key), ENCODE_TYPE));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } finally {
                }
                stringBuilder.append("&");
            }
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }
        return null;
    }
}
