package com.example.tracknjeep_test.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tracknjeep_test.R;
import com.example.tracknjeep_test.auth.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private TextInputEditText editTextEmail, editTextPassword, editTextFirstName, editTextLastName, editTextGender, editTextBirthdate;
    private Button btnRegister;
    private ProgressBar progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        editTextEmail = findViewById(R.id.emailText);
        editTextPassword = findViewById(R.id.passwordText);
        editTextFirstName = findViewById(R.id.firstNameText);
        editTextLastName = findViewById(R.id.lastNameText);
        editTextGender = findViewById(R.id.genderText);
        editTextBirthdate = findViewById(R.id.birthdateText);
        progBar = findViewById(R.id.registerProgBar);
        btnRegister = findViewById(R.id.registerBtn);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        progBar.setVisibility(View.VISIBLE);
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        String birthdate = editTextBirthdate.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(firstName) ||
                TextUtils.isEmpty(lastName) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(birthdate)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            progBar.setVisibility(View.GONE);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            if (firebaseUser != null) {
                                // User successfully registered, now add user details to Realtime Database
                                ReadWriteUserDetails userDetails = new ReadWriteUserDetails(firstName, lastName, birthdate, gender, email);
                                DatabaseReference usersRef = FirebaseDatabase.getInstance(
                                        "https://tracknjeep-f4109-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users");

                                usersRef.child(firebaseUser.getUid()).setValue(userDetails)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Register.this, Login.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Log.e("Register", "Error adding user data to Realtime Database", task.getException());
                                                    Toast.makeText(Register.this, "Failed to create account.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}