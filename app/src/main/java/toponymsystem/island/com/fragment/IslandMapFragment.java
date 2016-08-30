package toponymsystem.island.com.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;

import toponymsystem.island.com.R;
import toponymsystem.island.com.activity.HomeActivity_;
import toponymsystem.island.com.activity.IslandInformationActivity_;
import toponymsystem.island.com.application.ToponymApplication;
import toponymsystem.island.com.mapapi.Cluster;
import toponymsystem.island.com.mapapi.MapUtils;
import toponymsystem.island.com.model.LatLonData;
import toponymsystem.island.com.service.LocationService;
import toponymsystem.island.com.utils.TransformLatLon;

/**
 * Created by CuiXiaoYong E-mial:hellocui@aliyun.com
 * on 2016/3/27
 */
@EFragment(R.layout.fragment_island_map)
public class IslandMapFragment extends Fragment {
    @ViewById(R.id.mapView)
    MapView mapView;
    @ViewById(R.id.title)
    TextView title;
    private static final int MAX_ZOOM = 12;//最大缩放等级
    private final String PACKAGE_NAME = "toponymsystem.island.com";//项目包名
    private final String DB_NAME = "island.db"; //保存的数据库文件名
    private final String DB_PATH = File.separator + "data" + Environment.getDataDirectory().getAbsolutePath()
            + File.separator + PACKAGE_NAME + File.separator;  //在手机里存放数据库的位置
    private MyLocationOverlay myLocationOverlay;
    private LocationService locationService;
    private SQLiteDatabase dataBase = null;
    private Cursor cursor = null;
    private ArrayList<OverlayItem> mMarkerList;
    private Cluster mCluster;
    private MyOverlay mOverlay;
    private Boolean isAverageCenter = false;
    private int mGridSize = 60;
    private double mDistance = 150000;
    private String longitudeFromActivity = null;
    private String latitudeFromActivity = null;
    private ArrayList<OverlayItem> result;

