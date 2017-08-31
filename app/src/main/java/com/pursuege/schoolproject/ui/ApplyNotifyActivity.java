package com.pursuege.schoolproject.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.pursuege.schoolproject.R;
import com.pursuege.schoolproject.utils.PermissionUtil;

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
        if (PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 1)) {
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length < 1) {
            doShowToast("权限申请失败");
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            Toast.makeText(this, R.string.permission_fail, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public String setTopTitle() {
        return getResources().getString(R.string.select_school);
    }

    public void onclickReadNext(View v) {
        doStartActivity(MainActivity.class);
    }


}
