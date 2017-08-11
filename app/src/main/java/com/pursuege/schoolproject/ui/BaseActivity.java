package com.pursuege.schoolproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity {

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


}
