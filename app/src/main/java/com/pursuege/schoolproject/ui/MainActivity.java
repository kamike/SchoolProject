package com.pursuege.schoolproject.ui;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.pursuege.schoolproject.R;
import com.pursuege.schoolproject.bean.SchollInfoBean;
import com.pursuege.schoolproject.bean.SchoolAreaBean;
import com.pursuege.schoolproject.utils.LocationUtils;
import com.pursuege.schoolproject.utils.LogUtils;
import com.pursuege.schoolproject.utils.NetworkCore;
import com.pursuege.schoolproject.utils.PermissionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseTitleActivity {

    private ExpandableListView listView;
    private TextView tvLocation;
    private EditText etSearch;

    @Override
    public View getContentBaseView() {

        return setInflateView(R.layout.activity_main);
    }

    @Override
    public void setupUiView() {
        super.setupUiView();
        listView = (ExpandableListView) findViewById(R.id.main_listview);
        tvLocation = (TextView) findViewById(R.id.main_localtion_tv);
        etSearch = (EditText) findViewById(R.id.main_search_et);
        EventBus.getDefault().register(this);
    }

    @Override
    public void setupAllData() {
//        listView.setAdapter();
        onclickLocation(null);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    HashMap<String, Object> parmas = new HashMap<>();
                    parmas.put("college", etSearch.getText().toString());
                    parmas.put("page", "1");
                    NetworkCore.doPostParams(selectShool, parmas, SchollInfoBean[].class);

                }
                return false;
            }
        });
    }

    @Override
    public String setTopTitle() {
        return getResources().getString(R.string.select_school);
    }

    public void onclickLocation(View v) {
        tvLocation.setText(R.string.location_ing);
        if (PermissionUtil.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION, 0)) {
            onLocationPermission();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length < 1) {
            tvLocation.setText(R.string.location_fail_again);
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionSuccess(requestCode);
        } else {
            Toast.makeText(this, R.string.permission_fail, Toast.LENGTH_SHORT).show();
            tvLocation.setText(R.string.location_fail_again);
        }

    }

    public void permissionSuccess(int requestCode) {
        onLocationPermission();
    }

    private void onLocationPermission() {
        BDAbstractLocationListener listener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null) {
                    tvLocation.setText(R.string.location_fail_again);
                    return;
                }
                String city = bdLocation.getCity();
                if (locationUtils != null) {
                    locationUtils.setStopLocation();
                }
                if (TextUtils.isEmpty(city)) {
                    tvLocation.setText(R.string.location_fail_again);

                    return;
                }
                tvLocation.setText(city + getResources().getString(R.string.select_school_and_are));
                HashMap<String, Object> parmas = new HashMap<>();
                city = city.replace("市", "");
                parmas.put("college", city);
                parmas.put("page", "1");
                NetworkCore.doGetParams(selectShool, parmas, SchollInfoBean[].class);
            }

        };
        locationUtils = new LocationUtils(this, listener);
    }

    private LocationUtils locationUtils;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkFail(String message) {
        if (TextUtils.isEmpty(message)) {
            doShowToast(NetwException);
            return;
        }
        doShowToast(message);
        //

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(ArrayList<SchollInfoBean> list) {
        LogUtils.i("定位返回的数据：" + list);
        if(list==null||list.isEmpty()){
            doShowToast("没有任何数据！");
        }
        this.list = list;
        listView.setAdapter(new SchoolExpandAdapter(this, list));

    }

    private ArrayList<SchollInfoBean> list;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onclickMainNext(View v) {
        if (SchoolExpandAdapter.selectPosition == -1 || list == null || list.isEmpty()) {
            doShowMesage(R.string.not_select_school, null);
            return;
        }

        if (SchoolExpandAdapter.selectChildPosition >= 0) {
            SchoolAreaBean childInfo = list.get(SchoolExpandAdapter.selectPosition).deptList.get(SchoolExpandAdapter.selectChildPosition);
            LogUtils.i("cidid--start:" + childInfo.toString());
            SelectOperatNameActivity.startOperateSelect(this, childInfo.cidId + "");
        } else {
            SchollInfoBean info = list.get(SchoolExpandAdapter.selectPosition);
            SelectOperatNameActivity.startOperateSelect(this, info.collId + "");
        }


    }
}
