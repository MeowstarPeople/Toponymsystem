package toponymsystem.island.com.presenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import toponymsystem.island.com.iView.IMoreRegulationView;
import toponymsystem.island.com.model.RegulationData;
import toponymsystem.island.com.rest.api.CallbackListener;
import toponymsystem.island.com.rest.api.requester.AppRequester;

/**
 * Created by CuiXiaoYong
 * on 2016/4/21
 * E-Mail:hellocui@aliyun.com
 */
public class MoreRegulationsPresenter {

    private IMoreRegulationView iView;
    private AppRequester appRequester;
    private static List<RegulationData> mListData;
    private RegulationData regulationItem;

    static {
        mListData = new ArrayList<>();
    }

    public MoreRegulationsPresenter(IMoreRegulationView view, AppRequester appRequester) {
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
        appRequester.request(url, null);
        appRequester.setOnCallbackListener(new CallbackListener() {
            @Override
            public void onSuccess(Object data) {
                parseHtmlMulti(String.valueOf(data));
                iView.updateMoreRegulationsData(mListData);
                iView.dismissLoadingDialog();
            }

            @Override
            public void onFailure(String errorEvent) {
                iView.dismissLoadingDialog();
            }
        });
    }

    private void parseHtmlMulti(String strResult) {
        Document doc = Jsoup.parse(strResult);
        mListData = new ArrayList<>();
        Elements mContent = doc.getElementsByClass("haidaocontent");
        Elements units = mContent.select("tr");
        for (int i = 0; i < units.size(); i++) {
            regulationItem = new RegulationData();
            Element unit_ele = units.get(i);
            Element td_ele = unit_ele.getElementsByTag("td").get(0);
            Element ele_title = td_ele.child(0);
            //获取标题
            regulationItem.setRegulationTitle(ele_title.text());
            //获取链接
            regulationItem.setRegulationLink(ele_title.attr("href"));
            mListData.add(regulationItem);
        }
    }
}
