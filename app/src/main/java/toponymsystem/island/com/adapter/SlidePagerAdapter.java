package toponymsystem.island.com.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import toponymsystem.island.com.iView.SideItemListener;

/**
 * Created by CuiXiaoYong
 * on 2016/4/9
 * E-Mail:hellocui@aliyun.com
 */
public class SlidePagerAdapter extends PagerAdapter {

    List<ImageView> imageList;
    DisplayImageOptions displayImageOptions;
    private int position;
    SideItemListener listener;

    public SlidePagerAdapter(List imageList, DisplayImageOptions displayImageOptions) {
        this.imageList = imageList;
        this.displayImageOptions = displayImageOptions;
    }

    public void setOnItemClickListener(SideItemListener listener) {
        this.listener = listener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(imageList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        this.position = position;
        ImageView imageView = imageList.get(position);
        ImageLoader.getInstance().displayImage(imageView.getTag() + "", imageView, displayImageOptions);//显示图片
        ((ViewPager) container).addView(imageList.get(position));

        /* Viewpager项点击事件*/
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.getPosition(position);
                }
            }
        });
        return imageList.get(position);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
