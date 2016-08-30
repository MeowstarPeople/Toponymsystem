package toponymsystem.island.com.presenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import toponymsystem.island.com.iView.IRegulationView;
import toponymsystem.island.com.model.RegulationData;
import toponymsystem.island.com.rest.api.CallbackListener;
import toponymsystem.island.com.rest.api.requester.AppRequester;

/**
 * Created by CuiXiaoYong
 * on 2016/4/18
 * E-Mail:hellocui@aliyun.com
 */
public class RegulationPresenter {

    private IRegulationView iView;
    private AppRequester appRequester;
    private static List<List<RegulationData>> mListData;
    private RegulationData regulationItem;
    private String mMoreTitle;
    private String mMoreLink;

    static {
        mListData = new ArrayList<>();
    }

    public RegulationPresenter(IRegulationView view, AppRequester appRequester) {
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
                iView.updateNewsContentData(mListData);
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
        Elements ele_island_regulation = doc.getElementsByClass("guli");
        for (int i = 0; i < ele_island_regulation.size(); i++) {
            if (i < 3) {
                List<RegulationData> lawItemsList = new ArrayList<>();
                Element unit_ele = ele_island_regulation.get(i);
                Elements h_ele = unit_ele.getElementsByTag("dt");
                Elements moreLink = unit_ele.getElementsByClass("lbg_moves_c");
                //各分类item总数及全部的链接
                mMoreTitle = moreLink.text();
                mMoreLink = moreLink.select("a").attr("href");
                for (int j = 0; j < h_ele.size(); j++) {
                    regulationItem = new RegulationData();
                    if (j == 0) {//为了避免重复添加，只添加一次
                        regulationItem.setRegulationMoreTitle(mMoreTitle);
                        regulationItem.setRegulationMoreLink(mMoreLink);
                    }
                    Element a_ele = h_ele.get(j).child(0);
                    //政策具体内容标题
                    regulationItem.setRegulationTitle(a_ele.text());
                    //政策具体内容链接
                    regulationItem.setRegulationLink(a_ele.attr("href"));
                    lawItemsList.add(regulationItem);
                }
                mListData.add(lawItemsList);
            } else {
                /**
                 * 后三个为选项卡解析
                 */
                Elements ele_haidaolaw1 = doc.getElementsByClass("TabbedPanelsContent");
                for (int i1 = 0; i1 < ele_haidaolaw1.size(); i1++) {
                    List<RegulationData> lawItemsList = new ArrayList<>();
                    Element unit_ele = ele_haidaolaw1.get(i1);
                    Elements h_ele = unit_ele.getElementsByTag("dt");
                    Elements ele_moreLink = unit_ele.getElementsByClass("lbg_moves_c");
                    //各分类item总数及全部的链接
                    mMoreTitle = ele_moreLink.text();
                    mMoreLink = ele_moreLink.select("a").attr("href");
                    for (int j = 0; j < h_ele.size(); j++) {
                        regulationItem = new RegulationData();
                        regulationItem.setRegulationMoreTitle(mMoreTitle);
                        regulationItem.setRegulationMoreLink(mMoreLink);
                        Element a_ele = h_ele.get(j);
                        Element c_a_ele = a_ele.child(0);
                        //政策具体内容标题
                        regulationItem.setRegulationTitle(c_a_ele.text());
                        //政策具体内容链接
                        regulationItem.setRegulationLink(c_a_ele.attr("href"));
                        lawItemsList.add(regulationItem);
                    }
                    mListData.add(lawItemsList);
                }
            }
        }
    }
}
