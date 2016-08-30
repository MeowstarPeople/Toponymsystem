package toponymsystem.island.com.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import toponymsystem.island.com.R;
import toponymsystem.island.com.activity.MoreRegulationsActivity_;
import toponymsystem.island.com.activity.RegulationContentActivity_;
import toponymsystem.island.com.adapter.DepartmentalRulesAdapter;
import toponymsystem.island.com.adapter.IslandOthersAdapter;
import toponymsystem.island.com.adapter.IslandProtectionAdapter;
import toponymsystem.island.com.adapter.IslandUsingAdapter;
import toponymsystem.island.com.adapter.LawDeclarationAdapter;
import toponymsystem.island.com.adapter.RegulationDocumentsAdapter;
import toponymsystem.island.com.iView.IRegulationView;
import toponymsystem.island.com.model.RegulationData;
import toponymsystem.island.com.presenter.RegulationPresenter;
import toponymsystem.island.com.rest.api.URL;
import toponymsystem.island.com.widget.ListViewForScrollView;

/**
 * Created by CuiXiaoYong
 * on 2016/4/15
 * E-Mail:hellocui@aliyun.com
 */
@EFragment(R.layout.fragment__regulation)
public class RegulationFragment extends BaseFragment implements IRegulationView {
    /**
     * 法律与申明总共条数
     */
    @ViewById(R.id.law_declaration_total)
    TextView lawDeclarationTotal;
    /**
     * 行政法规与国务院文件总共条数
     */
    @ViewById(R.id.regulations_documents_total)
    TextView regulationsDocumentsTotal;
    /**
     * 部门规章总共条数
     */
    @ViewById(R.id.departmental_rules_total)
    TextView departmentalRulesTotal;
    /**
     * 海岛使用类总共条数
     */
    @ViewById(R.id.island_protection_total)
    TextView islandProtectionTotal;
    /**
     * 海岛保护类总共条数
     */
    @ViewById(R.id.island_using_total)
    TextView islandUsingTotal;
    /**
     * 海岛其他类总共条数
     */
    @ViewById(R.id.island_others_total)
    TextView islandOthersTotal;
    @ViewById(R.id.lv_law_declaration)
    ListViewForScrollView lawDeclarationList;
    @ViewById(R.id.lv_regulations_documents)
    ListViewForScrollView regulationsDocuments;
    @ViewById(R.id.lv_departmental_rules)
    ListViewForScrollView departmentalRules;

    @ViewById(R.id.lv_island_using)
    ListViewForScrollView islandUsing;
    @ViewById(R.id.lv_island_protection)
    ListViewForScrollView islandProtection;
    @ViewById(R.id.lv_island_others)
    ListViewForScrollView islandOthers;
    RegulationPresenter presenter;

    @ViewById(R.id.rl_law_declaration)
    RelativeLayout rl_law_declaration;
    @ViewById(R.id.rl_regulations_documents)
    RelativeLayout rl_regulations_documents;
    @ViewById(R.id.rl_departmental_rules)
    RelativeLayout rl_departmental_rules;
    @ViewById(R.id.rl_island_using)
    RelativeLayout rl_island_using;
    @ViewById(R.id.rl_island_protection)
    RelativeLayout rl_island_protection;
    @ViewById(R.id.rl_island_others)
    RelativeLayout rl_island_others;
    private List<List<RegulationData>> mListData;

    @Click({R.id.rl_law_declaration, R.id.rl_regulations_documents, R.id.rl_departmental_rules,
            R.id.rl_island_using, R.id.rl_island_protection, R.id.rl_island_others})
    void onClick(View v) {
        Intent intent = new Intent(getActivity(), MoreRegulationsActivity_.class);
        switch (v.getId()) {
            case R.id.rl_law_declaration:
                intent.putExtra("regulationMoreUrl", mListData.get(0).get(0).getRegulationMoreLink());
                intent.putExtra("regulationTitle", "法律与申明");
                startActivity(intent);
                break;
            case R.id.rl_regulations_documents:
                intent.putExtra("regulationMoreUrl", mListData.get(1).get(0).getRegulationMoreLink());
                intent.putExtra("regulationTitle", "行政法规与国务院文件");
                startActivity(intent);
                break;
            case R.id.rl_departmental_rules:
                intent.putExtra("regulationMoreUrl", mListData.get(2).get(0).getRegulationMoreLink());
                intent.putExtra("regulationTitle", "部门规章");
                startActivity(intent);
                break;
            case R.id.rl_island_using:
                intent.putExtra("regulationMoreUrl", mListData.get(3).get(0).getRegulationMoreLink());
                intent.putExtra("regulationTitle", "海岛使用类");
                startActivity(intent);
                break;
            case R.id.rl_island_protection:
                intent.putExtra("regulationMoreUrl", mListData.get(4).get(0).getRegulationMoreLink());
                intent.putExtra("regulationTitle", "海岛保护类");
                startActivity(intent);
                break;
            case R.id.rl_island_others:
                intent.putExtra("regulationMoreUrl", mListData.get(5).get(0).getRegulationMoreLink());
                intent.putExtra("regulationTitle", "其他");
                startActivity(intent);
                break;
        }
    }

