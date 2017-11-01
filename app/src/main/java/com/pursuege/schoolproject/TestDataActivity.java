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
import com.pursuege.schoolproject.ui.BaseActivity;

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
    ArrayList<CidDataBean> listCid;
    @Override
    public void setupAllData() {
        File file = new File(Environment.getExternalStorageDirectory() + "/data/sim1/cid_all_data");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/data/sim2/cid_all_data");
        if (!file.exists() && !file2.exists()) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
        String data = FileIOUtils.readFile2String(file);
        String data2 = FileIOUtils.readFile2String(file2);
        if (TextUtils.isEmpty(data) && TextUtils.isEmpty(data2)) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
        listCid= (ArrayList<CidDataBean>) JSON.parseArray(data, CidDataBean.class);
        ArrayList<CidDataBean> listCid2 = (ArrayList<CidDataBean>) JSON.parseArray(data2, CidDataBean.class);
        if (listCid == null || listCid.isEmpty()) {
            doShowToast("没有主卡对应的数据，请重新更新数据！");
            return;
        }
        if (listCid == null) {
            listCid = new ArrayList<>();
        }
        if (listCid2 != null) {
            listCid.addAll(listCid2);
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
