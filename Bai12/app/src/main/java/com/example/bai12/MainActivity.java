package com.example.bai12;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView txt1, txtdate;
    ListView lv;
    ArrayList<String> arraywork;
    ArrayAdapter<String> arrAdapater;
    EditText edtwork,edth,edtm;
    Button btnwork;

    String arr1[]= {"Hàng Điện tử","Hàng Hóa Chất",
            "Hàng Gia dụng","Hàng xây dựng"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bai 1
//        setContentView(R.layout.bai1_activity);
//        txt1 = findViewById(R.id.txt1);
//        final String arr[] =  {"Iphone 7", "SamSung Galaxy S7", "Nokia Lumia 730", "Sony Xperia XZ",
//                "HTC One E9"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, arr);
//        ListView lv1 = findViewById(R.id.lv1);
//        lv1.setAdapter(adapter);
//        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                txt1.setText("Vị trí "+position+ " : "+arr[position]);
//            }
//        });

        // Bai 2
//        setContentView(R.layout.bai2_activity);
//        arraywork = new ArrayList<>();
//        arrAdapater = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arraywork);
//        lv.setAdapter(arrAdapater);
//        Date currentDate = Calendar.getInstance().getTime();
//        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
//        txtdate.setText("Hôm Nay: "+simpleDateFormat.format(currentDate));
//        btnwork.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(edtwork.getText().toString().equals("")|| edth.getText().toString().equals("")
//                        ||edtm.getText().toString().equals("")) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    builder.setTitle("Info missing");
//                    builder.setMessage("Please enter all information of the work");
//                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                        }
//                    });
//                    builder.show();
//                }
//                else {
//                    String str = edtwork.getText().toString() + " - "+
//                            edth.getText().toString() + ":"+edtm.getText().toString();
//                    arraywork.add(str);
//                    arrAdapater.notifyDataSetChanged();
//                    edth.setText("");
//                    edtm.setText("");
//                    edtwork.setText("");
//                }
//            }
//        });

        // Bai 3
        setContentView(R.layout.bai3_activity);
        txt1 = (TextView) findViewById(R.id.txt1);
        Spinner spin1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_spinner_item,arr1);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spin1.setAdapter(adapter1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                txt1.setText(arr1[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
}