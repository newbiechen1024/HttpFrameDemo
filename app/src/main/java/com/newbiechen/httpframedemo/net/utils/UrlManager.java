package com.newbiechen.httpframedemo.net.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.newbiechen.httpframedemo.net.base.UrlData;
import com.newbiechen.httpframedemo.utils.IOUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by PC on 2016/9/30.
 * 单例模式
 * 解析xml数据
 */
public final class UrlManager {
    private static final String TAG = "UrlManager";

    private static Context sContext;
    private static String sUrlData;
    private UrlManager(Context context){
        sContext = context;
    }

    public static void init(Context context){
        if (sContext == null){
            sContext = context;
            setUrlData();
        }
    }


    private static void setUrlData(){
        AssetManager assetManager = sContext.getAssets();
        InputStream is = null;
        try {
            is = assetManager.open("url");
            sUrlData = IOUtils.input2Str(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(is);
        }
    }
    public static UrlData parseUrlData(String name){
        ByteArrayInputStream bais = new ByteArrayInputStream(sUrlData.getBytes());
        UrlData urlData = new UrlData();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(bais,"UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("Node") &&
                                parser.getAttributeValue(null,"name").equals(name)){
                            urlData.setUrlPath(parser.getAttributeValue(null,"url"));
                            urlData.setRequestType(parser.getAttributeValue(null,"type"));
                            urlData.setExpired(Long.valueOf(parser.getAttributeValue(null,"expired")));
                            //终止循环
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(bais);
        }

        Log.d(TAG, urlData.toString());
        return urlData;
    }
}
