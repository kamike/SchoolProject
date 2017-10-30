package com.pursuege.schoolproject;

import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.FileIOUtils;
import com.pursuege.schoolproject.bean.CidDataBean;
import com.pursuege.schoolproject.bean.MncCidBean;
import com.pursuege.schoolproject.ui.BaseActivity;
import com.pursuege.schoolproject.utils.CidIdUtils;

import java.io.File;
import java.util.ArrayList;

public class TestDataActivity extends BaseActivity {

    private ListView listView;

    @Override
    public View getContentView() {
        return setInflateView(R.layout.activity_test_data);
    }

    @Override
    public void setupUiView() {
        listView = (ListView) findViewById(R.id.test_listview);
    }

    @Override
    public void setupAllData() {
        MncCidBean mainMnc = CidIdUtils.getMainMncCid(this);
        if (mainMnc == null) {
            doShowToast("未获取到主卡数据");
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory() + "/data/" + mainMnc.mnc + "/cid_all_data");
        if (!file.exists()) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
        String data = FileIOUtils.readFile2String(file);
        if (TextUtils.isEmpty(data)) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
       final ArrayList<CidDataBean> listCid = (ArrayList<CidDataBean>) JSON.parseArray(data, CidDataBean.class);
        if (listCid == null || listCid.isEmpty()) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return listCid.size();
            }

            @Override
            public Object getItem(int position) {
                return listCid.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv=new TextView(TestDataActivity.this);
                tv.setText(listCid.get(position).toString());
                return tv;
            }
        });
    }
}
