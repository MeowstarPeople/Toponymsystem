package toponymsystem.island.com.model;

import java.util.List;

import toponymsystem.island.com.rest.Dto.Dto;

/**
 * Created by CuiXiaoYong
 * on 2016/4/19
 * E-Mail:hellocui@aliyun.com
 */
public class RegulationData extends Dto {

    public String regulationTitle;
    public String regulationLink;
    public String regulationMoreLink;
    public String regulationMoreTitle;

    public String getRegulationTitle() {
        return regulationTitle;
    }

    public void setRegulationTitle(String regulationTitle) {
        this.regulationTitle = regulationTitle;
    }

    public String getRegulationMoreTitle() {
        return regulationMoreTitle;
    }

    public void setRegulationMoreTitle(String regulationMoreTitle) {
        this.regulationMoreTitle = regulationMoreTitle;
    }

    public String getRegulationMoreLink() {
        return regulationMoreLink;
    }

    public void setRegulationMoreLink(String regulationMoreLink) {
        this.regulationMoreLink = regulationMoreLink;
    }

    public String getRegulationLink() {
        return regulationLink;
    }

    public void setRegulationLink(String regulationLink) {
        this.regulationLink = regulationLink;
    }
}
