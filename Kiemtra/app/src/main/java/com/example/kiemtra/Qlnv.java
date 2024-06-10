package com.example.kiemtra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Qlnv extends AppCompatActivity {
    ArrayList<NhanVien> listNv;
    ArrayAdapter<NhanVien> adapterNv;
    ListView lvDv;
    DatabaseReference db;
    Button btnLinkAddNv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlnv);

        db = FirebaseDatabase.getInstance().getReference("Nv");

        listNv = new ArrayList<>();
        adapterNv = new ArrayAdapterListNv(this, listNv);
        lvDv = findViewById(R.id.lvNv);
        lvDv.setAdapter(adapterNv);

        btnLinkAddNv = findViewById(R.id.btnLinkAddNv);

        loadNv();

        btnLinkAddNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Qlnv.this, AddNv.class);
                startActivity(intent);
            }
        });

        lvDv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NhanVien nhanVien = listNv.get(position);
                Intent intent = new Intent(Qlnv.this, DetailNV.class);
                intent.putExtra("id_nv", nhanVien.getMa());
                startActivity(intent);
            }
        });
    }


    private void loadNv() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listNv.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        NhanVien nhanVien = snapshot.getValue(NhanVien.class);
                        listNv.add(nhanVien);
                    }
                    adapterNv.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("tag", "loadUnit:onCancelled", databaseError.toException());
                    Toast.makeText(getApplicationContext(), "Failed to load units.", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }).thenRun(() -> {
            adapterNv.notifyDataSetChanged();
        });
        future.join();
    }
}