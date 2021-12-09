package com.example.telegraph.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telegraph.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgetpassword extends AppCompatActivity {
    EditText eemail;
    Button reset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        mAuth = FirebaseAuth.getInstance();
        reset=findViewById(R.id.reset);
        eemail=findViewById(R.id.resetemail);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpassword();
            }
        });
    }

    private void resetpassword() {
        final String email=eemail.getText().toString().trim();
        if(email.isEmpty()){
            eemail.setError("email is requried");
            eemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            eemail.setError("please inter valid email");
            eemail.requestFocus();
            return;
        }
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Forgetpassword.this, " check yor email", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Forgetpassword.this, " some thing wrong happend!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}