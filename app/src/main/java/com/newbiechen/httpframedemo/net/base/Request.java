package com.newbiechen.httpframedemo.net.base;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.newbiechen.httpframedemo.net.work.NetWork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created by PC on 2016/9/28.
 * 因为创建了Handler所以该类不允许在子线程中创建
 */
public class Request implements Comparable<Request>{

    private static final String TAG = "Request";
    //这里也可以使用枚举，但是个人认为使用枚举类型并没有提供很大的方便。根据能不用枚举就不要用枚举的原则
    public static final String GET = "GET";
    public static final String POST= "POST";
    public static final String OPTIONS = "OPTIONS";
    protected static final String CONTENT_TYPE = "Content-Type:";
    protected static final String TYPE_TEXT = "text/plain;charset=";
    protected static final String DEFAULT_CHARSET = "UTF-8";
    protected final List<RequestParameter> mHeaderParamList = new ArrayList<>();
    protected final List<RequestParameter> mBodyParamList = new ArrayList<>();

    //Handler必须是在主线程中，保证回调在主线程中发生
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private RequestCallback mCallBack;
    private String mUrlPath;

    protected String mCharSet = DEFAULT_CHARSET;
    //默认为GET请求
    protected String mRequestType = GET;
    protected Priority mPriority = Priority.NORMAL;

    public Request(String urlPath,String requestType){
        mUrlPath = urlPath;
        mRequestType = requestType;
    }

    /**
     * 设定优先级
     * @param another
     * @return
     */
    @Override
    public int compareTo(Request another) {
        Priority priority = another.getRequestPriority();
        return mPriority == priority ? 0 : mPriority.getNum() - priority.getNum();
    }


    public interface RequestCallback{
        void onFail();
        void onSucceed(Response response);
    }


    /**
     * 使用枚举类型。
     * 原因：因为之后需要用来优先级的价值
     */
    public enum Priority{
        LOW(1),
        NORMAL(2),
        HIGH(3);
        //表示当前优先级的z值
        private int num;

        Priority(int num){
            this.num = num;
        }

        public int getNum(){
            return num;
        }
    }

    /*********************公共的方法************************************/

    public void setRequestPriority(Priority priority){
        mPriority = priority;
    }

    public void setRequestCallback(RequestCallback callback){
        mCallBack = callback;
    }

    public Request addHeaderParam(String key, String value){
        RequestParameter parameter = new RequestParameter(key,value);
        mHeaderParamList.add(parameter);
        return this;
    }

    public void addHeaderParams(List<RequestParameter> headerParams){
        mHeaderParamList.addAll(headerParams);
    }

    /**
     * 添加参数
     * @param key    参数名
     * @param value  参数值
     */
    public Request addBodyParam(String key, String value){
        RequestParameter parameter = new RequestParameter(key,value);
        mBodyParamList.add(parameter);
        return this;
    }

    public void addRequestParams(List<RequestParameter> bodyParams){
        mBodyParamList.addAll(bodyParams);
    }

    public void setCharset(String charset){
        mCharSet = charset;
    }

    /**
     * 获取请求类型
     * @return
     */
    public String getRequestType() {
        return mRequestType;
    }

    /**
     * 获取请求头的数据
     * */
    public List<RequestParameter> getHeaders(){
        //防止数据被篡改
        return Collections.unmodifiableList(mHeaderParamList);
    }

    public String getUrlPath(){
        //get请求的参数
        if (mRequestType.equals(GET)){
            return getCompleteUrlPath();
        }
        else {
            return mUrlPath;
        }
    }

    /**
     * 设置get请求的明文传输的参数
     * @return
     */
    private String getCompleteUrlPath(){
        return spliceValue(GET);
    }

    /**
     * 拼接参数
     * */
    private String spliceValue(String requestType){
        StringBuilder builder = new StringBuilder();
        if (requestType.equals(GET)){
            builder.append(mUrlPath);
        }
        for(int i = 0; i< mBodyParamList.size(); ++i){
            RequestParameter requestParameter = mBodyParamList.get(i);
            if (i == 0 && requestType.equals(GET)){
                builder.append("?");
            }
            else if (i ==0 && requestType.equals(POST)){
                builder.append("");
            }
            else {
                builder.append("&");
            }
            builder.append(requestParameter.getKey()+"="+requestParameter.getValue());
        }
        Log.d(TAG,builder.toString());
        return builder.toString();
    }

    /**
     * 回调方法
     * */
    public void deliverResponse(final int status, final Response response){
        if (mCallBack != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //status表示连接超时
                    if (status == NetWork.FAIL){
                        mCallBack.onFail();
                    }
                    else {
                        //这表示连接网络成功，但具体状态参数还是根据response来（比如404,之类）
                        mCallBack.onSucceed(response);
                    }
                }
            });
        }
    }

    /**
     * 获取Post请求的参数
     * */
    public String getBodyContent(){
        return spliceValue(POST);
    }

    public String getBodyContentType(){
        return CONTENT_TYPE+TYPE_TEXT+mCharSet;
    }

    public Priority getRequestPriority(){
        return mPriority;
    }
}
