package toponymsystem.island.com.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import toponymsystem.island.com.model.SlideData;

/**
 * Created by CuiXiaoYong
 * on 2016/4/10
 * E-Mail:hellocui@aliyun.com
 */
public class SideDataParser {

    private static ArrayList<SlideData> listData = null;
    private static SlideData sideData = null;

    public SideDataParser() {

    }

    static {
        listData = new ArrayList<>();
    }

    /**
     * 解析轮播JSON数据
     *
     * @param data
     * @return
     */
    public static ArrayList<SlideData> parseSideJsonData(String data) {

        try {
            JSONArray jsonArray = new JSONObject(data).getJSONArray("datas");
            for (int i = 0; i < jsonArray.length(); i++) {
                sideData = new SlideData();
                JSONObject jsonObj = ((JSONObject) jsonArray.opt(i));
                sideData.setId(jsonObj.getInt("id"));
                sideData.setImgUrl(jsonObj.getString("imgUrl"));
                sideData.setTitle(jsonObj.getString("title"));
                if (jsonObj.has("islandId")) {
                    sideData.setIslandId(jsonObj.getInt("islandId"));
                }
                listData.add(sideData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listData;
    }
}
