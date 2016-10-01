package com.newbiechen.httpframedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.newbiechen.httpframedemo.net.base.Request;
import com.newbiechen.httpframedemo.net.base.RequestParameter;
import com.newbiechen.httpframedemo.net.base.Response;
import com.newbiechen.httpframedemo.net.request.FormRequest;
import com.newbiechen.httpframedemo.net.utils.RemoteService;
import com.newbiechen.httpframedemo.net.work.RequestQueue;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FormRequest req = new FormRequest("https://www.zhihu.com/login/phone_num");
        req.addBodyParam("_xsrf","efc0404cbcb0f56570ee3bbd317cc4ac")
                .addBodyParam("password","xxxxxxx")
                .addBodyParam("phone_num","xxxxxx")
                .addBodyParam("captcha_type","cn")
                .addBodyParam("remember_me","true");

        Request.RequestCallback callback = new Request.RequestCallback() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSucceed(Response response) {
                Log.d("Activity",response.getStatusCode()+response.getContent());
            }
        };
        req.setRequestCallback(callback);
        RequestQueue.getInstance().addRequest(req);
    }
}
