package toponymsystem.island.com.widget.cityView;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import toponymsystem.island.com.R;
import toponymsystem.island.com.iView.IStatisticsView;
import toponymsystem.island.com.widget.cityView.adapters.ArrayWheelAdapter;
import toponymsystem.island.com.widget.cityView.model.CityModel;
import toponymsystem.island.com.widget.cityView.model.DistrictModel;
import toponymsystem.island.com.widget.cityView.model.ProvinceModel;
import toponymsystem.island.com.widget.cityView.widget.OnWheelChangedListener;
import toponymsystem.island.com.widget.cityView.widget.WheelView;


public class RegionDialog extends Dialog implements OnClickListener, OnWheelChangedListener {
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private TextView mBtnConfirm;
    private TextView mBtnCancel;
    protected String[] mProvinceData;
    protected Map<String, String[]> mCityDataMap = new HashMap<>();
    protected Map<String, String[]> mDistrictDataMap = new HashMap<>();
    protected Map<String, String> mZipcodeDataMap = new HashMap<>();
    protected String mCurrentProvinceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName = "";
    protected String mCurrentZipCode = "";
    //    private StatisticsFragment_ modificationInfoActivity;
    private IStatisticsView iView;

    public RegionDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.dialog_region_main);
        setUpViews();
        setUpListener();
        setUpData();
//        modificationInfoActivity = (StatisticsFragment_) context;
    }

    public RegionDialog(Context context, int theme, IStatisticsView iView) {
        super(context, theme);
        setContentView(R.layout.dialog_region_main);
        setUpViews();
        setUpListener();
        setUpData();
        this.iView = iView;
    }

    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        mBtnConfirm = (TextView) findViewById(R.id.btn_confirm);
        mBtnCancel = (TextView) findViewById(R.id.btn_cancel);
    }

    private void setUpListener() {
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceData();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(getContext(), mProvinceData));
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDataMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDataMap.get(mCurrentDistrictName);
        }
    }


    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCityDataMap.get(mCurrentProvinceName)[pCurrent];
        String[] areas = mDistrictDataMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        ArrayWheelAdapter<String> arrayWheelAdapter = new ArrayWheelAdapter<String>(getContext(), areas);
        mViewDistrict.setViewAdapter(arrayWheelAdapter);
        mViewDistrict.setCurrentItem(0);
    }


    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProvinceName = mProvinceData[pCurrent];
        String[] cities = mCityDataMap.get(mCurrentProvinceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(getContext(), cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                int newValue = mViewDistrict.getCurrentItem();
                mCurrentDistrictName = mDistrictDataMap.get(mCurrentCityName)[newValue];
                mCurrentZipCode = mZipcodeDataMap.get(mCurrentDistrictName);
                iView.getDistrictData(mCurrentProvinceName, mCurrentCityName, mCurrentDistrictName);
                //  modificationInfoActivity.setResult(mCurrentProvinceName, mCurrentCityName, mCurrentDistrictName, mCurrentZipCode);
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    protected void initProvinceData() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getContext().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();

            provinceList = handler.getDataList();

            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProvinceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            mProvinceData = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {

                mProvinceData[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {

                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {

                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());

                        mZipcodeDataMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }

                    mDistrictDataMap.put(cityNames[j], distrinctNameArray);
                }

                mCityDataMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }
}
