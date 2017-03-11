package com.shssjk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.model.person.Camera;
import com.soubao.tpshop.utils.SPStringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author 摄像机列表
 */
public class CameraAdapter extends BaseAdapter {

    private String TAG = "CameraAdapter";

    private List<Camera> mCamera;
    private Context mContext;

    public CameraAdapter(Context context) {
        this.mContext = context;
    }
    public void setData(List<Camera> articles) {
        if (articles == null) return;
        this.mCamera = articles;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (mCamera == null) return 0;
        return mCamera.size();
    }
    @Override
    public Object getItem(int position) {
        if (mCamera == null) return null;
        return mCamera.get(position);
    }
    @Override
    public long getItemId(int position) {
        if (mCamera == null) return -1;
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //category_left_item.xml
        final ViewHolder holder;
        if (convertView == null) {
            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(R.layout.camera_list_item, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder(convertView);
                    //设置标记
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //获取该行数据
        Camera camer = (Camera) mCamera.get(position);
        if (!SPStringUtils.isEmpty(camer.getName())){
            holder.devicname_txtv.setText(camer.getName());
        }
        if (!SPStringUtils.isEmpty(camer.getDid())){
            holder.devicenum_txtv.setText(camer.getDid());
        }
        return convertView;
    }
    class ViewHolder {
        @Bind(R.id.devicname_txtv)
        TextView devicname_txtv;
        @Bind(R.id.devicenum_txtv)
        TextView devicenum_txtv;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
