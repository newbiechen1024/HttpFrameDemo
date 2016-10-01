package com.newbiechen.httpframedemo.net.request;

import com.newbiechen.httpframedemo.net.base.Request;
import com.newbiechen.httpframedemo.net.base.RequestParameter;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by PC on 2016/9/30.
 */
public class MutipartRequest extends Request{
    private static final String TAG = "FormRequest";
    //设置Boundary
    private static final String ALL_CHAR = "qwertyuiopasdfghjklzxcvbnm1234567890-_QWERTYUIOPASDFGHJKLZXCVBNM";
    private static final int BOUNDARY_SIZE = 30;

    private static final String NEW_LINE= "\r\n";
    private static final String CONTENT_DISPOSITION = "Content-Disposition:form-data;name=";
    private static final String TYPE_OCTET = "application/octet-stream";
    private static final Random RANDOM = new Random();

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private String mBoundary = null;

    public MutipartRequest(String urlPath) {
        super(urlPath, Request.POST);
    }

    @Override
    public List<RequestParameter> getHeaders() {
        List<RequestParameter> parameters = mHeaderParamList;
        //表单上传必须添加的表单头
        RequestParameter parameter = new RequestParameter(
                "Content-Type:mulipart/form-data;","boundary="+getBoundary()
        );
        parameters.add(parameter);
        return Collections.unmodifiableList(parameters);
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

    /*传输文本*/
    public void addPart(String name,String value){
        try {
            OutputStreamWriter osw = new OutputStreamWriter(baos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(getBoundary()+NEW_LINE);
            bw.write(getBodyContentType()+NEW_LINE);
            bw.write(CONTENT_DISPOSITION + name +NEW_LINE);
            bw.write(NEW_LINE);
            bw.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 传输文件内容
     * @param name
     * @param fileName
     * @param file
     */
    public void addPart(String name,String fileName,File file){
        OutputStreamWriter osw = new OutputStreamWriter(baos);
        BufferedWriter bw = new BufferedWriter(osw);
        try {
            bw.write(getBoundary()+NEW_LINE);
            bw.write(getBodyContentType()+NEW_LINE);
            bw.write(CONTENT_DISPOSITION + name + ";filename="+fileName+NEW_LINE);
            bw.write(NEW_LINE);
            bw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}