package toponymsystem.island.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import toponymsystem.island.com.R;
import toponymsystem.island.com.model.IslandData;
import toponymsystem.island.com.model.RegulationData;

/**
 * Created by CuiXiaoYong
 * on 2016/4/12
 * E-Mail:hellocui@aliyun.com
 */
public class TableStatisticsAdapter extends BaseAdapter {

    private List<IslandData> data;
    private LayoutInflater mInflater;
    Context mContext;

    /**
     * @param data 查询结果集
     */
    public TableStatisticsAdapter(Context context, List<IslandData> data) {
        mContext = context;
        this.data = data;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.table_statistics_item, null);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.usg = (TextView) convertView.findViewById(R.id.usg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.id.setText((position + 1) + "");
        holder.name.setText(data.get(position).getName());
        if (data.get(position).isReside()) {
            holder.type.setText("有居民");
        } else {
            holder.type.setText("无居民");
        }
        holder.usg.setText(data.get(position).getUsg());
        return convertView;
    }

    class ViewHolder {
        /**
         * ID
         */
        TextView id;
        /**
         * 海岛名
         */
        TextView name;
        /**
         * 类型
         */
        TextView type;
        /**
         * 用途
         */
        TextView usg;
    }
}
