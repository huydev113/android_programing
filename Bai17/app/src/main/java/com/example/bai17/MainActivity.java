package com.example.bai17;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="qlsach.db";
    //Khai báo ListView
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processCopy();
            database = openOrCreateDatabase ("qlsach.db", MODE_PRIVATE, null);
            lv = findViewById(R.id.lv);
            mylist = new ArrayList<>();
            myadapter = new ArrayAdapter<> (MainActivity.this,
                    android.R.layout.simple_list_item_1, mylist);
            lv.setAdapter (myadapter);
// Truy vấn CSDL và cập nhật hiển thị lên Listview
            Cursor c = database.query("tbsach",null,null,null,null,null,null);
            c.moveToFirst();
            String data ="";
            while (c.isAfterLast() == false)
            {
                data = c.getString(0)+"-"+c.getString(1)+"-"+c.getString(2); mylist.add(data);
                c.moveToNext();
                c.close();
                myadapter.notifyDataSetChanged();
            }
    }

    private void processCopy() {
//private app
        File dbFile = getDatabasePath (DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }

    public void CopyDataBaseFromAsset() {
// TODO Auto-generated method stub.
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
// Path to the just created empty db
            String outFileName = getDatabasePath();
// if the path doesn't exist first, create it
            File f= new File (getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
// Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream (outFileName);
// transfer bytes from the inputfile to the outputfile
// Truyền bytes dữ liệu từ input đến output
            int size = myInput.available();
            byte[] buffer = new byte [size];
            myInput.read(buffer);
            myOutput.write(buffer);
// Close the streams
            myOutput.flush();
            myOutput.close(); myInput.close();
        } catch (IOException e) {
// TODO Auto-generated catch block e.printStackTrace();
        }
    }
}