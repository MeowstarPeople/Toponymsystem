package toponymsystem.island.com.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.RadioGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import toponymsystem.island.com.R;

/**
 * Created by CuiXiaoYong E-mial:hellocui@aliyun.com
 * on 2016/3/27
 */
@EFragment(R.layout.fragment_information)
public class InformationFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @ViewById(R.id.information_radio_group)
    RadioGroup mRadioGroup;

    private IslandNewsFragment_ mFragmentNews;
    private RegulationFragment_ mFragmentRegulation;
    private FragmentManager mFragmentManager;

    static {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getChildFragmentManager();
    }

    @AfterViews
    void afterView() {

        mRadioGroup.setOnCheckedChangeListener(this);
        if (mRadioGroup.getCheckedRadioButtonId() < 0) {
            mRadioGroup.check(R.id.news);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.news:
                mFragmentNews = new IslandNewsFragment_();
                mFragmentManager.beginTransaction().replace(R.id.information_holder, mFragmentNews).commit();
                break;
            case R.id.regulation:
                mFragmentRegulation = new RegulationFragment_();
                mFragmentManager.beginTransaction().replace(R.id.information_holder, mFragmentRegulation).commit();
                break;
            default:
                break;
        }
    }
}
