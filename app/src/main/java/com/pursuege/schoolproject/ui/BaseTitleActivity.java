package com.pursuege.schoolproject.ui;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pursuege.schoolproject.R;

/**
 * Created by wangtao on 2017/8/7.
 */

public abstract class BaseTitleActivity extends BaseActivity {

    private TextView mTvTitle;
    private RelativeLayout mRlContent;
    private ImageView mIvBack;
    private LinearLayout mLlStatu;

    FrameLayout mFlContent;

    @Override
    public View getContentView() {
        View view = setInflateView(R.layout.activity_base_title);
        mFlContent = (FrameLayout) view.findViewById(R.id.fl_content);
        mFlContent.addView(getContentBaseView());
        mTvTitle = view.findViewById(R.id.tv_title);
        mRlContent = view.findViewById(R.id.rl_content);
        mIvBack = view.findViewById(R.id.iv_back);
        mLlStatu = view.findViewById(R.id.ll_statu);
        return view;
    }

    @Override
    public void setupUiView() {
        mTvTitle.setText(setTopTitle());
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void hintBackIv() {
        if (mIvBack != null) {
            mIvBack.setVisibility(View.GONE);
        }
    }

    public void setStatuColor(int color) {
        mLlStatu.setBackgroundColor(color);
    }

    public void setBGColor(int color) {
        mRlContent.setBackgroundColor(color);
    }

    @Override
    public void setupAllData() {

    }

    public abstract String setTopTitle();

    public abstract View getContentBaseView();
}
