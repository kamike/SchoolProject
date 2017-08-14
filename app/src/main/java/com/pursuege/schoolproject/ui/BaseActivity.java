package com.pursuege.schoolproject.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.pursuege.schoolproject.Constants;
import com.pursuege.schoolproject.R;

public abstract class BaseActivity extends AppCompatActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        setupUiView();
        setupAllData();
    }

    public abstract View getContentView();

    public abstract void setupUiView();

    public abstract void setupAllData();

    public View setInflateView(int layoutId) {
        return LayoutInflater.from(this).inflate(layoutId, null);
    }
    public void doStartActivity(Class activity) {
        Intent intent=new Intent(this,activity);
        startActivity(intent);

    }

    public void doShowMesage(String msg, DialogInterface.OnClickListener listener) {
        if (isFinishing()) {
            return;
        }
        new AlertDialog.Builder(this).setTitle(null).setMessage(msg).setNegativeButton(R.string.comfirm, listener).show();
    }

    public void doShowMesage(@StringRes int msg, DialogInterface.OnClickListener listener) {
        if (isFinishing()) {
            return;
        }
        new AlertDialog.Builder(this).setTitle(null).setMessage(msg).setNegativeButton(R.string.comfirm, listener).show();
    }
    public void doShowToast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void doShowToast(@StringRes final int msgRes) {
        Toast.makeText(this, msgRes, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
