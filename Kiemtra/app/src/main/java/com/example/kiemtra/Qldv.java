package com.example.kiemtra;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Qldv extends AppCompatActivity {
    ArrayList<DonVi> listDv;
    ArrayAdapterListDv arrayAdapterListDv;
    ListView lvDv;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference db;
    Button btnLinkAdd;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qldv);
        firebaseDatabase = FirebaseDatabase.getInstance();
        db = firebaseDatabase.getReference("Dv");
        lvDv = findViewById(R.id.lvDv);
        listDv = new ArrayList<>();
        arrayAdapterListDv = new ArrayAdapterListDv(this, listDv);
        lvDv.setAdapter(arrayAdapterListDv);
        btnLinkAdd = findViewById(R.id.btnLinkAdd);

        loadDv();

        btnLinkAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Qldv.this, AddDv.class);
                startActivity(intent);
            }
        });

        lvDv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DonVi donVi = listDv.get(position);
                Intent intent = new Intent(Qldv.this, DetailDV.class);
                intent.putExtra("id_dv", donVi.getMa());
                startActivity(intent);
            }
        });
    }

    private void loadDv() {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listDv.clear();
                    for (DataSnapshot donviSnapshot : dataSnapshot.getChildren()) {
                        DonVi donVi = donviSnapshot.getValue(DonVi.class);
                        listDv.add(donVi);
                    }
                    arrayAdapterListDv.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("tag", "loadUnit:onCancelled", databaseError.toException());
                    Toast.makeText(getApplicationContext(), "Failed to load units.", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }).thenRun(() -> {
            arrayAdapterListDv.notifyDataSetChanged();
        });
        future.join();
    }

}