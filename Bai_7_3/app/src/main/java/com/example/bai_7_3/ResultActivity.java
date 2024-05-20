package com.example.bai_7_3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {
    EditText txtA, txtB;
    Button btnSum, btnSub;
    Intent myIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        txtA = findViewById(R.id.txtAcResultA);
        txtB = findViewById(R.id.txtAcResultB);
        btnSum = findViewById(R.id.btnResponseSum);
        btnSub = findViewById(R.id.btnResponseSub);
        myIntent = getIntent();
        Bundle myBundle  = myIntent.getBundleExtra("myBackage");
        int a = myBundle.getInt("a");
        int b = myBundle.getInt("b");
        txtA.setText(a+"");
        txtB.setText(b+"");
        btnSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sum = a + b;
                myIntent.putExtra("result", sum);
                setResult(34, myIntent);
                finish();
            }
        });
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sub = a - b;
                myIntent.putExtra("result", sub);
                setResult(33, myIntent);
                finish();
            }
        });
    }
}