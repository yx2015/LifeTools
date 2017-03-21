package com.yx.lifetools.ui.a;

import com.yx.baselibrary.utils.TLogUtil;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Administrator on 2017-03-13.
 */

public class SaxHandler extends DefaultHandler {
    private String nodeName;
    private StringBuilder ids;
    private StringBuilder names;
    private StringBuilder versions;

    @Override
    public void startDocument() throws SAXException {
        ids = new StringBuilder();
        names = new StringBuilder();
        versions = new StringBuilder();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        nodeName = localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("app".equals(localName)){
                TLogUtil.e("endElement", "endElement: ids"+ids.toString());
                TLogUtil.e("endElement", "endElement: names"+names.toString());
                TLogUtil.e("endElement", "endElement: versions"+versions.toString());
                ids.setLength(0);
                names.setLength(0);
                versions.setLength(0);
            }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if ("id".equals(nodeName)){
            ids.append(ch,start,length);
        }else if ("name".equals(nodeName))
        {
            names.append(ch,start,length);
        }else if ("version".equals(nodeName)){
            versions.append(ch,start,length);
        }

    }
}
