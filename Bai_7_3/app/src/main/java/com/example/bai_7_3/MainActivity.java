package com.example.bai_7_3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText txtA, txtB, txtResult;
    Button btnRequestResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtA = findViewById(R.id.txtA);
        txtB = findViewById(R.id.txtB);
        txtResult = findViewById(R.id.txtResult);
        btnRequestResult = findViewById(R.id.btnRequestResult);
        btnRequestResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ResultActivity.class);
                int a = Integer.parseInt(txtA.getText().toString());
                int b = Integer.parseInt(txtB.getText().toString());
                Bundle myBundle = new Bundle();
                myBundle.putInt("a", a);
                myBundle.putInt("b", b);
                myIntent.putExtra("myBackage", myBundle);
                startActivityForResult(myIntent, 99);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == 34) {
            int sum = data.getIntExtra("result", 0);
            txtResult.setText(sum+"");
        }
        else if (requestCode == 99 && resultCode == 33) {
            int sub = data.getIntExtra("result", 0);
            txtResult.setText(sub+"");
        }
    }
}