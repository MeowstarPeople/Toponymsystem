package toponymsystem.island.com.mapapi;

import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class ClusterMarker extends OverlayItem {
	private GeoPoint mCenter;//中心点坐标
	private List<OverlayItem> mMarkers;//覆盖物中的标注物
	private MBound mGridBounds;
	public ClusterMarker(GeoPoint geoPoint, String title, String spa) {
		super(geoPoint, title, spa);
		mMarkers = new ArrayList<OverlayItem>();
	}
	/**
	 * 计算平均中心点
	 * @return GeoPoint
	 */
	private GeoPoint calAverageCenter(){
		int latitude=0,longitude=0;
		int len = mMarkers.size()==0?1:mMarkers.size();
		for(int i=0;i<len;i++){
			latitude = latitude + mMarkers.get(i).getPoint().getLatitudeE6();
			longitude = longitude + mMarkers.get(i).getPoint().getLongitudeE6();
		}
		return new GeoPoint((int)(latitude/len),(int)(longitude/len));
	}
	/**
	 * ClusterMarker中添加marker
	 * @param marker
	 * @param isAverageCenter
	 */
	public void AddMarker(OverlayItem marker, Boolean isAverageCenter){
		mMarkers.add(marker);
		if(!isAverageCenter){
			if(mCenter==null)
				this.mCenter = mMarkers.get(0).getPoint();
		}else{
			this.mCenter = calAverageCenter();
		}
	}
	public GeoPoint getmCenter() {
		return this.mCenter;
	}
	public void setmCenter(GeoPoint mCenter) {
		this.mCenter = mCenter;
	}
	public List<OverlayItem> getmMarkers() {
		return mMarkers;
	}
	public void setmMarkers(List<OverlayItem> mMarkers, Boolean isAverageCenter) {
		this.mMarkers.addAll(mMarkers);
		if(!isAverageCenter){
			if(mCenter==null){
				this.mCenter =  mMarkers.get(0).getPoint();
			}
		}else
			this.mCenter = calAverageCenter();
	}
	public MBound getmGridBounds() {
		return mGridBounds;
	}
	public void setmGridBounds(MBound mGridBounds) {
		this.mGridBounds = mGridBounds;
	}
}
