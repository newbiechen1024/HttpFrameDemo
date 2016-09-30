package com.newbiechen.httpframedemo.net;

import android.content.Context;
import android.content.res.AssetManager;

import com.newbiechen.httpframedemo.net.base.UrlData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by PC on 2016/9/30.
 * 单例模式
 * 解析xml数据
 */
public final class UrlManager {
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
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String str = null;
            while ((str = br.readLine()) != null){
                sb.append(str);
            }
            sUrlData = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static UrlData parseUrlData(String name){
        ByteArrayInputStream bais = new ByteArrayInputStream(sUrlData.getBytes());
        UrlData urlData = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(bais,"UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        urlData = new UrlData();
                        break;
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
        }
        return urlData;
    }
}
