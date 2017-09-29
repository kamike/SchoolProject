package com.pursuege.schoolproject.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.pursuege.schoolproject.R;

/**
 * Created by wangtao on 2017/9/25.
 */

public class AdapterSpinner extends BaseAdapter {
    private final Context context;
    public int[] imgArray={R.drawable.icon_mobile,R.drawable.icon_unicom,R.drawable.icon_telecom};

    public AdapterSpinner(Context c) {
        this.context = c;
    }

    @Override
    public int getCount() {
        return imgArray.length;
    }

    @Override
    public Object getItem(int i) {
        return imgArray[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView iv=new ImageView(context);
        iv.setPadding(SizeUtils.dp2px(10),SizeUtils.dp2px(10),SizeUtils.dp2px(10),0);
        iv.setImageResource(imgArray[i]);
        return iv;
    }
}
