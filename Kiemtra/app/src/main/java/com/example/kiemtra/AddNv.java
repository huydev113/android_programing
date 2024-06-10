package com.example.kiemtra;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AddNv extends AppCompatActivity {
    FirebaseDatabase db;
    EditText edtTen, edtEmail, edtChucvu, edtSodt;
    Button btnUpload, btnAddNv;
    ImageView imgAvatar;
    StorageReference mStorageRef;
    Uri selectedImageUri;
    Spinner spinner;
    ArrayList<DonVi> listDv;
    DonVi dv = new DonVi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nv);

        db = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        spinner = findViewById(R.id.spinner);
        edtTen = findViewById(R.id.edtTennv);
        edtEmail = findViewById(R.id.edtEmail);
        edtChucvu = findViewById(R.id.edtChucvu);
        edtSodt = findViewById(R.id.edtSodt);
        btnAddNv = findViewById(R.id.btnAddNv);
        btnUpload = findViewById(R.id.btnUpload);
        imgAvatar = findViewById(R.id.imgAvatar);

        listDv = new ArrayList<>();

        getAllUnits().thenAccept(unitList -> {
            runOnUiThread(() -> {
                // Hiển thị danh sách đơn vị trong Spinner
                ArrayAdapter<DonVi> adapter = new ArrayAdapter<>(AddNv.this,
                        android.R.layout.simple_spinner_item, listDv);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dv = listDv.get(position);
                    }
                });

            });
        }).exceptionally(exception -> {
            return null;
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnAddNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(selectedImageUri)
                        .thenAccept(imageUrl -> {
                            // Xử lý khi tải lên thành công và nhận được URL của ảnh
                            String ten = edtTen.getText().toString();
                            String email = edtEmail.getText().toString();
                            String chucvu = edtChucvu.getText().toString();
                            String sdt = edtSodt.getText().toString();
                            String anh = imageUrl;
                            NhanVien nhanVien = new NhanVien("123", ten, email, chucvu, sdt, anh, dv.getMa());
                            addNv(nhanVien);
                        })
                        .exceptionally(e -> {
                            // Xử lý khi xảy ra lỗi
                            Log.e("MainActivity", "Error uploading image", e);
                            return null;
                        });
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            Glide.with(this)
                    .load(selectedImageUri)
                    .into(imgAvatar);
        }
    }

    private CompletableFuture<List<DonVi>> getAllUnits() {
        CompletableFuture<List<DonVi>> future = new CompletableFuture<>();

        db.getReference("Dv").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonVi unit = snapshot.getValue(DonVi.class);
                    if (unit != null) {
                        listDv.add(unit);
                    }
                }
                future.complete(listDv);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    private CompletableFuture<String> uploadImage(Uri imageUri) {
        CompletableFuture<String> uploadTaskFuture = new CompletableFuture<>();

        if (imageUri != null) {
            StorageReference fileRef = mStorageRef.child("imageNv/" + UUID.randomUUID().toString());
            UploadTask uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    uploadTaskFuture.complete(task.getResult().toString());
                } else {
                    uploadTaskFuture.completeExceptionally(task.getException());
                }
            });
        } else {
            uploadTaskFuture.completeExceptionally(new IllegalArgumentException("Image URI is null"));
        }

        return uploadTaskFuture;
    }

    private void addNv(NhanVien nhanVien) {
        String ma = db.getReference("Nv").push().getKey();
        nhanVien.setMa(ma);

        db.getReference("Nv").child(ma).setValue(nhanVien)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddNv.this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MainActivity", "Error adding unit", e);
                        Toast.makeText(AddNv.this, "Failed to add unit", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}