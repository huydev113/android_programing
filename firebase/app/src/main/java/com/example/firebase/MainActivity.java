package com.example.firebase;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail;
    private Button buttonAdd, buttonUpdate, buttonDelete;
    private ListView listViewStudents;
    private List<Student> studentList;
    private ArrayAdapter<Student> arrayAdapter;
    private FirebaseDatabaseHelper databaseHelper;

    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        studentList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_1, studentList);
        listViewStudents = findViewById(R.id.listViewStudents);
        listViewStudents.setAdapter(arrayAdapter);
        databaseHelper = new FirebaseDatabaseHelper();
        Log.d("tag", databaseHelper.getReference().toString());

        loadStudents();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
                clearEdt();
            }
        });

        listViewStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editTextName.setText(studentList.get(position).getName());
                editTextEmail.setText(studentList.get(position).getEmail());
                index = position;
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
                clearEdt();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
                clearEdt();
            }
        });
    }

    private void addStudent() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String id = databaseHelper.getReference().push().getKey();
        Student student = new Student(id, name, email);
        databaseHelper.addStudent(student);
    }

    private void updateStudent() {
        String id = studentList.get(index).getId();
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        Student student = new Student(id, name, email);
        databaseHelper.updateStudent(id, student);
    }

    private void deleteStudent() {
        String id = studentList.get(index).getId();
        databaseHelper.deleteStudent(id);
    }

    private void loadStudents() {
        databaseHelper.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Student student = dataSnapshot.getValue(Student.class);
                    studentList.add(student);
                }
                Collections.reverse(studentList);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Load data failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearEdt() {
        editTextName.setText("");
        editTextEmail.setText("");
    }
}