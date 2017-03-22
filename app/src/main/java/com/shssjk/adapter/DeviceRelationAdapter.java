package com.shssjk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.shssjk.activity.R;
import com.shssjk.model.health.Device;
import com.shssjk.utils.SSUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-05-06.
 * 设备关系适配器
 */
public class DeviceRelationAdapter extends ArrayAdapter<Device> {
    private int resourceId;
    private Context mContext;
    List<Device> mHealthPeople = new ArrayList<Device>();

    public DeviceRelationAdapter(Context context, int textViewResourceId,
                                 List<Device> mHealthPeople) {
        super(context, textViewResourceId, mHealthPeople);
        resourceId = textViewResourceId;
        mContext = context;
        this.mHealthPeople = mHealthPeople;
    }
    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Device
                personArchives = getItem(position);//获取当前项的Fruit的实例
        View view;
        ViewHolder viewHoder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHoder = new ViewHolder();
            viewHoder.Name = (TextView) view.findViewById(R.id.devicetype_txtv);
            view.setTag(viewHoder);//将ViewHoder储存在View中
        } else {
            view = convertView;
            //重写获取ViewHolder
            viewHoder = (ViewHolder) view.getTag();
        }
        if (!SSUtils.isEmpty(personArchives.getRelation())) {
            viewHoder.Name.setText(personArchives.getRelation());
        }
        return view;
    }
    class ViewHolder {
        TextView Name;
          }
}
