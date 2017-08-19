package com.pursuege.schoolproject;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.pursuege.schoolproject.ui.BaseTitleActivity;
import com.pursuege.schoolproject.ui.MainActivity;

public class ApplyNotifyActivity extends BaseTitleActivity {

    private WebView webView;
    private Button btnNext;

    @Override
    public View getContentBaseView() {
        return setInflateView(R.layout.activity_apply_notify);
    }

    @Override
    public void setupUiView() {
        super.setupUiView();
        webView = (WebView) findViewById(R.id.apply_notify_webview);
        btnNext = (Button) findViewById(R.id.apply_notify_next_btn);
    }

    @Override
    public void setupAllData() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setDefaultTextEncodingName("utf-8"); //设置编码格式(false); //支持缩放，默认为true。是下面那个的前提。
        webView.loadUrl("file:///android_asset/apply_notify.html");
    }

    @Override
    public String setTopTitle() {
        return getResources().getString(R.string.select_school);
    }

    public void onclickReadNext(View v) {
        finish();
        doStartActivity(MainActivity.class);
    }


}
