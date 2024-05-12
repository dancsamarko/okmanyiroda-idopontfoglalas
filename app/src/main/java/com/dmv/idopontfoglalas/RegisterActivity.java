package com.dmv.idopontfoglalas;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText fullNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordAgainEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        fullNameEditText = findViewById(R.id.editTextFullName);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        passwordAgainEditText = findViewById(R.id.editTextPasswordAgain);

        mAuth = FirebaseAuth.getInstance();
    }


    @SuppressLint("RestrictedApi")
    public void register(View view) {
        String fullNameStr = fullNameEditText.getText().toString();
        String emailStr = emailEditText.getText().toString();
        String passwordStr = passwordEditText.getText().toString();
        String passwordAgainStr = passwordAgainEditText.getText().toString();

        if (!passwordStr.equals(passwordAgainStr)) {
            Toast.makeText(RegisterActivity.this, "Nem egyenlő a két jelszó.", Toast.LENGTH_LONG).show();
        }

        mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "User created");
                    startBooking();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció. " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void startBooking() {
        Intent intent = new Intent(this, BookingsListActivity.class);
        startActivity(intent);
    }

    public void cancel(View view) {
        finish();
    }
}