package com.pursuege.schoolproject.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
    private int blue, normal;
    private Drawable drawableLine;

    public SchoolExpandAdapter(Context mainActivity, ArrayList<SchollInfoBean> list) {
        this.list = list;
        this.context = mainActivity;
        blue = context.getResources().getColor(R.color.txt_blue);
        normal = context.getResources().getColor(R.color.txt_normal);
        drawableLine = context.getResources().getDrawable(R.drawable.icon_line_blue);
        selectPosition = -1;
        selectChildPosition = -1;
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
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        final TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.txt_item, null);
        textView.setText(list.get(i).college);
        if (i == selectPosition) {
            textView.setTextColor(blue);
        } else {
            textView.setTextColor(normal);
        }
        if (list.get(i).deptList == null || list.get(i).deptList.isEmpty()) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectPosition = i;
                    notifyDataSetChanged();
                }
            });
        }

        return textView;
    }

    public static int selectPosition = -1;
    public static int selectChildPosition = -1;

    @Override
    public View getChildView(final int position, final int childPosition, boolean b, View view, ViewGroup viewGroup) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text_shcool_child, null);
        if (list.get(position).deptList != null) {
            if (list.get(position).deptList.size() > childPosition) {
                textView.setText(list.get(position).deptList.get(childPosition).dept);
            }
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectChildPosition = childPosition;
                selectPosition = position;
                notifyDataSetChanged();

            }
        });

        if (childPosition == selectChildPosition) {
            textView.setTextColor(blue);
            textView.setCompoundDrawables(drawableLine, null, null, null);
        } else {
            textView.setTextColor(normal);
            textView.setCompoundDrawables(null, null, null, null);
        }
        return textView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
