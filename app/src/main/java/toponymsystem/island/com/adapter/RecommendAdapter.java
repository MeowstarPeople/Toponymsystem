package toponymsystem.island.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import toponymsystem.island.com.R;
import toponymsystem.island.com.model.IslandData;
import toponymsystem.island.com.rest.api.URL;
import toponymsystem.island.com.utils.StringUtil;

/**
 * Created by CuiXiaoYong
 * on 2016/4/12
 * E-Mail:hellocui@aliyun.com
 */
public class RecommendAdapter extends BaseAdapter {

    private List<IslandData> mTouristRecreationData;
    private LayoutInflater mInflater;
    private DisplayImageOptions displayImageOptions;
    Context context;

    /**
     * @param data 数据
     */
    public RecommendAdapter(Context context, List<IslandData> data, DisplayImageOptions displayImageOptions) {
        this.context = context;
        mTouristRecreationData = data;
        this.displayImageOptions = displayImageOptions;
    }

    @Override
    public int getCount() {
        return mTouristRecreationData == null ? 0 : mTouristRecreationData.size();
    }

    @Override
    public Object getItem(int position) {
        return mTouristRecreationData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_grid_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.island_name);
            holder.image = (ImageView) convertView.findViewById(R.id.island_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mTouristRecreationData.get(position).getName());
        if (!StringUtil.isEmpty(mTouristRecreationData.get(position).getImgUrl())) {
            ImageLoader.getInstance().displayImage(URL.HTTP + mTouristRecreationData.get(position).getImgUrl(), holder.image, displayImageOptions);
        } else {
            holder.image.setImageResource(R.drawable.image);
        }
        return convertView;
    }

    class ViewHolder {
        /**
         * 岛名
         */
        TextView name;
        /**
         * 基本图片
         */
        ImageView image;
    }
}
