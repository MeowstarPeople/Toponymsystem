package toponymsystem.island.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

import toponymsystem.island.com.R;
import toponymsystem.island.com.model.IslandNewsData;
import toponymsystem.island.com.model.RegulationData;

/**
 * Created by CuiXiaoYong
 * on 2016/4/12
 * E-Mail:hellocui@aliyun.com
 */
public class MoreRegulationsAdapter extends BaseAdapter {

    private List<RegulationData> data;
    private LayoutInflater mInflater;
    Context context;

    /**
     * @param data 查询结果集
     */
    public MoreRegulationsAdapter(Context context, List<RegulationData> data) {
        this.context = context;
        this.data = data;
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
            convertView = mInflater.inflate(R.layout.layout_more_regulations_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(data.get(position).getRegulationTitle());
        return convertView;
    }

    class ViewHolder {
        TextView name;
    }
}
