package com.example.kiemtra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.CompletableFuture;

public class DetailDV extends AppCompatActivity {
    TextView txtTenDv, txtEmail, txtWeb, txtDiaChi, txtSodt;
    DatabaseReference db;
    ImageView imgDv;
    Button btnUpdateDv, btnDeleteDv;
    String maDv;
    String imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dv);

        db = FirebaseDatabase.getInstance().getReference("Dv");

        imgDv = findViewById(R.id.imgDv);
        txtTenDv = findViewById(R.id.txtTenDv);
        txtEmail = findViewById(R.id.txtEmail);
        txtWeb = findViewById(R.id.txtWeb);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtSodt = findViewById(R.id.txtSodt);
        btnUpdateDv = findViewById(R.id.btnUpdateDv);
        btnDeleteDv = findViewById(R.id.btnDeleteDv);

        Intent intent = getIntent();
        String maDv = intent.getStringExtra("id_dv");
        if (maDv != null) {
            getDonVi(maDv).thenAccept(donVi -> {
                runOnUiThread(() -> {
                    imgUrl = donVi.getLogo();
                    txtTenDv.setText(txtTenDv.getText().toString() + " " + donVi.getTen());
                    txtEmail.setText(txtEmail.getText().toString() + " " + donVi.getEmail());
                    txtWeb.setText(txtWeb.getText().toString() + " " + donVi.getWeb());
                    txtDiaChi.setText(txtDiaChi.getText().toString() + " " + donVi.getDiachi());
                    txtSodt.setText(txtSodt.getText().toString() + " " + donVi.getSodienthoai());
                    Glide.with(this)
                            .load(donVi.getLogo()) // Thay getImageUrl() bằng phương thức lấy URL của ảnh từ đối tượng DonVi
                            .into(imgDv);
                });
            }).exceptionally(throwable -> {
                Toast.makeText(getApplicationContext(), "Error getting data" + throwable, Toast.LENGTH_SHORT).show();
                return null;
            });
        }

        btnUpdateDv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailDV.this, UpadteDv.class);
                intent.putExtra("id_dv", maDv);
                startActivity(intent);
            }
        });

        btnDeleteDv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgUrl != null) {
                    FirebaseStorage.getInstance().getReferenceFromUrl(imgUrl).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Xóa ảnh thành công
                            if (maDv != null) {
                                deleteDonViByIdAsync(maDv).thenRun(() -> {
                                    runOnUiThread(() -> {
                                        Toast.makeText(DetailDV.this, "Xóa đơn vị thành công", Toast.LENGTH_SHORT).show();
                                        finish(); // Quay lại màn hình trước đó sau khi xóa thành công
                                    });
                                }).exceptionally(throwable -> {
                                    runOnUiThread(() -> {
                                        Log.e("DetailActivity", "Error deleting unit", throwable);
                                        Toast.makeText(DetailDV.this, "Failed to delete unit", Toast.LENGTH_SHORT).show();
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

    private CompletableFuture<DonVi> getDonVi(String ma) {
        CompletableFuture<DonVi> future = new CompletableFuture<>();
        db.child(ma).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DonVi donVi = dataSnapshot.getValue(DonVi.class);
                if (donVi != null) {
                    future.complete(donVi);
                } else {
                    future.completeExceptionally(new Exception("DonVi not found"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    private CompletableFuture<Void> deleteDonViByIdAsync(String unitId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        db.child(unitId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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