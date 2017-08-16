package com.pursuege.schoolproject.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.pursuege.schoolproject.R;
import com.pursuege.schoolproject.bean.SchollInfoBean;

import java.util.ArrayList;

/**
 * Created by wangtao on 2017/8/11.
 */

public class SchoolExpandAdapter extends BaseExpandableListAdapter {


    private final ArrayList<SchollInfoBean> list;
    private final Context context;

    public SchoolExpandAdapter(Context mainActivity, ArrayList<SchollInfoBean> list) {
        this.list = list;
        this.context = mainActivity;

    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        if (list.get(i).deptList == null) {
            return 0;
        }
        return list.get(i).deptList.size();
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).deptList.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.txt_item, null);
        textView.setText(list.get(i).college);
        return textView;
    }

    @Override
    public View getChildView(int position, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.txt_item, null);
        if (list.get(position).deptList != null) {
            if (list.get(position).deptList.size() > childPosition) {
                textView.setText(list.get(position).deptList.get(childPosition).dept);
            }
        }
        return textView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
