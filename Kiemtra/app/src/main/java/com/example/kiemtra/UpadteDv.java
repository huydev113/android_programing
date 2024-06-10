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
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UpadteDv extends AppCompatActivity {
    EditText edtTendv, edtEmail, edtWebsite, edtDiachi, edtSdt;
    DatabaseReference db;
    StorageReference mStorageRef;
    ImageView imgUpdateDv;
    Button btnChooseLogo, btnSaveDv;
    String maDv;
    Uri selectedImageUri;
    String imgUrl;
    Boolean uploadImage = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upadte_dv);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseDatabase.getInstance().getReference("Dv");

        Intent intent = getIntent();
        maDv = intent.getStringExtra("id_dv");

        imgUpdateDv = findViewById(R.id.imgUpdateDv);
        edtTendv = findViewById(R.id.edtUpdateTendv);
        edtEmail = findViewById(R.id.edtEmail);
        edtWebsite = findViewById(R.id.edtWebsite);
        edtDiachi = findViewById(R.id.edtDiachi);
        edtSdt = findViewById(R.id.edtSdt);
        btnChooseLogo = findViewById(R.id.btnChooseLogo);
        btnSaveDv = findViewById(R.id.btnSaveDv);

        getDonVi(maDv).thenAccept(donVi -> {
            runOnUiThread(() -> {
                imgUrl = donVi.getLogo();
                edtTendv.setText(edtTendv.getText().toString() + " " + donVi.getTen());
                edtEmail.setText(edtEmail.getText().toString() + " " + donVi.getEmail());
                edtWebsite.setText(edtWebsite.getText().toString() + " " + donVi.getWeb());
                edtDiachi.setText(edtDiachi.getText().toString() + " " + donVi.getDiachi());
                edtSdt.setText(edtSdt.getText().toString() + " " + donVi.getSodienthoai());
                Glide.with(this)
                        .load(donVi.getLogo()) // Thay getImageUrl() bằng phương thức lấy URL của ảnh từ đối tượng DonVi
                        .into(imgUpdateDv);
            });
        }).exceptionally(throwable -> {
            Toast.makeText(getApplicationContext(), "Error getting data" + throwable, Toast.LENGTH_SHORT).show();
            return null;
        });

        btnChooseLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnSaveDv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadImage) {
                    deleteImageFromFirebase(imgUrl).thenAccept(r -> {
                        // Xóa ảnh thành công
                        uploadImage(selectedImageUri)
                                .thenAccept(imageUrl -> {
                                    // Xử lý khi tải lên thành công và nhận được URL của ảnh
                                    String ten = edtTendv.getText().toString();
                                    String email = edtEmail.getText().toString();
                                    String web = edtWebsite.getText().toString();
                                    String diaChi = edtDiachi.getText().toString();
                                    String sdt = edtSdt.getText().toString();
                                    String logo = imageUrl;
                                    DonVi donVi = new DonVi(maDv, ten, email, web, sdt, logo, diaChi);
                                    updateUnitById(donVi).thenAccept(c -> {
                                        finish();
                                    }).exceptionally(e -> {
                                        // Xử lý khi xảy ra lỗi
                                        Log.e("MainActivity", "Error uploading image", e);
                                        return null;
                                    });;
                                })
                                .exceptionally(e -> {
                                    // Xử lý khi xảy ra lỗi
                                    Log.e("MainActivity", "Error uploading image", e);
                                    return null;
                                });
                    }).exceptionally(exception -> {
                        // Xóa ảnh thất bại
                        runOnUiThread(() -> {
                            Log.e("MainActivity", "Failed to delete image", exception);
                            Toast.makeText(UpadteDv.this, "Failed to delete image", Toast.LENGTH_SHORT).show();
                        });
                        return null;
                    });
                }
                else {
                    String ten = edtTendv.getText().toString();
                    String email = edtEmail.getText().toString();
                    String web = edtWebsite.getText().toString();
                    String diaChi = edtDiachi.getText().toString();
                    String sdt = edtSdt.getText().toString();
                    String logo = imgUrl;
                    DonVi donVi = new DonVi(maDv, ten, email, web, sdt, logo, diaChi);
                    updateUnitById(donVi).thenAccept(c -> {
                        finish();
                    }).exceptionally(e -> {
                        // Xử lý khi xảy ra lỗi
                        Log.e("MainActivity", "Error uploading image", e);
                        return null;
                    });;
                }
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
                    .into(imgUpdateDv);
            uploadImage = true;
        }
    }

    private CompletableFuture<Void> deleteImageFromFirebase(String image) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseStorage.getInstance().getReferenceFromUrl(image).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                future.complete(null); // Xóa thành công, hoàn thành CompletableFuture
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                future.completeExceptionally(exception); // Xóa thất bại, hoàn thành CompletableFuture với ngoại lệ
            }
        });

        return future;
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

    private CompletableFuture<DonVi> getDonVi(String ma) {
        CompletableFuture<DonVi> future = new CompletableFuture<>();
        Log.d("check", "check async");
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

    public CompletableFuture<Void> updateUnitById(DonVi donvi) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        db.child(donvi.getMa()).setValue(donvi).addOnCompleteListener(new OnCompleteListener<Void>() {
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