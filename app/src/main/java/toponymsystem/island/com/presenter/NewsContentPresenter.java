package toponymsystem.island.com.presenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import toponymsystem.island.com.iView.INewsContentView;
import toponymsystem.island.com.model.NewsContentData;
import toponymsystem.island.com.rest.api.CallbackListener;
import toponymsystem.island.com.rest.api.requester.AppRequester;

/**
 * Created by CuiXiaoYong
 * on 2016/4/18
 * E-Mail:hellocui@aliyun.com
 */
public class NewsContentPresenter {

    private INewsContentView iView;
    private AppRequester appRequester;
    private NewsContentData contentData;

    public NewsContentPresenter(INewsContentView view, AppRequester appRequester) {
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
                iView.updateNewsContentData(contentData);
                iView.dismissLoadingDialog();
            }

            @Override
            public void onFailure(String errorEvent) {
                iView.dismissLoadingDialog();
            }
        });
    }

    private void parseHtmlData(String htmlResult) {
        List<String> tempList = new ArrayList<>();
        Document doc = Jsoup.parse(htmlResult);
        contentData = new NewsContentData();
        StringBuilder sb = new StringBuilder();
        List<String> listImageLink = new ArrayList<>();
        List<String> listImageTitle = new ArrayList<>();
        Elements ele_date = doc.getElementsByClass("sourse");//获取时间
        contentData.setDate(ele_date.text());
        Elements ele_source = doc.getElementsByClass("time");//获取来源
        contentData.setSource(ele_source.text());
        Element haiDaoContent = doc.getElementById("aa");
        try {//获取图片地址
            Elements ele_img = haiDaoContent.getElementsByTag("img");
            for (int i = 0; i < ele_img.size(); i++) {
                listImageLink.add(ele_img.get(i).attr("src"));
            }
            contentData.setImageLink(listImageLink);
            //获取图片标题
            Elements ele_ImgTitle;
            Elements ele_ImgTitle1 = haiDaoContent.getElementsByAttributeValueMatching("align", "center");
            Elements ele_ImgTitle2 = haiDaoContent.getElementsByAttribute("style");
            if (ele_ImgTitle1.size() == 0) {
                ele_ImgTitle = ele_ImgTitle2;
            } else {
                ele_ImgTitle = ele_ImgTitle1;
            }
            for (int i = 0; i < ele_ImgTitle.size(); i++) {
                tempList.add(ele_ImgTitle.get(i).text());
                String url = ele_ImgTitle.get(i).getElementsByTag("img").attr("src");//获取链接，目的去除
                if (url.contains(".jpg")) {
                    continue;
                }
                int length = ele_ImgTitle.get(i).text().length();//获取文字目的去除 &nbsp;
                if (length == 1) {
                    continue;
                }
                listImageTitle.add(ele_ImgTitle.get(i).text());
            }
            contentData.setImageTitle(listImageTitle);
        } catch (Exception e) {
            e.getStackTrace();
        }
        //获取新闻内容
        Elements ele_Content = haiDaoContent.getElementsByTag("p");
        outer:
        for (int i = 0; i < ele_Content.size(); i++) {
            Element content_ele = ele_Content.get(i);
            inner:
            for (int j = 0; j < tempList.size(); j++) {
                if (content_ele.text().equals(tempList.get(j))) {
                    continue outer;
                }
            }
            if (!content_ele.hasAttr("style"))
                sb.append(content_ele.text() + "\n");
        }
        tempList.clear();
        contentData.setContent(sb.toString());
        sb.setLength(0);//清空sb
    }
}
