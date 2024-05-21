package com.example.bai15;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    }
}