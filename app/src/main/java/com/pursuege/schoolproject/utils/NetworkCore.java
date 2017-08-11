package com.pursuege.schoolproject.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONException;
import com.pursuege.schoolproject.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangtao on 2017/8/11.
 */

public class NetworkCore {
    public static void doPostParams(String urlName, HashMap<String, Object> params, final Class t) {
        if (TextUtils.isEmpty(urlName)) {
            return;
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.TIMEOUT, TimeUnit.SECONDS)
                .build();
        StringBuffer sb = new StringBuffer("?");
        FormBody.Builder formbody = new FormBody.Builder();
        if (params != null) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key));
                sb.append("&");
                formbody.add(key, params.get(key) + "");
            }
            sb.deleteCharAt(sb.length() - 1);
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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException, JSONException {
                String resoult = response.body().string();

            }
        });

    }

    public static final MediaType JSON_TYPE = MediaType.parse("application/json");
}
