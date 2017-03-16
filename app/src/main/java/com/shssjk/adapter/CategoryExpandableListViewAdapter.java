package com.shssjk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shssjk.activity.R;
import com.shssjk.activity.shop.ProductListActivity;
import com.shssjk.model.shop.Data;
import com.shssjk.model.shop.Tmenu;
import com.shssjk.utils.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * ExpandableListView 商品分类 适配器
 */
public class CategoryExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    /**
     * 每个分组的名字的集合
     */
    private List<String> groupList;

    /**
     * 所有分组的所有子项的 GridView 数据集合
     */
    private List<List<String>> itemList;
      List<Data> categories= new ArrayList<Data>();
    private GridView gridView;
    private MyGridViewAdapter gridViewAdapter;

    public CategoryExpandableListViewAdapter(Context context, List<String> groupList,
                                             List<List<String>> itemList) {
        mContext = context;
        this.groupList = groupList;
        this.itemList = itemList;
    }
    public CategoryExpandableListViewAdapter(Context context) {
        mContext = context;
    }

    public CategoryExpandableListViewAdapter(Context context, List<Data> categories) {
        mContext = context;
        this.categories=categories;
    }
    public void setDate(List<Data> categories) {
        if(categories == null)return;
        this.categories=categories;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getTmenu();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
            parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.expandablelist_group, null);
        }
        ImageView ivGroup = (ImageView) convertView.findViewById(R.id.iv_group);
        TextView tvGroup = (TextView) convertView.findViewById(R.id.tv_group);
        // 如果是展开状态，就显示展开的箭头，否则，显示折叠的箭头
//        if (isExpanded) {
//            ivGroup.setImageResource(R.drawable.ic_open);
//        } else {
//            ivGroup.setImageResource(R.drawable.ic_close);
//        }
        // 设置分组组名
        tvGroup.setText(categories.get(groupPosition).getName());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.expandablelist_item, null);
        }
        // 因为 convertView 的布局就是一个 GridView，
        // 所以可以向下转型为 GridView
        gridView = (GridView) convertView;
        // 创建 GridView 适配器
        Logger.e("categories",categories.get(groupPosition).getTmenu().size()+"");
        gridViewAdapter = new MyGridViewAdapter(mContext, categories.
                get(groupPosition).getTmenu());
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tmenu tmenu = categories.
                        get(groupPosition).getTmenu().get(position);
                startupActivity(tmenu.getId());
            }
        });


        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void startupActivity(String categoryId){
        Intent intent = new Intent(mContext , ProductListActivity.class);
        intent.putExtra("category", categoryId);
        mContext.startActivity(intent);
    }

}
