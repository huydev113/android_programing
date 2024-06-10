package com.example.kiemtra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.concurrent.CompletableFuture;

public class DetailNV extends AppCompatActivity {
    TextView txtTenNv, txtEmailNv, txtChucvuNv, txtMaDv, txtSodt;
    FirebaseDatabase db;
    ImageView imgNv;
    Button btnUpadateNv, btnDeleteNv;
    String maNv;
    String imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_nv);

        db = FirebaseDatabase.getInstance();

        imgNv = findViewById(R.id.imgNv);
        txtTenNv = findViewById(R.id.txtTenNv);
        txtEmailNv = findViewById(R.id.txtEmailNv);
        txtChucvuNv = findViewById(R.id.txtChucvuNv);
        txtMaDv = findViewById(R.id.txtMaDv);
        txtSodt = findViewById(R.id.txtSodt);
        btnUpadateNv = findViewById(R.id.btnUpadateNv);
        btnDeleteNv = findViewById(R.id.btnDeleteNv);

        Intent intent = getIntent();
        maNv = intent.getStringExtra("id_nv");
        if (maNv != null) {
            getNhanVien(maNv).thenAccept(nhanvien -> {
                runOnUiThread(() -> {
                    db.getReference("Dv").child(nhanvien.getMadonvi()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DonVi unit = dataSnapshot.getValue(DonVi.class);
                            imgUrl = nhanvien.getAnh();
                            txtTenNv.setText(txtTenNv.getText().toString() + " " + nhanvien.getHoten());
                            txtEmailNv.setText(txtEmailNv.getText().toString() + " " + nhanvien.getEmail());
                            txtChucvuNv.setText(txtChucvuNv.getText().toString() + " " + nhanvien.getChucvu());
                            txtMaDv.setText(txtMaDv.getText().toString() + " " + unit.getTen());
                            txtSodt.setText(txtSodt.getText().toString() + " " + nhanvien.getSodienthoai());
                            Glide.with(DetailNV.this)
                                    .load(nhanvien.getAnh()) // Thay getImageUrl() bằng phương thức lấy URL của ảnh từ đối tượng DonVi
                                    .into(imgNv);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                });
            }).exceptionally(throwable -> {
                Toast.makeText(getApplicationContext(), "Error getting data" + throwable, Toast.LENGTH_SHORT).show();
                return null;
            });
        }

        btnUpadateNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnDeleteNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgUrl != null) {
                    FirebaseStorage.getInstance().getReferenceFromUrl(imgUrl).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Xóa ảnh thành công
                            if (maNv != null) {
                                deleteNhanvienByIdAsync(maNv).thenRun(() -> {
                                    runOnUiThread(() -> {
                                        Toast.makeText(DetailNV.this, "Xóa nhân viên thành công", Toast.LENGTH_SHORT).show();
                                        finish(); // Quay lại màn hình trước đó sau khi xóa thành công
                                    });
                                }).exceptionally(throwable -> {
                                    runOnUiThread(() -> {
                                        Log.e("DetailActivity", "Error deleting unit", throwable);
                                        Toast.makeText(DetailNV.this, "Failed to delete unit", Toast.LENGTH_SHORT).show();
                                    });
                                    return null;
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Xóa ảnh thất bại
                            Log.e("Firebase", "Failed to delete image", exception);
                            Toast.makeText(getApplicationContext(), "Failed to delete image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private CompletableFuture<NhanVien> getNhanVien(String ma) {
        CompletableFuture<NhanVien> future = new CompletableFuture<>();
        db.getReference("Nv").child(ma).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NhanVien nhanvien = dataSnapshot.getValue(NhanVien.class);
                if (nhanvien != null) {
                    future.complete(nhanvien);
                } else {
                    future.completeExceptionally(new Exception("Nhanvien not found"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    private CompletableFuture<Void> deleteNhanvienByIdAsync(String ma) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        db.getReference("Nv").child(ma).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    future.complete(null); // Hoàn thành CompletableFuture với kết quả thành công
                } else {
                    future.completeExceptionally(task.getException()); // Hoàn thành CompletableFuture với ngoại lệ
                }
            }
        });
        return future;
    }
}