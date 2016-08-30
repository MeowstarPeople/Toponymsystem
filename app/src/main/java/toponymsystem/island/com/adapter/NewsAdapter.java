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
import toponymsystem.island.com.model.IslandNewsData;
import toponymsystem.island.com.rest.api.URL;
import toponymsystem.island.com.utils.StringUtil;

/**
 * Created by CuiXiaoYong
 * on 2016/4/12
 * E-Mail:hellocui@aliyun.com
 */
public class NewsAdapter extends BaseAdapter {

    private List<IslandNewsData> data;
    private LayoutInflater mInflater;
    private DisplayImageOptions displayImageOptions;
    Context context;

    /**
     * @param data 查询结果集
     */
    public NewsAdapter(Context context, List<IslandNewsData> data, DisplayImageOptions displayImageOptions) {
        this.context = context;
        this.data = data;
        this.displayImageOptions = displayImageOptions;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_news_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(data.get(position).getTitle());
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
