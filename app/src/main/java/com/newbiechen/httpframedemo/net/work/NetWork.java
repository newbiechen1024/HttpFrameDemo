package com.newbiechen.httpframedemo.net.work;

import android.util.Log;

import com.newbiechen.httpframedemo.net.base.Request;
import com.newbiechen.httpframedemo.net.base.RequestParameter;
import com.newbiechen.httpframedemo.net.base.Response;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by PC on 2016/9/29.
 */
public class NetWork implements Runnable {

    public static final int FAIL = 0;
    public static final int SUCCEED= 1;
    private static final String TAG = "NetWork";
    private static final int TIME_OUT = 3000;

    private Request mRequest;
    public NetWork (Request request){
        mRequest = request;
    }

    @Override
    public void run() {
        Response response = null;
        HttpURLConnection connection = null;
        try {
            //设置网络传输的参数
            connection = createHttpConn();
            //获取返回的数据
            response = getResponse(connection);
            //调用分发回调
            mRequest.deliverResponse(SUCCEED,response);
        } catch (IOException e) {
            e.printStackTrace();
            mRequest.deliverResponse(FAIL,response);
        } finally {
            //取消连接
            if (connection != null){
                connection.disconnect();
            }
        }
    }

    private HttpURLConnection createHttpConn() throws IOException{
        URL url = new URL(mRequest.getUrlPath());
        //开启网络连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置连接超时
        conn.setConnectTimeout(TIME_OUT);
        conn.setReadTimeout(TIME_OUT);
        conn.setRequestMethod(mRequest.getRequestType());
        return conn;
    }

    /**
     * 获取提交之后的数据
     * @param conn
     * @throws IOException
     */
    private Response getResponse(HttpURLConnection conn) throws IOException{
        //设置请求头
        for (RequestParameter parameter : mRequest.getHeaders()){
            conn.addRequestProperty(parameter.getKey(),parameter.getValue());
        }
        //发送Post请求的数据
        if (mRequest.getRequestType().equals(Request.POST)){
            sendPostData(conn);
        }
        //读取传输回来的数据
        String data = new String(readData(conn).getBytes(),"UTF-8");
        Log.d(TAG,conn.getResponseCode()+data);
        return  new Response(conn.getResponseCode(),data);
    }

    /**
     * 发送Post请求的数据
     * @param conn
     */
    private void sendPostData(HttpURLConnection conn){
        OutputStream os = null;
        try {
            conn.setDoOutput(true);
            os = conn.getOutputStream();
            os.write(mRequest.getBodyContent().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"输出流读取错误");
        } finally {
            close(os);
        }
    }

    /**
     * 从网络中获取数据，并将数据变成String类型
     * @param connection
     * @return
     */
    private String readData(HttpURLConnection connection){
        InputStream is = null;
        String data = "";
        try {
            is = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String str = null;
            while ((str = br.readLine()) != null){
                stringBuilder.append(str);
            }
            data = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"获取输入流错误");
        }finally {
            close(is);
        }
        return data;
    }

    /**
     * 关闭数据流
     * @param closeable
     */
    private void close (Closeable closeable){
        try {
            if (closeable != null){
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"关闭流出错");
        }
    }
}
