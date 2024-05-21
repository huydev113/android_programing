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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText txtId, txtName, txtQuantity;
    Button btnAdd, btnUpdate, btnDelete;
    ListView lv;
    ArrayList<String> myList;
    ArrayAdapter<String> adapter;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtId = findViewById(R.id.txtMaLop);
        txtName = findViewById(R.id.txtTenLop);
        txtQuantity = findViewById(R.id.txtSiSo);
        btnAdd = findViewById(R.id.btnThem);
        btnUpdate = findViewById(R.id.btnSua);
        btnDelete = findViewById(R.id.btnXoa);
        lv = findViewById(R.id.lv);
        myList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        lv.setAdapter(adapter);
        db = openOrCreateDatabase("qlsv.db", MODE_PRIVATE, null);
        try {
            db.execSQL("create table tblop(malop text primary key, tenlop text, siso, integer)");
        } catch (Exception e) {
            Log.e("Error","Table da ton tai");
        }
        loadData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtId.getText().toString().isEmpty() || txtName.getText().toString().isEmpty() || txtQuantity.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                String id = txtId.getText().toString();
                String name = txtName.getText().toString();
                int quantity = Integer.parseInt(txtQuantity.getText().toString());
                if (quantity <= 0) {
                    Toast.makeText(getApplicationContext(), "Sĩ số phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor cursor = db.query("tblop", null, "malop=?", new String[]{id}, null, null, null);
                if (cursor.getCount() > 0) {
                    Toast.makeText(getApplicationContext(), "Mã lớp đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("malop", id);
                contentValues.put("tenlop", name);
                contentValues.put("siso", quantity);
                if (db.insert("tblop", null, contentValues) != -1) {
                    Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    txtId.setText("");
                    txtName.setText("");
                    txtQuantity.setText("");
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
                String arr[] = myList.get(position).split("-");
                txtId.setText(arr[0]);
                txtName.setText(arr[1]);
                txtQuantity.setText(arr[2]);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = txtId.getText().toString();
                if (id.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn mã lớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (db.delete("tblop", "malop=?", new String[]{id}) != 0) {
                    Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    txtId.setText("");
                    txtName.setText("");
                    txtQuantity.setText("");
                    loadData();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtId.getText().toString().isEmpty() || txtName.getText().toString().isEmpty() || txtQuantity.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Không được để trống", Toast.LENGTH_SHORT).show();
                    return ;
                }
                String id = txtId.getText().toString();
                String name = txtName.getText().toString();
                int quantity = Integer.parseInt(txtQuantity.getText().toString());
                if (quantity <= 0) {
                    Toast.makeText(getApplicationContext(), "Sĩ số phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("malop", id);
                contentValues.put("tenlop", name);
                contentValues.put("siso", quantity);
                if (db.update("tblop", contentValues, "malop=?", new String[]{id}) != 0) {
                    Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                    txtId.setText("");
                    txtName.setText("");
                    txtQuantity.setText("");
                    loadData();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Sửa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadData() {
        myList.clear();
        Cursor cursor = db.query("tblop", null, null, null, null, null, "malop DESC");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            int quantity = cursor.getInt(2);
            myList.add(id + "-" + name + "-" + quantity);
            cursor.moveToNext();
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}