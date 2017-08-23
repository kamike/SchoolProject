package com.pursuege.schoolproject.ui;

import android.view.View;

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
        long start = System.currentTimeMillis();
//        try {
//            InputStream in = getResources().getAssets().open("json_test.json");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            String str = null;
//            StringBuilder sb = new StringBuilder();
//            while ((str = reader.readLine()) != null) {
//                sb.append(str);
//            }
//            in.close();
//            reader.close();
//            AllSchollBean bean = JSON.parseObject(sb.toString(), AllSchollBean.class);
//            System.out.println("有多少条数据：" + bean.data.size());
//            System.out.println("用了多长时间：" + (System.currentTimeMillis() - start) + "ms");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
