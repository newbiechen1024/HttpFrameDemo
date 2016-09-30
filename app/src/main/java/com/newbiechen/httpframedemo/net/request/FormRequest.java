package com.newbiechen.httpframedemo.net.request;

import com.newbiechen.httpframedemo.net.base.Request;
import com.newbiechen.httpframedemo.net.base.RequestParameter;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by PC on 2016/9/30.
 * 模拟Form表单上传
 */
public class FormRequest extends Request {
    private static final String TAG = "FormRequest";
    //设置Boundary
    private static final String ALL_CHAR = "qwertyuiopasdfghjklzxcvbnm1234567890-_QWERTYUIOPASDFGHJKLZXCVBNM";
    private static final int BOUNDARY_SIZE = 30;

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final String NEW_LINE= "\r\n";
    private static final String CONTENT_TYPE = "Content-Type:text/plain;charset=";
    private static final String CONTENT_DISPOSITION = "Content-Disposition:form-data;name=";

    private static final Random RANDOM = new Random();

    private String mCharSet = DEFAULT_CHARSET;
    private String mBoundary = null;

    public FormRequest(String urlPath) {
        super(urlPath, Request.POST);
    }

    @Override
    public List<RequestParameter> getHeaders() {
        List<RequestParameter> parameters = mHeaderParam;
        //表单上传必须添加的表单头
        RequestParameter parameter = new RequestParameter(
                "Content-Type:mulipart/form-data;","boundary="+getBoundary()
        );
        parameters.add(parameter);
        return Collections.unmodifiableList(parameters);
    }

    /**
     * Post请求的内容
     * @return
     */
    @Override
    public String getBodyContent() {
        StringBuilder builder = new StringBuilder();
        for(RequestParameter parameter : mRequestParam){
            builder.append("--"+getBoundary()+NEW_LINE);
            builder.append(CONTENT_DISPOSITION+parameter.getKey()+NEW_LINE);
            builder.append(CONTENT_TYPE+mCharSet+NEW_LINE);
            builder.append(NEW_LINE);
            builder.append(parameter.getValue()+NEW_LINE);
        }
        builder.append("--"+getBoundary()+"--");
        return builder.toString();
    }

    private String getBoundary(){
        if (mBoundary == null){
            char [] charset = ALL_CHAR.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<BOUNDARY_SIZE; ++i){
                sb.append(charset[RANDOM.nextInt(charset.length)]);
            }
            mBoundary = sb.toString();
        }
        return mBoundary;
    }

    /***************************公共方法***************************/

    public void setCharset(String charset){
        mCharSet = charset;
    }

}
