package toponymsystem.island.com.presenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import toponymsystem.island.com.iView.IIslandNewsView;
import toponymsystem.island.com.model.IslandNewsData;
import toponymsystem.island.com.rest.api.CallbackListener;
import toponymsystem.island.com.rest.api.requester.AppRequester;

/**
 * Created by CuiXiaoYong
 * on 2016/4/18
 * E-Mail:hellocui@aliyun.com
 */
public class IslandNewsPresenter {

    private IIslandNewsView iView;
    private AppRequester appRequester;
    private List<IslandNewsData> dataList = new ArrayList<>();;


    public IslandNewsPresenter(IIslandNewsView view, AppRequester appRequester) {
        this.appRequester = appRequester;
        iView = view;
    }

    public void loadData(String url) {
        getData(url);
    }

    /**
     * 访问中国海岛网获取数据， 并解析
     *
     * @param url URL
     */
    private void getData(String url) {
        iView.showLoadingDialog();
        dataList.clear();
        appRequester.request(url, null);
        appRequester.setOnCallbackListener(new CallbackListener() {
            @Override
            public void onSuccess(Object data) {
                parseHtmlData(String.valueOf(data));
                iView.updateIIslandNewsData(dataList);
                iView.dismissLoadingDialog();
            }

            @Override
            public void onFailure(String errorEvent) {
                iView.dismissLoadingDialog();
            }
        });
    }

    private void parseHtmlData(String htmlResult) {

        Document doc = Jsoup.parse(htmlResult);
        Elements islandContent = doc.getElementsByClass("haidaocontent");
        Elements units = islandContent.select("tr");
        for (int i = 0; i < units.size(); i++) {
            IslandNewsData itemData = new IslandNewsData();
            Element unit_ele = units.get(i);
            Element h1_ele = unit_ele.getElementsByTag("td").get(0);
            Element h1_a_ele = h1_ele.child(0);
            itemData.setNewsContentLink(h1_a_ele.attr("href"));
            itemData.setTitle(h1_a_ele.text());
            dataList.add(itemData);
        }
    }
}
