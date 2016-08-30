package toponymsystem.island.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

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
public class SearchDataAdapter extends BaseAdapter {

    private List<IslandData> mSearchData;
    private LayoutInflater mInflater;
    private DisplayImageOptions displayImageOptions;
    Context context;

    /**
     * @param searchData 查询结果集
     */
    public SearchDataAdapter(Context context, List<IslandData> searchData, DisplayImageOptions displayImageOptions) {
        this.context = context;
        mSearchData = searchData;
        this.displayImageOptions = displayImageOptions;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mSearchData == null ? 0 : mSearchData.size();
    }

    @Override
    public Object getItem(int position) {
        return mSearchData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_search_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mSearchData.get(position).getName());
        if (!StringUtil.isEmpty(mSearchData.get(position).getImgUrl())) {
            ImageLoader.getInstance().displayImage(URL.HTTP + mSearchData.get(position).getImgUrl(), holder.image, displayImageOptions);
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