    public IslandMapFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        mMarkerList = new ArrayList<>();
    }

    @AfterViews
    public void afterView() {
        title.setText("海岛地图");
        HomeActivity_ activity = (HomeActivity_) getActivity();
        longitudeFromActivity = activity.getIntent().getStringExtra("longitude");
        latitudeFromActivity = activity.getIntent().getStringExtra("latitude");
        initBaiDuMap();
        doBackground();
        setMKMapViewListener();
    }

    /**
     * 开始线程启动定位以及标注为添加
     */
    @Background
    void doBackground() {
        initLocationOrientate();
        addMarkers();
    }

    /**
     * 生成海岛经纬度坐标，并设置OverlayItem
     */
    private void addMarkers() {

        double northLatitude, eastLongitude;
        String islandName;
        int mIslandId;
        LatLonData mLatLon;
        View view;
        TextView tv_name;
        Bitmap snapshot;
        Drawable drawable;
        GeoPoint geoPoint;
        OverlayItem overlayItem;
        dataBase = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);//打开数据库
        cursor = dataBase.query("information_db",
                new String[]{"island_id", "name", "eastlongitude", "northlongitude"}, null, null, null, null, null, null);
        /**获取本地数据库中海岛相关信息（标准名、ID、东经、北纬）*/
        while (cursor.moveToNext()) {
            islandName = cursor.getString(cursor.getColumnIndex("name"));
            mIslandId = cursor.getInt(cursor.getColumnIndex("island_id"));
            northLatitude = cursor.getDouble(cursor.getColumnIndex("northlongitude"));
            eastLongitude = cursor.getDouble(cursor.getColumnIndex("eastlongitude"));
            /**PS坐标转百度坐标*/
            mLatLon = TransformLatLon.transform(northLatitude, eastLongitude);
            geoPoint = new GeoPoint((int) (mLatLon.getLatitude() * 1e6), (int) (mLatLon.getLongitude() * 1e6));
            /**自定义标注物 这里是图标 加 文字说明形式*/
            view = LayoutInflater.from(getActivity()).inflate(R.layout.marker, null);
            tv_name = (TextView) view.findViewById(R.id.txtIslandName);
            tv_name.setText(islandName);//设置海岛名
            snapshot = MapUtils.convertViewToBitmap(view);
            drawable = new BitmapDrawable(snapshot);
            drawable.setLevel(1);
            /**设置OverlayItem*/
            overlayItem = new OverlayItem(geoPoint, null, null);
            overlayItem.setMarker(drawable);
            overlayItem.setTitle(mIslandId + "");
            mMarkerList.add(overlayItem);
        }
    }

    /**
     * 监听MapView的状态，有三个重要的回调函数
     */
    private void setMKMapViewListener() {
        MKMapViewListener mapViewListener = new MKMapViewListener() {

            @Override
            public void onMapMoveFinish() {
                /**判断当前地图的比例尺，如果是最大比例尺，就不聚合，直接显示屏幕经纬度范围内的覆盖物*/
                if (mapView.getZoomLevel() >= MAX_ZOOM) {
                    mOverlay.removeAll();
                    pinMarkers(refreshVersionClusterMarker(mMarkerList));
                } else {
                    /**根据屏幕内标注物创建聚合物*/
                    ArrayList<OverlayItem> clusters = mCluster.createCluster(refreshVersionClusterMarker(mMarkerList));
                    mOverlay.removeAll();
                    pinMarkers(clusters);
                }
            }

            @Override
            public void onMapAnimationFinish() {
                if (mapView.getZoomLevel() >= MAX_ZOOM) {
                    mOverlay.removeAll();
                    pinMarkers(refreshVersionClusterMarker(mMarkerList));
                } else {
                    ArrayList<OverlayItem> clusters = mCluster.createCluster(refreshVersionClusterMarker(mMarkerList));
                    mOverlay.removeAll();
                    pinMarkers(clusters);
                }
            }

            @Override
            public void onMapLoadFinish() {
            }


            @Override
            public void onClickMapPoi(MapPoi arg0) {
            }

            @Override
            public void onGetCurrentMap(Bitmap arg0) {
            }
        };
        /** 注册地图放大缩小监听事件*/
        mapView.regMapViewListener(ToponymApplication.mBMapManager, mapViewListener);
    }

    /**
     * 标注物显示，刷新MapView
     */
    private void pinMarkers(ArrayList<OverlayItem> list) {
        mOverlay.removeAll();
        for (int i = 0; i < list.size(); i++) {
            mOverlay.addItem(list.get(i));
        }
        mapView.refresh();
    }

    /**
     * 屏幕范围内覆盖物刷新方法，判断屏幕经纬度范围内标注物
     *
     * @param list
     * @return 返回当前屏幕内的所有标注物
     */
    private ArrayList<OverlayItem> refreshVersionClusterMarker(ArrayList<OverlayItem> list) {
        result = new ArrayList<>();
        GeoPoint mCenter = mapView.getMapCenter();
        int lat = mCenter.getLatitudeE6();
        int lng = mCenter.getLongitudeE6();
        int LatSpan = mapView.getLatitudeSpan();
        int LngSpan = mapView.getLongitudeSpan();
        int topLat = lat + LatSpan / 2;
        int bottomLat = lat - LatSpan / 2;
        int leftLng = lng - LngSpan / 2;
        int rightLng = lng + LngSpan / 2;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPoint().getLatitudeE6() > bottomLat && list.get(i).getPoint().getLatitudeE6() < topLat
                    && list.get(i).getPoint().getLongitudeE6() > leftLng && list.get(i).getPoint().getLongitudeE6() < rightLng) {
                result.add(list.get(i));
            }
        }
        return result;
    }

    /*
     * 要处理overlay点击事件时需要继承ItemizedOverlay，不处理点击事件时可直接生成ItemizedOverlay.
     */
    public class MyOverlay extends ItemizedOverlay<OverlayItem> {
        public MyOverlay(Drawable arg0, MapView arg1) {
            super(arg0, arg1);
        }

        @Override
        public int size() {
            return super.size();
        }

        //标注物点击触发事件
        @Override
        protected boolean onTap(int position) {
            OverlayItem overlay = getItem(position);
            Drawable bd = overlay.getMarker();
            int mAlpha = bd.getLevel();
            if (mAlpha == 1) {//判断是否是海岛标注图标，如果是则跳转到详情页
                String mId = overlay.getTitle();
                Intent intent = new Intent(getActivity(), IslandInformationActivity_.class);
                intent.putExtra("islandID", Integer.parseInt(mId));
                startActivity(intent);
            }
            return true;
        }
    }

    /**
     * 初始化定位
     */
    private void initLocationOrientate() {
        try {
            locationService = ((ToponymApplication) getActivity().getApplication()).locationService;
            myLocationOverlay = new MyLocationOverlay(mapView);//定位图层
            mapView.getOverlays().add(myLocationOverlay);
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
            locationService.registerListener(mListener);
            locationService.start();// 定位SDK
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private BDLocationListener mListener = new BDLocationListener() {
        LocationData locData = new LocationData();

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null) {
                return;
            }
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            try {
                if (longitudeFromActivity != null && latitudeFromActivity != null) {
                    /**
                     * 海岛详情页面定位，根据海岛坐标定位
                     */
                    LatLonData latlonData = TransformLatLon.transform(Double.valueOf(latitudeFromActivity),
                            Double.valueOf(longitudeFromActivity));
                    locData.latitude = latlonData.getLatitude();
                    locData.longitude = latlonData.getLongitude();
                    locData.direction = 2.0f;
                } else {
                    /**
                     * 根据用户实际位置定位
                     */
                    locData.latitude = latitude;
                    locData.longitude = longitude;
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
            myLocationOverlay.setData(locData);
            mapView.getController().animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                    (int) (locData.longitude * 1e6)));
            mapView.refresh();
            /**
             * LOG打印
             */
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append(location.getDirection());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                }
                Log.e("LOC", sb.toString());
            }
        }
    };

    /**
     * 初始化地图相关的代码，创建覆盖物图层，自定义图层，定位图层
     */
    private void initBaiDuMap() {
        mapView.setSatellite(true);
        mapView.getController().enableClick(true);
        mapView.setBuiltInZoomControls(false);
        mCluster = new Cluster(getActivity(), mapView, isAverageCenter, mGridSize, mDistance);
        /**标注物点击实现类*/
        mOverlay = new MyOverlay(null, mapView);
        mapView.getOverlays().clear();
        mapView.getOverlays().add(mOverlay);
        /**
         * 根据地图进去入口设置不同缩放等级
         */
        if (longitudeFromActivity != null && latitudeFromActivity != null) {
            mapView.getController().setZoom(12.0f);
        } else {
            mapView.getController().setZoom(7.5f);
        }
    }

    /**
     * 地图生命周期依附
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 地图生命周期依附
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Fragment", "onStop");
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop();
        result.clear();
        mMarkerList.clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("Fragment", "onDetach");
    }

    /**
     * 地图生命周期依附
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.destroy();
        mapView = null;
        Log.i("Fragment", "onDestroy");
    }
}
