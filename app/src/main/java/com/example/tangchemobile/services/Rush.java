package com.example.tangchemobile.services;


import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Rush {

    private Context parent;
    public Rush(Context parent) {
        this.parent = parent;
    }

    public void SetUrl(String url) {
        this.url = url;
    }

    public boolean Put(String src , String value) {
        RequestParams rp = new RequestParams();
        rp.add("src", src);
        rp.add("value", value);

        http.put(this.url, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(parent, "推送失败:" + String.valueOf(statusCode), Toast.LENGTH_SHORT).show();
            }

        });

        return true;
    }

    private String url = "";
}