    @AfterViews
    void afterView() {
        presenter = new RegulationPresenter(this, appRequester);
        presenter.loadData(URL.ISLAND_REGULATION);
    }

    @Override
    public void updateNewsContentData(List<List<RegulationData>> data) {
        updateView(data);
    }

    /**
     * UI线程显示数据
     *
     * @param data presenter处理返回数据
     */
    @UiThread
    void updateView(List<List<RegulationData>> data) {
        mListData = data;
        updateLawDeclaration(data.get(0));
        updateRegulationsDocuments(data.get(1));
        updateDepartmentalRules(data.get(2));
        updateIslandUsing(data.get(3));
        updateIslandProtection(data.get(4));
        updateIslands(data.get(5));
    }

    /**
     * 更新法律与申明数据
     */
    private void updateLawDeclaration(final List<RegulationData> listData) {
        rl_law_declaration.setVisibility(View.VISIBLE);
        lawDeclarationTotal.setText(listData.get(0).getRegulationMoreTitle());
        LawDeclarationAdapter adapter = new LawDeclarationAdapter(getActivity(), listData);
        lawDeclarationList.setAdapter(adapter);
        lawDeclarationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RegulationContentActivity_.class);
                intent.putExtra("regulationContentUrl", listData.get(position).getRegulationLink());
                startActivity(intent);
            }
        });
    }

    /**
     * 更新行政法规与国务院文件数据
     */
    private void updateRegulationsDocuments(final List<RegulationData> listData) {
        rl_regulations_documents.setVisibility(View.VISIBLE);
        regulationsDocumentsTotal.setText(listData.get(0).getRegulationMoreTitle());
        RegulationDocumentsAdapter adapter = new RegulationDocumentsAdapter(getActivity(), listData);
        regulationsDocuments.setAdapter(adapter);
        regulationsDocuments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RegulationContentActivity_.class);
                intent.putExtra("regulationContentUrl", listData.get(position).getRegulationLink());
                startActivity(intent);
            }
        });
    }

    /**
     * 更新部门规章数据
     */
    private void updateDepartmentalRules(final List<RegulationData> listData) {
        rl_departmental_rules.setVisibility(View.VISIBLE);
        departmentalRulesTotal.setText(listData.get(0).getRegulationMoreTitle());
        DepartmentalRulesAdapter adapter = new DepartmentalRulesAdapter(getActivity(), listData);
        departmentalRules.setAdapter(adapter);
        departmentalRules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RegulationContentActivity_.class);
                intent.putExtra("regulationContentUrl", listData.get(position).getRegulationLink());
                startActivity(intent);
            }
        });

    }

    /**
     * 更新海岛使用类数据
     */
    private void updateIslandUsing(final List<RegulationData> listData) {
        rl_island_using.setVisibility(View.VISIBLE);
        islandUsingTotal.setText(listData.get(0).getRegulationMoreTitle());
        IslandUsingAdapter adapter = new IslandUsingAdapter(getActivity(), listData);
        islandUsing.setAdapter(adapter);
        islandUsing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RegulationContentActivity_.class);
                intent.putExtra("regulationContentUrl", listData.get(position).getRegulationLink());
                startActivity(intent);
            }
        });

    }

    /**
     * 更新海岛保护类数据
     */
    private void updateIslandProtection(final List<RegulationData> listData) {
        rl_island_protection.setVisibility(View.VISIBLE);
        islandProtectionTotal.setText(listData.get(0).getRegulationMoreTitle());
        IslandProtectionAdapter adapter = new IslandProtectionAdapter(getActivity(), listData);
        islandProtection.setAdapter(adapter);
        islandProtection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RegulationContentActivity_.class);
                intent.putExtra("regulationContentUrl", listData.get(position).getRegulationLink());
                startActivity(intent);
            }
        });
    }

    /**
     * 更新海岛其他类数据
     */
    private void updateIslands(final List<RegulationData> listData) {
        rl_island_others.setVisibility(View.VISIBLE);
        islandOthersTotal.setText(listData.get(0).getRegulationMoreTitle());
        IslandOthersAdapter adapter = new IslandOthersAdapter(getActivity(), listData);
        islandOthers.setAdapter(adapter);
        islandOthers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RegulationContentActivity_.class);
                intent.putExtra("regulationContentUrl", listData.get(position).getRegulationLink());
                startActivity(intent);
            }
        });

    }

    @Override
    public void showLoadingDialog() {
        showLoading();
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoading();
    }
}
