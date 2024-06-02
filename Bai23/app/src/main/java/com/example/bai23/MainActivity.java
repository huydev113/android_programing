package com.example.bai23;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ListView Lv1;
    ArrayList<List> mylist;
    MyArrayAdapter myadapter;
    String nodeName;
    String title = "";
    String link = "";
    String description = "";
    String des = "";
    String urlStr = "";
    Bitmap mIcon_val = null;
    String URL= "https://vnexpress.net/rss/tin-moi-nhat.rss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Lv1= findViewById(R.id.lv1);
        mylist = new ArrayList<List>();
        myadapter = new MyArrayAdapter (MainActivity.this, R.layout.layout_listview, mylist);
        Lv1.setAdapter(myadapter);
        LoadExampleTask task = new LoadExampleTask();
        task.execute();

        class LoadExampleTask extends AsyncTask<void, void, ArrayList<List>> {

            @Override
            protected ArrayList<List> doInBackground(Void... voids) {
                mylist = new ArrayList<>();
                try {
                    // Tạo đối tượng Parser để chứa dữ liệu từ file XML
                    XmlPullParserFactory fc=XmlPullParserFactory.newInstance();
                    XmlPullParser parser= fc.newPullParser();
                    // Tạo mới và gọi đến phương thức getXmLFromUrL (URL)
                    XMLParse myparser = new XMLParse();
                    String xml = myparser.getXmlFromUrl(URL); // getting XML from URL
                    //Copy dữ liệu từ String xml vào đối tượng parser
                    parser.setInput(new StringReader(xml));
                    // Bắt đầu duyệt parser
                    int eventType=-1;
                    while (eventType!=XmlPullParser.END_DOCUMENT)
                    {
                        eventType=parser.next();
                        switch(eventType)
                        {
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.END_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                nodeName=parser.getName();
                                if (nodeName.equals("title")){ title=parser.nextText();
                                }
                                else if (nodeName.equals("link")){ link parser.nextText();
                                }
                                else if (nodeName.equals("description")){ description=parser.nextText();
                                    try {
                                        urlStr = description.substring((description.indexOf("src=" (description.indexOf("></a") - 2)); } + 5),
                                    catch (Exception e1)
                                    {
                                    }
                                    e1.printStackTrace();
                                    des=
                                            description.substring(description.indexOf("</br>")+5);
                                    try {
                                        URL newurl = new URL(urlStr.toString()+"");
                                        mIcon_val =
                                                BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                break;
                            case XmlPullParser. END_TAG:
                                nodeName=parser.getName();
                                if (nodeName.equals("item")){
                                    mylist.add(new List(mIcon_val, title, des, link));
                                }
                                break;
                        }
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    132
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mylist;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
            } catch (XmlPullParserException e) {
                throw new RuntimeException(e);
            }
        }
    }
}