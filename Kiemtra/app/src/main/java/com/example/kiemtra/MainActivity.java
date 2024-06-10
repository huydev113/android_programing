package com.example.kiemtra;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocalActivityManager localActivityManager = new LocalActivityManager(this, false);
        localActivityManager.dispatchCreate(savedInstanceState);

        TabHost tabHost = findViewById(R.id.tabhost);
        tabHost.setup(localActivityManager);

        // Create the tabs and associate them with the activities
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1")
                .setIndicator("Đơn vị")
                .setContent(new Intent(this, Qldv.class));
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2")
                .setIndicator("Nhân viên")
                .setContent(new Intent(this, Qlnv.class));

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.setCurrentTab(0);
    }
}