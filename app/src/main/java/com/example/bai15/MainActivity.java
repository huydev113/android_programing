package com.example.bai15;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText txtMaLop, txtTenLop, txtSiSo;
    Button btnThem, btnSua, btnXoa, btnTruyVan;
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMaLop = findViewById(R.id.txtMaLop);
        txtTenLop = findViewById(R.id.txtTenLop);
        txtSiSo = findViewById(R.id.txtSiSo);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnXoa = findViewById(R.id.btnXoa);
        btnTruyVan = findViewById(R.id.btnTruyVan);
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        mydatabase = openOrCreateDatabase("qlsinhvien.db", MODE_PRIVATE, null);
        try {
            String sql = "Create table tbllop(malop text primary key, tenlop text, siso integer)";
            mydatabase.execSQL(sql);
        } catch (Exception e) {
            Log.e("Error","Table da ton tai");
        }
        loadData();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtMaLop.getText().toString().isEmpty() || txtMaLop.getText().toString().isEmpty() || txtSiSo.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                String malop = txtMaLop.getText().toString();
                String tenlop = txtTenLop.getText().toString();
                int siso = Integer.parseInt("0" + txtSiSo.getText().toString());
                if(siso <= 0) {
                    Toast.makeText(getApplicationContext(), "Sĩ số phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor c = mydatabase.query("tbllop", null, "malop=?", new String[]{malop}, null, null, null);
                if(c.getCount() > 0) {
                    Toast.makeText(getApplicationContext(), "Mã lớp đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues row = new ContentValues();
                row.put("malop", malop);
                row.put("tenlop", tenlop);
                row.put("siso", siso);

                if(mydatabase.insert("tbllop", null, row) != -1) {
                    Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    txtMaLop.setText("");
                    txtTenLop.setText("");
                    txtSiSo.setText("");
                    loadData();
                }

                else {
                    Toast.makeText(getApplicationContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String arr[] = mylist.get(position).split("-");
                txtMaLop.setText(arr[0]);
                txtTenLop.setText(arr[1]);
                txtSiSo.setText(arr[2]);
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String malop = txtMaLop.getText().toString();
                if(malop.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Mã lớp không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mydatabase.delete("tbllop", "malop=?", new String[]{malop}) != 0) {
                    Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    txtMaLop.setText("");
                    txtTenLop.setText("");
                    txtSiSo.setText("");
                    loadData();
                } else {
                    Toast.makeText(getApplicationContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtMaLop.getText().toString().isEmpty() || txtMaLop.getText().toString().isEmpty() || txtSiSo.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                String malop = txtMaLop.getText().toString();
                String tenlop = txtTenLop.getText().toString();
                int siso = Integer.parseInt("0" + txtSiSo.getText().toString());
                if(siso <= 0) {
                    Toast.makeText(getApplicationContext(), "Sĩ số phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues row = new ContentValues();
                row.put("malop", malop);
                row.put("tenlop", tenlop);
                row.put("siso", siso);

                if(mydatabase.update("tbllop", row, "malop=?", new String[]{malop}) != 0) {
                    Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                    txtMaLop.setText("");
                    txtTenLop.setText("");
                    txtSiSo.setText("");
                    loadData();
                }

                else {
                    Toast.makeText(getApplicationContext(), "Sửa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTruyVan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void loadData() {
        mylist.clear();
        Cursor c = mydatabase.query("tbllop", null, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String malop = c.getString(0);
            String tenlop = c.getString(1);
            int siso = c.getInt(2);
            mylist.add(malop + " - " + tenlop + " - " + siso);
            c.moveToNext();
        }
        c.close();
        myadapter.notifyDataSetChanged();
    }
}