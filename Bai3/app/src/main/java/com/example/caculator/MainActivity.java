package com.example.caculator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText txtSoA, txtSoB, txtKetQua;
    Button btnTong, btnHieu, btnTich, btnThuong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        txtSoA = findViewById(R.id.txtSoA);
        txtSoB = findViewById(R.id.txtSoB);
        txtKetQua = findViewById(R.id.txtKetQua);
        btnTong = findViewById(R.id.btnTong);
        btnHieu = findViewById(R.id.btnHieu);
        btnTich = findViewById(R.id.btnTich);
        btnThuong = findViewById(R.id.btnThuong);

        // Handle event
        btnTong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt("0"+txtSoA.getText());
                int b = Integer.parseInt("0"+txtSoB.getText());

                if(a == 0) txtSoA.setText("0");
                if(b == 0) txtSoB.setText("0");
                txtKetQua.setText(String.valueOf(a+b));
            }
        });

        btnHieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt("0"+txtSoA.getText());
                int b = Integer.parseInt("0"+txtSoB.getText());

                if(a == 0) txtSoA.setText("0");
                if(b == 0) txtSoB.setText("0");

                txtKetQua.setText(String.valueOf(a-b));
            }
        });

        btnTich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt("0"+txtSoA.getText());
                int b = Integer.parseInt("0"+txtSoB.getText());
                if(a == 0) txtSoA.setText("0");
                if(b == 0) txtSoB.setText("0");
                txtKetQua.setText(String.valueOf(a*b));
            }
        });

        btnThuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt("0"+txtSoA.getText());
                int b = Integer.parseInt("0"+txtSoB.getText());
                if(a == 0) txtSoA.setText("0");
                if(b == 0) {txtKetQua.setText("B phai khac 0"); return;};
                txtKetQua.setText(String.valueOf(a/b));
            }
        });
    }
}