package com.yx.lifetools.ui.a;

import android.content.Context;
import android.content.SharedPreferences;

import com.yx.baselibrary.utils.TLogUtil;
import com.yx.lifetools.R;
import com.yx.lifetools.manager.BaseApplication;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Administrator on 2017-03-13.
 */

public class ParseXml {
    public ParseXml() {
    }

    public void pullParseXML(String xmlData) {

        try {
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            StringReader stringReader = new StringReader(xmlData);

            xmlPullParser.setInput(stringReader);
            int eventType = xmlPullParser.getEventType();
            String id = null;
            String name = null;
            String version = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        }
                        if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        }
                        if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("app".equals(nodeName)) {
                            TLogUtil.e("pullParseXML", "pullParseXML: id is " + id);
                            TLogUtil.e("pullParseXML", "pullParseXML: name is " + name);
                            TLogUtil.e("pullParseXML", "pullParseXML: version is " + version);
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveXml() {
        SharedPreferences sharePreference = BaseApplication.getAppContext().getSharedPreferences("app", Context.MODE_APPEND);
        SharedPreferences.Editor editor = sharePreference.edit();

        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("<apps>");
        strBuilder.append("\n");
        for (int i = 0; i < 3; i++) {
            strBuilder.append("<app>");
            strBuilder.append("\n");
            strBuilder.append("<id>");
            strBuilder.append(i);
            strBuilder.append("</id>");
            strBuilder.append("\n");
            strBuilder.append("<name>");
            strBuilder.append("" + i + "+");
            strBuilder.append("</name>");
            strBuilder.append("\n");
            strBuilder.append("<version>");
            strBuilder.append(i * 2.0);
            strBuilder.append("</version>");
            strBuilder.append("\n");
            strBuilder.append("</app>");
            strBuilder.append("\n");
        }
        strBuilder.append("</apps>");
        editor.putString("xml", strBuilder.toString());
        editor.commit();

    }


    public String getXml() {
        SharedPreferences sharePreference = BaseApplication.getAppContext().getSharedPreferences("app", Context.MODE_APPEND);
        return sharePreference.getString("xml", "");
    }

    public void saxParseXml(String xmlData) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
            SaxHandler saxHandler = new SaxHandler();
            xmlReader.setContentHandler(saxHandler);
            xmlReader.parse(new InputSource(new StringReader(xmlData)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseXml(Context context) {
//        InputStream is = context.getClassLoader().getResourceAsStream("app.xml");
        //实例化一个XmlPullParser对象
        //设置输入流和编码
        try {
            InputStream is = context.getResources().openRawResource(R.raw.app);
            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(is, "UTF-8");
            //触发了第一个事件，根据XML的语法，也就是从他开始了解文档
            int eventCode = xmlPullParser.getEventType();

            String id = null;
            String name = null;
            String version = null;

            while (eventCode != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventCode) {
                    case XmlPullParser.START_TAG:
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        }
                        if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        }
                        if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("app".equals(nodeName)) {
                            TLogUtil.e("parseXml", "parseXml: id is " + id);
                            TLogUtil.e("parseXml", "parseXml: name is " + name);
                            TLogUtil.e("parseXml", "parseXml: version is " + version);
                        }
                        break;
                }
                eventCode = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saxPull(Context context){
        InputStream is = context.getResources().openRawResource(R.raw.app);
        //通过获取到的InputStream来得到InputSource实例
        InputSource is2 = new InputSource(is);
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
            SaxHandler saxHandler = new SaxHandler();
            xmlReader.setContentHandler(saxHandler);
            xmlReader.parse(is2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
