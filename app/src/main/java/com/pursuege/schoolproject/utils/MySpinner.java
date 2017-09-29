package com.pursuege.schoolproject.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by wangtao on 2017/9/29.
 */

public class MySpinner {
    private final Context context;
    private final LinearLayout linearLayout;
    private final OnViewSelectClickListener listener;
    private int[] arrayImgs;

    public MySpinner(Context c, int[] arrayImgs, LinearLayout linearLayout, OnViewSelectClickListener listener) {
        this.context = c;
        this.arrayImgs = arrayImgs;
        this.linearLayout = linearLayout;

        this.listener = listener;
        linearLayout.setVisibility(View.VISIBLE);
        setSelectIndex(0);
    }

    public void setSelectIndex(final int index) {
        linearLayout.removeAllViews();
        ImageView iv = new ImageView(context);

        iv.setImageResource(arrayImgs[index]);
        currentSelectIndex = index;
        linearLayout.addView(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearLayout.getChildCount() == 1) {
                    expandItem();
                } else {
                    linearLayout.removeViews(1, linearLayout.getChildCount() - 1);
                    listener.onViewSelect(index);
                }
            }
        });
    }

    private int currentSelectIndex = 0;

    private void expandItem() {
        for (int position = 0; position < arrayImgs.length; position++) {
            if (position != currentSelectIndex) {
                ImageView iv = new ImageView(context);

                iv.setPadding(15, 10, 10, 15);
                iv.setBackgroundColor(Color.rgb(232, 232, 232));
                iv.setImageResource(arrayImgs[position]);
                final int pos = position;
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setSelectIndex(pos);
                    }
                });
                linearLayout.addView(iv);
            }
        }
    }

    public interface OnViewSelectClickListener {

        public void onViewSelect(int position);
    }

}
