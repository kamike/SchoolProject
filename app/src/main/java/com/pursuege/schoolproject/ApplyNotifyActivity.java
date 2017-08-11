package com.pursuege.schoolproject;

import android.view.View;

import com.pursuege.schoolproject.ui.BaseTitleActivity;

public class ApplyNotifyActivity extends BaseTitleActivity {

    @Override
    public View getContentBaseView() {
        return setInflateView(R.layout.activity_apply_notify);
    }

    @Override
    public void setupUiView() {
        super.setupUiView();
    }

    @Override
    public void setupAllData() {
        finish();
        doStartActivity(MainActivity.class);
    }

    @Override
    public String setTopTitle() {
        return getResources().getString(R.string.select_school);
    }

    public void onclickLocation(View v) {

    }


}
