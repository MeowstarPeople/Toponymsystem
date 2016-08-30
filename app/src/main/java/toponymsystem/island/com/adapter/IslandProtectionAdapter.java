package toponymsystem.island.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import toponymsystem.island.com.R;
import toponymsystem.island.com.model.RegulationData;

/**
 * Created by CuiXiaoYong
 * on 2016/4/12
 * E-Mail:hellocui@aliyun.com
 */
public class IslandProtectionAdapter extends BaseAdapter {

    private List<RegulationData> data;
    private LayoutInflater mInflater;
    Context mContext;

    /**
     * @param data 查询结果集
     */
    public IslandProtectionAdapter(Context context, List<RegulationData> data) {
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
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.layout_regulation_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(data.get(position).getRegulationTitle());
        return convertView;
    }

    class ViewHolder {
        /**
         * 标题
         */
        TextView title;
    }
}
