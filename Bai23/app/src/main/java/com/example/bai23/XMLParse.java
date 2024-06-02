package com.example.bai23;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.ClientProtocolException;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils;

import java.io.IOException;

public class XMLParse {
    public String getXmlFromUrl(String url) {
        String xml = null;
        try {
            // Tạo đối tượng Client và tạo Http Connetction
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url); //Tiến hành Request Lên server và nhận đáp ứng Response
            HttpResponse httpResponse = httpClient.execute(httpPost); //Lấy các Thực thể trong đáp ứng Response chuyến qua kiểu String vàgắn vào file xml
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString (httpEntity);
        } catch (ClientProtocolException e) { e.printStackTrace();
        } catch (IOException e) { e.printStackTrace(); }
// return XML
        return xml;
    }
}
