package toponymsystem.island.com.presenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import toponymsystem.island.com.iView.IRegulationContentView;
import toponymsystem.island.com.model.RegulationDetailsData;
import toponymsystem.island.com.rest.api.CallbackListener;
import toponymsystem.island.com.rest.api.requester.AppRequester;

/**
 * Created by CuiXiaoYong
 * on 2016/4/18
 * E-Mail:hellocui@aliyun.com
 */
public class RegulationContentPresenter {

    private IRegulationContentView iView;
    private AppRequester appRequester;
    private static RegulationDetailsData mRegulationContent;

    static {
        mRegulationContent = new RegulationDetailsData();
    }

    public RegulationContentPresenter(IRegulationContentView view, AppRequester appRequester) {
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
                parseHtmlData(String.valueOf(data));
                iView.updateRegulationContentData(mRegulationContent);
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
        StringBuilder sb = new StringBuilder();
        String mContent;

        //标题
        Elements ele_Title = doc.getElementsByClass("title_haidao");
        mRegulationContent.setTitle(ele_Title.text());
        //日期
        Elements ele_Date = doc.getElementsByClass("sourse");
        mRegulationContent.setDate(ele_Date.text());
        //内容
        Element ele_law_content = doc.getElementById("aa");
        Elements ele_Content = ele_law_content.getElementsByTag("p");
        for (int i = 0; i < ele_Content.size(); i++) {
            Element p_content = ele_Content.get(i);
            mContent = p_content.text();
            if (!p_content.getElementsByTag("a").hasAttr("href"))
                sb.append(mContent + "\n");
        }
        mRegulationContent.setContent(sb.toString());
    }
}
