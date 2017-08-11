package com.pursuege.schoolproject.ui;

import android.view.View;

import com.pursuege.schoolproject.ApplyNotifyActivity;
import com.pursuege.schoolproject.R;

public class WellcomeActivity extends BaseActivity {


    @Override
    public View getContentView() {
        return setInflateView(R.layout.activity_wellcome);
    }

    @Override
    public void setupUiView() {
        finish();
        doStartActivity(ApplyNotifyActivity.class);
    }


    @Override
    public void setupAllData() {

    }
}
