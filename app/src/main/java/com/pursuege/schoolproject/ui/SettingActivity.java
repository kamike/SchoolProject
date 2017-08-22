package com.pursuege.schoolproject.ui;

import android.view.View;
import android.widget.ImageView;

import com.pursuege.schoolproject.R;

public class SettingActivity extends BaseTitleActivity {

    private ImageView ivMsg, ivjar, ivMusic;

    @Override
    public String setTopTitle() {
        return getResources().getString(R.string.setting_noftify_type);
    }

    @Override
    public View getContentBaseView() {
        return setInflateView(R.layout.activity_setting);
    }

    @Override
    public void setupUiView() {
        super.setupUiView();
        ivMsg = (ImageView) findViewById(R.id.setting_msg_switch_iv);
        ivjar = (ImageView) findViewById(R.id.setting_jar_switch_iv);
        ivMusic = (ImageView) findViewById(R.id.setting_music_switch_iv);
    }

    @Override
    public void setupAllData() {

    }

    public void onclickNotifySwitch(View v) {

    }

    public void onclickJarSwitch(View v) {

    }

    public void onclickMusicSwitch(View v) {

    }

    public void onclickSubmit(View v) {

    }
}
