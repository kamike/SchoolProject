package com.pursuege.schoolproject;


import android.view.View;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.pursuege.schoolproject.ui.BaseTitleActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends BaseTitleActivity {

    private ExpandableListView listView;

    @Override
    public View getContentBaseView() {
        return setInflateView(R.layout.activity_main);
    }

    @Override
    public void setupUiView() {
        super.setupUiView();
        listView= (ExpandableListView) findViewById(R.id.main_listview);
    }

    @Override
    public void setupAllData() {
//        listView.setAdapter();
    }

    @Override
    public String setTopTitle() {
        return getResources().getString(R.string.select_school);
    }

    public void onclickLocation(View v) {
        long start = System.currentTimeMillis();
        try {
            InputStream in = getResources().getAssets().open("json_test.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            reader.close();
            AllSchollBean bean = JSON.parseObject(sb.toString(), AllSchollBean.class);
            System.out.println("有多少条数据：" + bean.data.size());
            System.out.println("用了多长时间：" + (System.currentTimeMillis() - start) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
