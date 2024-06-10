package com.example.kiemtra;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AddDv extends AppCompatActivity {
    EditText edtTen, edtEmail, edtWeb, edtDichi, edtSdt;
    Button btnUploadImg, btnAddDV;
    ImageView imgDv;
    DatabaseReference db;
    StorageReference mStorageRef;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dv);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference("Dv");

        edtTen = findViewById(R.id.edtAddTendv);
        edtEmail = findViewById(R.id.edtAddEmail);
        edtWeb = findViewById(R.id.edtAddWebsite);
        edtDichi = findViewById(R.id.edtAddDiachi);
        edtSdt = findViewById(R.id.edtAddSdt);
        imgDv = findViewById(R.id.imgUpdateDv);
        btnUploadImg = findViewById(R.id.btnUploadImg);
        btnAddDV = findViewById(R.id.btnAddDV);

        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnAddDV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(selectedImageUri)
                        .thenAccept(imageUrl -> {
                            // Xử lý khi tải lên thành công và nhận được URL của ảnh
                            String ten = edtTen.getText().toString();
                            String email = edtEmail.getText().toString();
                            String web = edtWeb.getText().toString();
                            String diaChi = edtDichi.getText().toString();
                            String sdt = edtSdt.getText().toString();
                            String logo = imageUrl;
                            DonVi donVi = new DonVi("123", ten, email, web, sdt, logo, diaChi);
                            addDv(donVi);
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
                    .into(imgDv);
        }
    }

    private CompletableFuture<String> uploadImage(Uri imageUri) {
        CompletableFuture<String> uploadTaskFuture = new CompletableFuture<>();

        if (imageUri != null) {
            StorageReference fileRef = mStorageRef.child("imageDV/" + UUID.randomUUID().toString());
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

    private void addDv(DonVi donVi) {
        String ma = db.push().getKey();
        donVi.setMa(ma);

        db.child(ma).setValue(donVi)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddDv.this, "Unit added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MainActivity", "Error adding unit", e);
                        Toast.makeText(AddDv.this, "Failed to add unit", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}