package com.pursuege.schoolproject.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.pursuege.schoolproject.Constants;
import com.pursuege.schoolproject.bean.BaseServerBean;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * Created by wangtao on 2017/8/11.
 */

public class NetworkCore implements Constants {

    public static void doPostParams(String urlName, HashMap<String, Object> params, final Class t) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .build();
        FormBody.Builder formbody = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                formbody.add(key, params.get(key) + "");
            }
        }
        FormBody body = formbody.build();
        final Request req = new Request.Builder()
                .url(Constants.BASEURL + "/" + urlName)
                .post(body)
                .addHeader("Content-Type", "applicationapplication/json")
                .build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EventBus.getDefault().post("");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resoult = response.body().string();
                if (TextUtils.isEmpty(resoult)) {
                    EventBus.getDefault().post("");
                    return;
                }
                try {
                    BaseServerBean base = parseObject(resoult, BaseServerBean.class);
                    if (base == null) {
                        EventBus.getDefault().post("");
                        return;
                    }
                    if (!TextUtils.equals(base.state, "success")) {
                        EventBus.getDefault().post(base.message);
                        return;
                    }
                    if (t.isArray()) {
                        EventBus.getDefault().post(JSON.parseArray(base.data, t.getComponentType()));
                    } else {
                        EventBus.getDefault().post(JSON.parseObject(base.data, t));
                    }
                } catch (JSONException e) {
                    LogUtils.i("Exception:" + resoult);
                    EventBus.getDefault().post("");
                    e.printStackTrace();
                }

            }
        });

    }

    public static void doPostBase(String urlName, HashMap<String, Object> params, final Class t) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .build();
        FormBody.Builder formbody = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                formbody.add(key, params.get(key) + "");
            }
        }
        FormBody body = formbody.build();
        final Request req = new Request.Builder()
                .url(Constants.BASEURL + "/" + urlName)
                .post(body)
                .addHeader("Content-Type", "applicationapplication/json")
                .build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EventBus.getDefault().post("");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resoult = response.body().string();
                if (TextUtils.isEmpty(resoult)) {
                    EventBus.getDefault().post("");
                    return;
                }
                try {
                    BaseServerBean base = parseObject(resoult, BaseServerBean.class);
                    EventBus.getDefault().post(base);
                } catch (JSONException e) {
                    LogUtils.i("Exception:" + resoult);
                    EventBus.getDefault().post("");
                    e.printStackTrace();
                }

            }
        });

    }

    public static void doGetParams(String urlName, HashMap<String, Object> params, final Class t) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .build();

        StringBuffer sb = new StringBuffer("?");
        if (params != null) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        final Request req = new Request.Builder()
                .url(Constants.BASEURL + "/" + urlName + sb.toString())
                .get()
                .addHeader("Content-Type", "applicationapplication/json")
                .build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                EventBus.getDefault().post("");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resoult = response.body().string();
                if (TextUtils.isEmpty(resoult)) {
                    EventBus.getDefault().post("");
                    return;
                }
                try {
                    BaseServerBean base = parseObject(resoult, BaseServerBean.class);
                    if (base == null) {
                        EventBus.getDefault().post("");
                        return;
                    }
                    if (!TextUtils.equals(base.state, "success")) {
                        EventBus.getDefault().post(base.message);
                        return;
                    }
                    if (t.isArray()) {
                        EventBus.getDefault().post(JSON.parseArray(base.data, t.getComponentType()));
                    } else {
                        EventBus.getDefault().post(parseObject(base.data, t));
                    }
                } catch (JSONException e) {
                    LogUtils.i("Exception:" + resoult);
                    EventBus.getDefault().post("");
                    e.printStackTrace();
                }

            }
        });

    }

}
